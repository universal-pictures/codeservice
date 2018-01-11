package com.universalinvents.udccs.codes;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
public class MasterCodeRequest
{
    private String code;
    private Long studioId;
    private Long contentId;
    private String createdBy;
    private Long partnerId;
    private Long appId;
    private String status;

    public MasterCodeRequest() {}

    public MasterCodeRequest(String code, Long studioId, Long contentId, String createdBy, Long partnerId, Long appId, String status)
    {
        this.code = code;
        this.studioId = studioId;
        this.contentId = contentId;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
        this.appId = appId;
        this.status = status;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
