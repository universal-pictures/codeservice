package com.universalinvents.udccs.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.codes.MasterCode;

import java.util.Date;

public class MasterCodeEvent extends AbstractEvent {

    private String code;
    private Long appId;
    private Long contentId;

    private String createdBy;
    private Date createdOn;
    private Date modifiedOn;
    private String externalId;
    private String format;
    private String pairedRetailerCode;
    private MasterCode.Status status;

    public MasterCodeEvent(MasterCode masterCode) {
        this.code = masterCode.getCode();
        this.appId = masterCode.getApp().getId();
        this.contentId = masterCode.getContent().getId();
        this.createdBy = masterCode.getCreatedBy();
        this.createdOn = masterCode.getCreatedOn();
        this.modifiedOn = masterCode.getModifiedOn();
        this.externalId = masterCode.getExternalId();
        this.format = masterCode.getFormat();
        this.pairedRetailerCode = masterCode.isPaired()
                ? masterCode.getPairing().getRetailerCode().getCode()
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

    public String getExternalId() {
        return externalId;
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

}
