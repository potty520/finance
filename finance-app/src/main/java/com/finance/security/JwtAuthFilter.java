package com.finance.security;

import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.finance.module.system.entity.SysUser;
import com.finance.module.system.mapper.SysUserMapper;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    private final JwtUtil jwtUtil;

    @Resource
    private SysUserMapper userMapper;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(header);
        if (StringUtils.hasText(token) && token.startsWith(prefix)) {
            token = token.substring(prefix.length());
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                if (StringUtils.hasText(username)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    SysUser user = userMapper.selectByUsername(username);
                    if (user != null) {
                        List<String> perms = userMapper.selectPermCodesByUserId(user.getId());
                        List<SimpleGrantedAuthority> authorities = perms.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
                log.debug("JWT 解析异常: {}", e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 输出未授权响应
     */
    public static void writeUnauthorized(HttpServletResponse response, Integer code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(Result.error(code, message)));
        }
    }
}
