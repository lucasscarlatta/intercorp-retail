package com.example.demo.config;

import com.example.demo.exception.error.ApiError;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan({"com.example.demo", "com.example.demo.exception.error"})
public class SwaggerUiWebMvcConfigurer {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
                .select().apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(typeResolver.resolve(ApiError.class));
    }
}