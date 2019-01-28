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

package com.universalinvents.codeservice.apps;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by kkirkland on 11/7/17.
 */
@ApiModel("AppRequest")
public class AppRequest {

    @ApiModelProperty(value = "The name of the app",
                      example = "iOS App",
                      required = true)
    private String name;
    @ApiModelProperty(value = "A short description of the app",
                      example = "My Company's iOS Application",
                      required = true)
    private String description;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = true)
    private String status;
    @ApiModelProperty(value = "The id of the related Referral Partner",
                      example = "1",
                      required = true)
    private Long partnerId;
    @ApiModelProperty(value = "Unique api access token (received from AWS)",
                      example = "2mmu5pbq316h6tu2g44e5rx43ba",
                      required = true)
    private String accessToken;

    public AppRequest() {
    }

    public AppRequest(String name, String description, String status, Long partnerId, String accessToken) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.partnerId = partnerId;
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
