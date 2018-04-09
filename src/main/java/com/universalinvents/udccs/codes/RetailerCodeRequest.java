package com.universalinvents.udccs.codes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
@ApiModel("RetailerCodeRequest")
public class RetailerCodeRequest
{
    @ApiModelProperty(value = "The id of the related Content (title)",
                      example = "1",
                      required = true)
    private Long contentId;
    @ApiModelProperty(value = "The id of the related Retailer",
                      example = "1",
                      required = true)
    private Long retailerId;
    @ApiModelProperty(value = "The format of the related Content for this code",
                      example = "HD",
                      required = true)
    private String format;

    public RetailerCodeRequest() {}

    public RetailerCodeRequest(Long contentId, Long partnerId, String format)
    {
        this.contentId = contentId;
        this.retailerId = partnerId;
        this.format = format;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
