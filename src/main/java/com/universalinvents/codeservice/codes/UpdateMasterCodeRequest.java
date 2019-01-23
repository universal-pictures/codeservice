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
