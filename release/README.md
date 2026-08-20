# 清账发布包

本目录包含已构建并完成线上验证的发布文件。

| 文件 | 说明 | SHA-256 |
|------|------|---------|
| `finance-app.jar` | Spring Boot 后端发布包，约 69MB | `15BF4A2E13E08D06DAA5E3712CFB2A22B27985D77844B425CF880F69FB14731A` |
| `finance-web-dist.zip` | Vite 前端生产构建包 | `CE464B48A16E275428EA84459CFC92EB2C914F128D94DA0AF1DE1575AF4340AB` |

## 前端升级

```bash
unzip -o finance-web-dist.zip -d /opt/qingzhang/frontend
find /opt/qingzhang/frontend -type d -exec chmod 755 {} +
find /opt/qingzhang/frontend -type f -exec chmod 644 {} +
nginx -t && systemctl reload nginx
```

## 后端升级

```bash
cp finance-app.jar /opt/qingzhang/jar/finance-app.jar
systemctl restart qingzhang-api
systemctl status qingzhang-api --no-pager
```

数据库配置、初始化和完整部署流程请参考仓库根目录 `DEPLOY.md`。
