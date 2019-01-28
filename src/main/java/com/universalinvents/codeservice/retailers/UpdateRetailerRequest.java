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

package com.universalinvents.codeservice.retailers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 */
@ApiModel("UpdateRetailerRequest")
public class UpdateRetailerRequest {
    @ApiModelProperty(value = "The name of the retailer",
                      example = "Movies Anywhere",
                      required = false)
    private String name;
    @ApiModelProperty(value = "A URL pointing to a logo for this retailer",
                      example = "http://example.com/mylogoimage.png",
                      required = false)
    private String logoUrl;
    @ApiModelProperty(value = "Where to send users when redeeming codes for this retailer",
                      example = "http://example.com/redeem/",
                      required = false)
    private String redemptionUrl;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = false)
    private String status;
    @ApiModelProperty(value = "A field used to store any external identifier related to this retailer",
            example = "123-53242-3234eff-2234f-231232",
            required = false)
    private String externalId;
    @ApiModelProperty(value="The base URL for this retailer's Code Management Service API",
            example = "https://example.com",
            required = false)
    private String baseUrl;

    public UpdateRetailerRequest() {}

    public UpdateRetailerRequest(String name, String logoUrl, String redemptionUrl,
                                 String status, String externalId, String baseUrl) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.redemptionUrl = redemptionUrl;
        this.status = status;
        this.externalId = externalId;
        this.baseUrl = baseUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRedemptionUrl() {
        return redemptionUrl;
    }

    public void setRedemptionUrl(String redemptionUrl) {
        this.redemptionUrl = redemptionUrl;
    }

    public String getExternalId() { return externalId; }

    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getBaseUrl() { return baseUrl; }

    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}
