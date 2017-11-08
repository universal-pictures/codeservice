package com.dreamworks.uddcs.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by dsherman on 2/24/17.
 * Updated by kkirkland on 11/7/17.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${build.version}")
    private String BUILD_VERSION;

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }


    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                .title("Media Everywhere")
                .description("NBCUniversal Media Everywhere API")
                .license("License TBD")
                .licenseUrl("License TBD")
                .version(BUILD_VERSION)
                .build();
    }
}
