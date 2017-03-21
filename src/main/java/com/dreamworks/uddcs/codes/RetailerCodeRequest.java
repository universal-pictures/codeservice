package com.dreamworks.uddcs.codes;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by dsherman on 2/27/17.
 */
public class RetailerCodeRequest
{
    private Long contentId;
    private Long retailerId;
    @NotEmpty
    private String code;

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
