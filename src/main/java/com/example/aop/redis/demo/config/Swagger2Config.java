package com.example.aop.redis.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                // 为扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.aop.redis.demo.controller")).paths(
                        PathSelectors.any())
                .build();
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // //大标题
                .title("注解方式实现Spring AOP 整合Redis 缓存RESTfull API")
                // 版本号
                .version("1.0")
                // 描述
                .description("API 描述")
                //作者
                .contact(new Contact("eric.he", "http://www.swagger-ui.com", "heyong_1988@outlook.com"))
                .build();
    }
}
