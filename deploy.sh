# 清账 - 一键部署脚本

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}   清账 (QingZhang) 一键部署脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# ==================== 配置区 ====================
# 根据实际情况修改以下变量，或者通过环境变量传入

APP_DIR="${APP_DIR:-/opt/qingzhang}"
DB_HOST="${DB_HOST:-127.0.0.1}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-finance_db}"
DB_USER="${DB_USER:-qingzhang}"
DB_PASS="${DB_PASS:-change-me-please}"
JWT_SECRET="${JWT_SECRET:-qingzhang-jwt-secret-$(date +%s)}"
SERVER_PORT="${SERVER_PORT:-18080}"
NGINX_PORT="${NGINX_PORT:-80}"
JAVA_OPTS="${JAVA_OPTS:--Xms256m -Xmx512m}"

# ==================== 检查 ====================
command -v java >/dev/null 2>&1 || { echo -e "${RED}错误: 需要 Java 17+${NC}"; exit 1; }
command -v mysql >/dev/null 2>&1 || { echo -e "${RED}错误: 需要 MySQL 客户端${NC}"; exit 1; }
command -v nginx >/dev/null 2>&1 || echo -e "${YELLOW}警告: 未找到 nginx，前端需手动托管${NC}"

JAVA_VER=$(java -version 2>&1 | head -1 | grep -oP '\d+' | head -1)
if [ "$JAVA_VER" -lt 17 ]; then
    echo -e "${RED}错误: Java 版本需 >= 17，当前: $JAVA_VER${NC}"
    exit 1
fi

# ==================== 创建目录 ====================
echo -e "${YELLOW}[1/6] 创建部署目录...${NC}"
mkdir -p "$APP_DIR"/{jar,frontend,data/files,logs,sql}

# 复制部署包（假设脚本和部署包在同一目录）
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -f "$SCRIPT_DIR/finance-app.jar" ]; then
    cp "$SCRIPT_DIR/finance-app.jar" "$APP_DIR/jar/"
elif [ -f "$SCRIPT_DIR/../finance-app/target/finance-app.jar" ]; then
    cp "$SCRIPT_DIR/../finance-app/target/finance-app.jar" "$APP_DIR/jar/"
else
    echo -e "${RED}错误: 未找到 finance-app.jar${NC}"
    echo "请将 JAR 包放到脚本同目录或先执行 mvn package"
    exit 1
fi

if [ -d "$SCRIPT_DIR/dist" ]; then
    cp -r "$SCRIPT_DIR/dist/"* "$APP_DIR/frontend/"
elif [ -d "$SCRIPT_DIR/../finance-web/dist" ]; then
    cp -r "$SCRIPT_DIR/../finance-web/dist/"* "$APP_DIR/frontend/"
else
    echo -e "${RED}错误: 未找到前端 dist 目录${NC}"
    echo "请先执行 vite build 并将 dist 放到脚本同目录"
    exit 1
fi

if [ -f "$SCRIPT_DIR/00-full-database.sql" ]; then
    cp "$SCRIPT_DIR/00-full-database.sql" "$APP_DIR/sql/"
elif [ -f "$SCRIPT_DIR/../sql/00-full-database.sql" ]; then
    cp "$SCRIPT_DIR/../sql/00-full-database.sql" "$APP_DIR/sql/"
else
    echo -e "${RED}错误: 未找到 00-full-database.sql${NC}"
    exit 1
fi

echo -e "${GREEN}目录创建完成${NC}"

# ==================== 数据库 ====================
echo -e "${YELLOW}[2/6] 初始化数据库...${NC}"

# 创建数据库（如不存在）
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;" 2>/dev/null

# 导入全量数据
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$APP_DIR/sql/00-full-database.sql" 2>/dev/null

echo -e "${GREEN}数据库初始化完成${NC}"

# ==================== 后端配置 ====================
echo -e "${YELLOW}[3/6] 生成后端配置...${NC}"

