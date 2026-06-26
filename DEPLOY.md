# 清账 (QingZhang) 部署文档

## 系统要求

| 项目 | 最低版本 |
|------|---------|
| 操作系统 | CentOS 7+ / Ubuntu 20.04+ / Debian 11+ |
| Java | JDK 17+ |
| MySQL | 8.0+ / MariaDB 10.6+ |
| Nginx | 1.18+ |
| 内存 | 1GB+ |
| 磁盘 | 2GB+ |

---

## 一、全新部署（推荐）

### 准备部署包

从开发环境复制以下文件到目标服务器的同一目录：

```
qingzhang/
├── deploy.sh              # 一键部署脚本
├── finance-app.jar        # 后端 JAR (69MB)
├── dist/                  # 前端构建产物
│   ├── index.html
│   ├── assets/
│   └── login-illustration.svg
└── 00-full-database.sql   # 全量数据库 (254KB)
```

### 设置环境变量并执行

```bash
# 数据库配置
export DB_HOST="127.0.0.1"
export DB_PORT="3306"
export DB_NAME="finance_db"
export DB_USER="qingzhang"
export DB_PASS="your-db-password"

# JWT 密钥（生产环境请使用随机字符串）
export JWT_SECRET="your-random-jwt-secret"

# 端口（可选，默认 18080）
export SERVER_PORT="18080"

# 执行部署
chmod +x deploy.sh
./deploy.sh
```

部署完成后访问 `http://服务器IP` ，账号 `admin` 密码 `123456`。

---

## 二、手动部署

### 1. 安装依赖

```bash
# CentOS
yum install -y java-17-openjdk nginx mysql-server

# Ubuntu
apt install -y openjdk-17-jdk nginx mysql-server
```

### 2. 初始化数据库

```bash
mysql -u root -p << SQL
CREATE DATABASE IF NOT EXISTS finance_db DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'qingzhang'@'%' IDENTIFIED BY 'your-password';
GRANT ALL ON finance_db.* TO 'qingzhang'@'%';
FLUSH PRIVILEGES;
SQL

# 导入全量数据
mysql -u qingzhang -p finance_db < 00-full-database.sql
```

### 3. 部署后端

```bash
mkdir -p /opt/qingzhang/{jar,data/files,logs}

# 创建配置文件
cat > /opt/qingzhang/jar/application-prod.yml << 'YAML'
server:
  port: 18080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/finance_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: qingzhang
    password: your-db-password

jwt:
  secret: your-jwt-secret
  expire: 86400

file:
  upload-path: /opt/qingzhang/data/files/

logging:
  level:
    com.finance: info
    com.baomidou.mybatisplus: warn
  file:
    path: /opt/qingzhang/logs/
YAML

# 启动
java -Xms256m -Xmx512m \
  -jar finance-app.jar \
  --spring.profiles.active=prod \
  --spring.config.location=/opt/qingzhang/jar/application-prod.yml \
  > /opt/qingzhang/logs/api.log 2>&1 &
```

### 4. 部署前端

```bash
# 复制前端文件
cp -r dist/* /opt/qingzhang/frontend/

# 配置 Nginx
cat > /etc/nginx/conf.d/qingzhang.conf << 'NGINX'
server {
    listen 80;
    root /opt/qingzhang/frontend;
    index index.html;

    location /api/ {
        proxy_pass http://127.0.0.1:18080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        client_max_body_size 50m;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /assets/ {
        expires 7d;
        add_header Cache-Control "public, immutable";
    }
}
NGINX

nginx -t && nginx -s reload
```

### 5. 设置 systemd（开机自启）

```bash
cat > /etc/systemd/system/qingzhang-api.service << 'UNIT'
[Unit]
Description=清账 API
After=network.target

[Service]
Type=simple
WorkingDirectory=/opt/qingzhang
ExecStart=/usr/bin/java -Xms256m -Xmx512m -jar /opt/qingzhang/jar/finance-app.jar --spring.profiles.active=prod --spring.config.location=/opt/qingzhang/jar/application-prod.yml
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
UNIT

systemctl daemon-reload
systemctl enable qingzhang-api
systemctl start qingzhang-api
```

---

## 三、升级部署（已有数据库）

如果已有数据库，只升级应用层：

```bash
# 1. 停止旧服务
systemctl stop qingzhang-api

# 2. 导入增量SQL（如需新增菜单、科目等）
mysql -u qingzhang -p finance_db < 25-upgrade-data.sql

# 3. 替换 JAR
cp finance-app.jar /opt/qingzhang/jar/

# 4. 替换前端
rm -rf /opt/qingzhang/frontend/*
cp -r dist/* /opt/qingzhang/frontend/

# 5. 启动
systemctl start qingzhang-api
```

---

## 四、日志与维护

```bash
# 查看后端日志
tail -f /opt/qingzhang/logs/api.log

# 查看服务状态
systemctl status qingzhang-api

# 重启
systemctl restart qingzhang-api
systemctl reload nginx

# 备份数据库
mysqldump -u qingzhang -p finance_db > backup_$(date +%Y%m%d).sql
```

---

## 五、安全建议

1. **修改默认密码**: 登录后立即修改 admin 密码
2. **JWT 密钥**: 生产环境使用随机字符串，勿用默认值
3. **数据库密码**: 使用强密码，限制访问 IP
4. **HTTPS**: 生产环境配置 SSL 证书
5. **防火墙**: 只开放 80/443 端口，数据库端口仅本地访问
6. **日志脱敏**: 生产环境关闭 SQL 日志（`log-impl` 设为 `off`）

---

## 六、部署包清单

| 文件 | 说明 |
|------|------|
| `finance-app.jar` | Spring Boot 后端 (JDK17) |
| `dist/` | Vue3 前端构建产物 |
| `00-full-database.sql` | 全量库（77表 + 种子数据） |
| `25-upgrade-data.sql` | 增量升级 SQL |
| `deploy.sh` | 一键部署脚本 |
