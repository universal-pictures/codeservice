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

@ApiModel("UpdateMasterCodeRequest")
public class UpdateMasterCodeRequest {
    @ApiModelProperty(value = "The id of the related content (title)",
            example = "1",
            required = true)
    private Long contentId;

    public UpdateMasterCodeRequest() {}

    public UpdateMasterCodeRequest(Long contentId) {
        this.contentId = contentId;
    }

    public Long getContentId() { return contentId; }

    public void setContentId(Long contentId) { this.contentId = contentId; }
}
