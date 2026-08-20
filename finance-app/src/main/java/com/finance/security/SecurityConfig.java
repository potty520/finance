package com.finance.security;

import com.finance.common.response.Result;
import com.finance.common.response.ResultCode;
import com.alibaba.fastjson2.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security 配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * 允许匿名访问的路径
     */
    public static final String[] WHITE_LIST = {
            "/auth/login",
            "/auth/captcha",
            "/auth/logout",
            "/debug/encode",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/error",
            "/favicon.ico"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(WHITE_LIST).permitAll()
                // 所有写操作必须具备对应模块的写权限，不能只依赖前端菜单隐藏。
                .antMatchers(HttpMethod.POST, "/asset/**").hasAnyAuthority(
                        "fa:asset:add", "fa:asset:edit", "fa:category:add", "fa:category:edit",
                        "fa:depreciation:add", "fa:disposal:add", "fa:inventory:add")
                .antMatchers(HttpMethod.PUT, "/asset/**").hasAnyAuthority(
                        "fa:asset:edit", "fa:category:edit")
                .antMatchers(HttpMethod.DELETE, "/asset/**").hasAnyAuthority(
                        "fa:asset:delete", "fa:category:delete")
                .antMatchers(HttpMethod.POST, "/budget/**").hasAnyAuthority(
                        "budget:master:add", "budget:master:edit", "budget:exec:add", "budget:exec:edit")
                .antMatchers(HttpMethod.PUT, "/budget/**").hasAnyAuthority(
                        "budget:master:edit", "budget:exec:edit")
                .antMatchers(HttpMethod.DELETE, "/budget/**").hasAuthority("budget:master:delete")
                .antMatchers(HttpMethod.POST, "/cashier/**").hasAnyAuthority(
                        "cash:account:add", "cash:account:edit", "cash:journal:add", "cash:journal:edit",
                        "cash:reconcile:add", "cash:reconcile:edit", "cash:bill:add", "cash:bill:edit")
                .antMatchers(HttpMethod.PUT, "/cashier/**").hasAnyAuthority(
                        "cash:account:edit", "cash:journal:edit", "cash:reconcile:edit", "cash:bill:edit")
                .antMatchers(HttpMethod.DELETE, "/cashier/**").hasAnyAuthority(
                        "cash:account:delete", "cash:journal:delete", "cash:reconcile:delete", "cash:bill:delete")
                .antMatchers(HttpMethod.POST, "/consol/**").hasAnyAuthority(
                        "consolidation:group:add", "consolidation:group:edit", "consolidation:elim:add",
                        "consolidation:elim:edit", "consolidation:worksheet:add", "consolidation:worksheet:edit")
                .antMatchers(HttpMethod.PUT, "/consol/**").hasAnyAuthority(
                        "consolidation:group:edit", "consolidation:elim:edit", "consolidation:worksheet:edit")
                .antMatchers(HttpMethod.DELETE, "/consol/**").hasAnyAuthority(
                        "consolidation:group:delete", "consolidation:elim:delete", "consolidation:worksheet:delete")
                .antMatchers(HttpMethod.POST, "/contract/**").hasAnyAuthority(
                        "contract:sales:add", "contract:sales:edit", "contract:purchase:add", "contract:purchase:edit")
                .antMatchers(HttpMethod.PUT, "/contract/**").hasAnyAuthority(
                        "contract:sales:edit", "contract:purchase:edit")
                .antMatchers(HttpMethod.DELETE, "/contract/**").hasAnyAuthority(
                        "contract:sales:delete", "contract:purchase:delete")
                .antMatchers(HttpMethod.POST, "/cost/**").hasAnyAuthority(
                        "cost:center:add", "cost:center:edit", "cost:element:add", "cost:element:edit",
                        "cost:calc:add", "cost:calc:edit")
                .antMatchers(HttpMethod.PUT, "/cost/**").hasAnyAuthority(
                        "cost:center:edit", "cost:element:edit", "cost:calc:edit")
                .antMatchers(HttpMethod.DELETE, "/cost/**").hasAnyAuthority(
                        "cost:center:delete", "cost:element:delete", "cost:calc:delete")
                .antMatchers(HttpMethod.POST, "/expense/**").hasAnyAuthority(
                        "expense:apply:add", "expense:repay:add", "expense:loan:add")
                .antMatchers(HttpMethod.PUT, "/expense/**").hasAnyAuthority(
                        "expense:apply:edit", "expense:repay:edit", "expense:loan:edit")
                .antMatchers(HttpMethod.DELETE, "/expense/**").hasAnyAuthority(
                        "expense:apply:delete", "expense:repay:delete", "expense:loan:delete")
                .antMatchers(HttpMethod.POST, "/ledger/**").hasAnyAuthority(
                        "gl:subject:add", "gl:aux:add", "gl:period:add", "gl:voucher:add", "gl:voucher:audit",
                        "gl:voucher:post", "gl:voucher:reverse")
                .antMatchers(HttpMethod.PUT, "/ledger/**").hasAnyAuthority(
                        "gl:subject:edit", "gl:aux:edit", "gl:period:edit", "gl:voucher:edit")
                .antMatchers(HttpMethod.DELETE, "/ledger/**").hasAnyAuthority(
                        "gl:subject:delete", "gl:aux:delete", "gl:period:delete", "gl:voucher:delete")
                .antMatchers(HttpMethod.POST, "/payable/**").hasAnyAuthority(
                        "ap:supplier:add", "ap:invoice:add", "ap:payment:add", "ap:writeoff:add",
                        "ap:invoice:edit", "ap:payment:edit")
                .antMatchers(HttpMethod.PUT, "/payable/**").hasAnyAuthority(
                        "ap:supplier:edit", "ap:invoice:edit", "ap:payment:edit")
                .antMatchers(HttpMethod.DELETE, "/payable/**").hasAnyAuthority(
                        "ap:supplier:delete", "ap:invoice:delete", "ap:payment:delete")
                .antMatchers(HttpMethod.POST, "/project/**").hasAnyAuthority(
                        "project:add", "project:edit", "project:budget:add", "project:cost:add")
                .antMatchers(HttpMethod.PUT, "/project/**").hasAnyAuthority(
                        "project:edit", "project:budget:edit", "project:cost:edit")
                .antMatchers(HttpMethod.DELETE, "/project/**").hasAnyAuthority(
                        "project:delete", "project:budget:delete", "project:cost:delete")
                .antMatchers(HttpMethod.POST, "/receivable/**").hasAnyAuthority(
                        "ar:customer:add", "ar:invoice:add", "ar:receipt:add", "ar:writeoff:add",
                        "ar:invoice:edit", "ar:receipt:edit")
                .antMatchers(HttpMethod.PUT, "/receivable/**").hasAnyAuthority(
                        "ar:customer:edit", "ar:invoice:edit", "ar:receipt:edit")
                .antMatchers(HttpMethod.DELETE, "/receivable/**").hasAnyAuthority(
                        "ar:customer:delete", "ar:invoice:delete", "ar:receipt:delete")
                .antMatchers(HttpMethod.POST, "/report/**").hasAnyAuthority(
                        "report:bs", "report:is", "report:cf", "report:custom", "report:custom:add")
                .antMatchers(HttpMethod.PUT, "/report/**").hasAuthority("report:custom:edit")
                .antMatchers(HttpMethod.DELETE, "/report/**").hasAuthority("report:custom:delete")
                .antMatchers(HttpMethod.POST, "/stock/**").hasAnyAuthority(
                        "inventory:item:add", "inventory:item:edit", "inventory:warehouse:add",
                        "inventory:warehouse:edit", "inventory:trans:add", "inventory:adjust:add")
                .antMatchers(HttpMethod.PUT, "/stock/**").hasAnyAuthority(
                        "inventory:item:edit", "inventory:warehouse:edit", "inventory:adjust:edit")
                .antMatchers(HttpMethod.DELETE, "/stock/**").hasAnyAuthority(
                        "inventory:item:delete", "inventory:warehouse:delete", "inventory:adjust:delete")
                .antMatchers(HttpMethod.POST, "/system/user/changePwd").authenticated()
                .antMatchers(HttpMethod.POST, "/system/company/**").hasAuthority("system:company:add")
                .antMatchers(HttpMethod.PUT, "/system/company/**").hasAuthority("system:company:edit")
                .antMatchers(HttpMethod.DELETE, "/system/company/**").hasAuthority("system:company:delete")
                .antMatchers(HttpMethod.POST, "/system/**").hasAnyAuthority(
                        "system:user:add", "system:role:add", "system:menu:add", "system:dept:add",
                        "system:dict:add", "system:config:add", "system:log:login", "system:log:operation")
                .antMatchers(HttpMethod.PUT, "/system/**").hasAnyAuthority(
                        "system:user:edit", "system:role:edit", "system:menu:edit", "system:dept:edit",
                        "system:dict:edit", "system:config:edit")
                .antMatchers(HttpMethod.DELETE, "/system/**").hasAnyAuthority(
                        "system:user:delete", "system:role:delete", "system:menu:delete", "system:dept:delete",
                        "system:dict:delete", "system:config:delete", "system:log:login", "system:log:operation")
                .antMatchers(HttpMethod.GET, "/tax/**").hasAuthority("tax:vat")
                .antMatchers(HttpMethod.GET, "/system/log/**").hasAnyAuthority(
                        "system:log:login", "system:log:operation")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, e) -> {
                    resp.setStatus(200);
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write(JSON.toJSONString(Result.error(ResultCode.UNAUTHORIZED)));
                })
                .accessDeniedHandler((req, resp, e) -> {
                    resp.setStatus(200);
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write(JSON.toJSONString(Result.error(ResultCode.FORBIDDEN)));
                })
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
