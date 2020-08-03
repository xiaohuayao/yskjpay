package com.yishang.yspay.config;

import com.yishang.yspay.interceptors.JWTAuthorizationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author zhangjun
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/pay/user/login").permitAll()
                .antMatchers("/pay/merchant/uploadFile").permitAll()
                .antMatchers("/pay/user/getcode").permitAll()
                .antMatchers("/pay/order/exportOrder").permitAll()
                .antMatchers("/pay/order/exportExcelOrder").permitAll()
                //过滤Swagger相关-wyr
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**",
                        "/swagger-resources/configuration/ui", "/swagge‌​r-ui.html").permitAll()

                .antMatchers(HttpMethod.OPTIONS).permitAll()

                //.antMatchers(HttpMethod.POST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
