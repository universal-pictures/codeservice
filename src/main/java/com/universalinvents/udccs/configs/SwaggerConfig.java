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
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
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
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                                                      .select()
                                                      .apis(RequestHandlerSelectors.any())
                                                      .paths(PathSelectors.regex("/api.*"))
                                                      .build()
                                                      .useDefaultResponseMessages(false);
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
        return new ApiInfoBuilder().title("Code API")
                                   .description(getDescription())
                                   .license("License TBD")
                                   .licenseUrl("License TBD")
                                   .version(BUILD_VERSION)
                                   .build();
    }

    private String getDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("This API service provides digital e-Copy codes associated ")
          .append("to specific movie titles. The e-Copy code can later be paired ")
          .append("to a specific digital retailer redemption code, which would be ")
          .append("recognized and redeemable at a retailer platform such as:")
          .append("<ul>")
          .append("<li>Movies Anywhere</li>")
          .append("<li>iTunes</li>")
          .append("<li>Google Play</li>")
          .append("<li>Amazon Video</li>")
          .append("<li>Vudu</li>")
          .append("<li>Fandango Now</li>")
          .append("<li>Sky</li>")
          .append("</ul>");
        return (sb.toString());
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .defaultModelRendering(ModelRendering.MODEL)
                .build();
    }
}
