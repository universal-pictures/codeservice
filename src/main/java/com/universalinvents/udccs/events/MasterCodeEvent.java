package com.universalinvents.udccs.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.codes.MasterCode;

import java.util.Date;

public class MasterCodeEvent extends AbstractEvent {

    private String code;
    private Long appId;
    private Long contentId;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date modifiedOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date expiresOn;
    private String masterCodeExternalId;
    private String retailerExternalId;
    private String format;
    private String pairedRetailerCode;
    private MasterCode.Status status;

    public MasterCodeEvent(MasterCode masterCode) {
        this.code = masterCode.getCode();
        this.appId = masterCode.getApp().getId();
        this.contentId = masterCode.getContent().getId();
        this.createdBy = masterCode.getCreatedBy();
        this.createdOn = masterCode.getCreatedOn();
        this.expiresOn = masterCode.getExpiresOn();
        this.modifiedOn = masterCode.getModifiedOn();
        this.masterCodeExternalId = masterCode.getExternalId();
        this.format = masterCode.getFormat();
        this.pairedRetailerCode = masterCode.isPaired()
                ? masterCode.getPairing().getRetailerCode().getCode()
                : null;
        this.retailerExternalId = masterCode.isPaired()
                ? masterCode.getPairing().getRetailerCode().getRetailer().getExternalId()
                : null;
        this.status = masterCode.getStatus();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public Long getAppId() {
        return appId;
    }

    public Long getContentId() {
        return contentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public String getMasterCodeExternalId() {
        return masterCodeExternalId;
    }

    public String getRetailerExternalId() {
        return retailerExternalId;
    }

    public String getFormat() {
        return format;
    }

    public String getPairedRetailerCode() {
        return pairedRetailerCode;
    }

    public MasterCode.Status getStatus() {
        return status;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }
}
