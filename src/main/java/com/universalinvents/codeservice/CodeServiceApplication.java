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

package com.universalinvents.codeservice;

import com.universalinvents.codeservice.utilities.CodeServiceRetryListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@EnableRetry
@SpringBootApplication
public class CodeServiceApplication {

    @Value("${resttemplateutil.max-retries}")
    private int maxAttempts;

    @Value("${resttemplateutil.retry-delay}")
    private int retryDelay;

    public static void main(String[] args)
    {
        SpringApplication.run(CodeServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = Collections.singletonMap(
                HttpServerErrorException.class, Boolean.TRUE);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, retryableExceptions);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(retryDelay);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);
        template.registerListener(new CodeServiceRetryListener());

        return template;
    }
}
