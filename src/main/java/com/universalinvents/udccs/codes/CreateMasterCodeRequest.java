package com.universalinvents.udccs.codes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
@ApiModel("CreateMasterCodeRequest")
public class CreateMasterCodeRequest {

    @ApiModelProperty(value = "The id of the related content (title)",
                      example = "1",
                      required = true)
    private Long contentId;
    @ApiModelProperty(value = "The video format of the content related to this code",
                      example = "HD",
                      required = true)
    private String format;
    @ApiModelProperty(value = "Who created this code? Value can be any string but an external customer id is preferred.",
                      example = "qas8fcw00sddergwpjkn4weiibnc925z",
                      required = false)
    private String createdBy;
    @ApiModelProperty(value = "The id of the referral partner that's creating this code. Required if *create* " +
                      "is 'true'",
                      example = "1",
                      required = false)
    private Long partnerId;
    @ApiModelProperty(value = "The id of the app that's being used to create this code. Required if *create* " +
                      "is 'true'",
                      example = "2",
                      required = false)
    private Long appId;
    @ApiModelProperty(value = "Should a new code be generated or just use an already ingested one? 'true' = " +
                      "generate new code, 'false' or not specified (default) = use ingested code",
                      example = "true",
                      required = false)
    private Boolean create = Boolean.FALSE;  // Default to FALSE

    public CreateMasterCodeRequest() {
    }

    public CreateMasterCodeRequest(Long contentId, String format, String createdBy, Long partnerId, Long appId,
                                   Boolean create) {
        this.contentId = contentId;
        this.format = format;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
        this.appId = appId;
        this.create = create;
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

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }
}
