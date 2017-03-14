package com.dreamworks.uddcs.codes;

/**
 * Created by dsherman on 2/27/17.
 */
public class StudioCodeRequest
{
    private String contentId;
    private String createdBy;
    private String partnerId;

    public StudioCodeRequest() {}

    public StudioCodeRequest(String contentId, String createdBy, String partnerId)
    {
        this.contentId = contentId;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getPartnerId()
    {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }
}
