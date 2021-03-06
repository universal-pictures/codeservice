/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.configs;

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


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${build.version}")
    private String BUILD_VERSION;

    @Value("${swagger.ui.host}")
    private String SWAGGER_UI_HOST;

    @Value("${swagger.ui.protocol}")
    private String SWAGGER_UI_PROTOCOL;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .protocols(Collections.singleton(SWAGGER_UI_PROTOCOL))
                .host(SWAGGER_UI_HOST)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.universalinvents.codeservice"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .useDefaultResponseMessages(false);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Allow anyone and anything access. Probably ok for Swagger spec
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "api_key", "Authorization"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Code Service")
                .description(getDescription())
                .license("Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
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
