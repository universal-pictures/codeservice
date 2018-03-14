package com.universalinvents.udccs.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

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
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Allow anyone and anything access. Probably ok for Swagger spec
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "api_key", "Authorization"));

        source.registerCorsConfiguration("/api.*", config);
        return new CorsFilter(source);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Code API")
                .description(getDescription())
                .license("License TBD")
                .licenseUrl("License TBD")
                .version(BUILD_VERSION)
                .build();
    }

    private String getDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("NBCUniversal Digital Movie Code API<p/>");
        sb.append("<u><b>Database Schema:</b></u><br/>");
        sb.append("<img src='/images/APIIG_DB_Schema_V2.png'>");
        return (sb.toString());
    }
}
