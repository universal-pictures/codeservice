package com.universalinvents.udccs.codes;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
public class CreateMasterCodeRequest {
    private Long contentId;
    private String createdBy;
    private Long partnerId;
    private Long appId;
    private Boolean create = Boolean.TRUE;  // Default to TRUE

    public CreateMasterCodeRequest() {
    }

    public CreateMasterCodeRequest(Long contentId, String createdBy, Long partnerId, Long appId,
                                   Boolean create) {
        this.contentId = contentId;
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