cat > "$APP_DIR/jar/application-prod.yml" << YAML
server:
  port: ${SERVER_PORT}
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: ${DB_USER}
    password: ${DB_PASS}

jwt:
  secret: ${JWT_SECRET}
  expire: 86400

file:
  upload-path: ${APP_DIR}/data/files/

logging:
  level:
    com.finance: info
    com.baomidou.mybatisplus: warn
  file:
    path: ${APP_DIR}/logs/
YAML

echo -e "${GREEN}后端配置完成${NC}"

# ==================== 后端服务 ====================
echo -e "${YELLOW}[4/6] 配置后端服务...${NC}"

cat > /etc/systemd/system/qingzhang-api.service << UNIT
[Unit]
Description=清账 API Service
After=network.target mysql.service

[Service]
Type=simple
User=root
WorkingDirectory=${APP_DIR}
ExecStart=/usr/bin/java ${JAVA_OPTS} -jar ${APP_DIR}/jar/finance-app.jar --spring.profiles.active=prod --spring.config.location=${APP_DIR}/jar/application-prod.yml
Restart=on-failure
RestartSec=10
StandardOutput=append:${APP_DIR}/logs/api.log
StandardError=append:${APP_DIR}/logs/api-error.log

[Install]
WantedBy=multi-user.target
UNIT

systemctl daemon-reload
systemctl enable qingzhang-api
systemctl restart qingzhang-api

echo -e "${GREEN}后端服务已启动${NC}"

# ==================== Nginx ====================
echo -e "${YELLOW}[5/6] 配置 Nginx...${NC}"

if command -v nginx >/dev/null 2>&1; then
    cat > /etc/nginx/conf.d/qingzhang.conf << 'NGINX'
server {
    listen       80;
    server_name  localhost;

    # 前端静态文件
    root   /opt/qingzhang/frontend;
    index  index.html;

    # API 反向代理
    location /api/ {
        proxy_pass http://127.0.0.1:18080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        client_max_body_size 50m;
    }

    # 前端路由 history 模式
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 文件上传下载
    location /api/file/ {
        proxy_pass http://127.0.0.1:18080/api/file/;
        proxy_set_header Host $host;
    }

    # 静态资源缓存
    location /assets/ {
        expires 7d;
        add_header Cache-Control "public, immutable";
    }
}
NGINX

    nginx -t && systemctl reload nginx
    echo -e "${GREEN}Nginx 配置完成${NC}"
else
    echo -e "${YELLOW}跳过 Nginx 配置 (未安装)${NC}"
fi

# ==================== 验证 ====================
echo -e "${YELLOW}[6/6] 验证部署...${NC}"
sleep 3

# 检查后端
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"***"}' \
    "http://127.0.0.1:${SERVER_PORT}/api/auth/login" 2>/dev/null || echo "000")

if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✅ 后端 API 正常${NC}"
else
    echo -e "${RED}❌ 后端 API 异常 (HTTP $HTTP_CODE)${NC}"
    echo "查看日志: journalctl -u qingzhang-api -f"
fi

# 检查前端
if command -v nginx >/dev/null 2>&1; then
    FRONT_CODE=$(curl -s -o /dev/null -w "%{http_code}" "http://127.0.0.1:${NGINX_PORT}/" 2>/dev/null)
    if [ "$FRONT_CODE" = "200" ]; then
        echo -e "${GREEN}✅ 前端页面正常${NC}"
    else
        echo -e "${RED}❌ 前端页面异常 (HTTP $FRONT_CODE)${NC}"
    fi
fi

# ==================== 完成 ====================
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  部署完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "访问地址: http://服务器IP"
echo -e "默认账号: admin"
echo -e "默认密码: 123456"
echo ""
echo -e "服务管理:"
echo -e "  后端: systemctl {start|stop|restart|status} qingzhang-api"
echo -e "  日志: tail -f ${APP_DIR}/logs/api.log"
echo -e "  前端: systemctl {start|stop|restart|reload} nginx"
echo ""
echo -e "${YELLOW}⚠️  请立即修改默认密码！${NC}"
echo ""
