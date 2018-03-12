package com.universalinvents.udccs.codes;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
public class IngestMasterCodeRequest {
    private Long contentId;
    private String format;
    private String createdBy;
    private Long partnerId;
    private Long appId;

    public IngestMasterCodeRequest() {
    }

    public IngestMasterCodeRequest(Long contentId, String format, String createdBy, Long partnerId, Long appId) {
        this.contentId = contentId;
        this.format = format;
        this.createdBy = createdBy;
        this.partnerId = partnerId;
        this.appId = appId;
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
}
