package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class RetailerCodeRequest
{
    private String code;
    private Long contentId;
    private Long retailerId;

    public RetailerCodeRequest() {}

    public RetailerCodeRequest(Long contentId, Long partnerId)
    {
        this.contentId = contentId;
        this.retailerId = partnerId;
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
}
