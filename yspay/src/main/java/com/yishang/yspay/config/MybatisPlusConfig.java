package com.yishang.yspay.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 分页插件配置类
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor PaginationInterceptor(){
        return new PaginationInterceptor();
    }


}
