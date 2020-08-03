package com.yishang.yspay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * http://localhost:8080/swagger-ui.html
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yishang.yspay.controller"))   //扫描接口的路径
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("亿商支付接口文档")  //标题
//                        .description("SpringBoot整合Swagger，详细信息......")   //详细信息
                        .version(" 1.0")
//                        .contact(new Contact("xiaohua","www.xiaohuayao.top","z962700753@gmail.com"))
//                        .license("The Apache License")
//                        .licenseUrl("http://www.baidu.com")
                        .build());
    }
}
