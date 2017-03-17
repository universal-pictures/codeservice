package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class StudioCodeRequest
{
    private Long contentId;
    private String createdBy;
    private Long partnerId;

    public StudioCodeRequest() {}

    public StudioCodeRequest(Long contentId, String createdBy, Long partnerId)
    {
        this.contentId = contentId;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
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
}
