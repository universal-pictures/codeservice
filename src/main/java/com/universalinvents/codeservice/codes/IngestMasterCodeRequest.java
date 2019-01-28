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

package com.universalinvents.codeservice.codes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
@ApiModel("IngestMasterCodeRequest")
public class IngestMasterCodeRequest {

    @ApiModelProperty(value = "The id of the related content (title)",
                      example = "1",
                      required = true)
    private Long contentId;
    @ApiModelProperty(value = "The video format of the content related to this code",
                      example = "HD",
                      required = true)
    private String format;
    @ApiModelProperty(value = "Who ingested this code? Value can be any string but an external customer id is preferred.",
                      example = "qas8fcw00sddergwpjkn4weiibnc925z",
                      required = false)
    private String createdBy;
    @ApiModelProperty(value = "The id of the referral partner that's importing this code.",
                      example = "1",
                      required = false)
    private Long partnerId;
    @ApiModelProperty(value = "The id of the app that's being used to import this code.",
                      example = "2",
                      required = false)
    private Long appId;

    public IngestMasterCodeRequest() {
    }

    public IngestMasterCodeRequest(Long contentId, String format, String createdBy, Long partnerId, Long appId) {
        this.contentId = contentId;
        this.format = format;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
        this.appId = appId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
