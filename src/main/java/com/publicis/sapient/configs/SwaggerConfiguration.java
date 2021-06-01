package com.publicis.sapient.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfiguration {

    @Bean
    public Docket postsApi() {
        log.info("Configuring swagger ... ");
        return new Docket(DocumentationType.SWAGGER_2).
                groupName("public-api")
                .apiInfo(apiInfo()).select()
                .paths(PathSelectors.ant("/football-service/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("FootBall Service Rest APIs")
                .description("Small demo service")
                .contact(new Contact("Vishesh Awasthi", "https://github.com/vishesh-awasthi/football-service", "visheshkawasthi@gmail.com"))
                .version("1.0").build();
    }
}

