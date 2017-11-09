package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
public class RetailerCodeRequest
{
    private String code;
    private Long contentId;
    private Long retailerId;
    private String format;

    public RetailerCodeRequest() {}

    public RetailerCodeRequest(String code, Long contentId, Long partnerId, String format)
    {
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
