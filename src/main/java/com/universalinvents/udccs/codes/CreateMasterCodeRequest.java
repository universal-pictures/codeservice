package com.universalinvents.udccs.codes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

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
                      required = true)
    private Long partnerId;
    @ApiModelProperty(value = "The id of the app that's being used to create this code. Required if *create* " +
                      "is 'true'",
                      example = "2",
                      required = true)
    private Long appId;
    @ApiModelProperty(value = "A field used to store any external identifier related to this code such as a " +
                      "transaction id.",
                      example = "123-53242-3234eff-2234f-231232",
                      required = false)
    private String externalId;
    @ApiModelProperty(value = "When does this code expire? This parameter is immutable so once you determine its " +
            "value here it can not be updated later. Use the following format: yyyy-MM-dd'T'HH:mm:ss.SSSZ. " +
            "If unspecified, this code will not expire.",
            example = "2020-01-01'T'00:00:01.000Z",
            required = false)
    private Date expiresOn;

    public CreateMasterCodeRequest() {
    }

    public CreateMasterCodeRequest(Long contentId, String format, String createdBy, Long partnerId, Long appId,
                                   String externalId, Date expiresOn) {
        this.contentId = contentId;
        this.format = format;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
        this.appId = appId;
        this.externalId = externalId;
        this.expiresOn = expiresOn;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }
}
