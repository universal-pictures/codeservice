package com.universalinvents.codeservice.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.codeservice.codes.RetailerCode;

import java.util.Date;

public class RetailerCodeEvent extends AbstractEvent {

    private String code;
    private Long contentId;
    private String format;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date modifiedOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date expiresOn;
    private String pairedMasterCode;
    private Long retailerId;
    private RetailerCode.Status status;
    private String masterCodeExternalId;
    private String retailerExternalId;

    public RetailerCodeEvent(RetailerCode retailerCode) {
        this.code = retailerCode.getCode();
        this.contentId = retailerCode.getContent().getId();
        this.format = retailerCode.getFormat();
        this.createdOn = retailerCode.getCreatedOn();
        this.modifiedOn = retailerCode.getModifiedOn();
        this.expiresOn = retailerCode.getExpiresOn();
        this.pairedMasterCode = retailerCode.isPaired()
                ? retailerCode.getPairing().getMasterCode().getCode()
                : null;
        this.retailerId = retailerCode.getRetailer().getId();
        this.status = retailerCode.getStatus();
        this.masterCodeExternalId = retailerCode.isPaired()
                ? retailerCode.getPairing().getMasterCode().getExternalId()
                : null;
        this.retailerExternalId = retailerCode.isPaired()
                ? retailerCode.getRetailer().getExternalId()
                : null;
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

    public Long getContentId() {
        return contentId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getFormat() {
        return format;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public String getPairedMasterCode() {
        return pairedMasterCode;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public RetailerCode.Status getStatus() {
        return status;
    }

    public String getMasterCodeExternalId() {
        return masterCodeExternalId;
    }

    public String getRetailerExternalId() {
        return retailerExternalId;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }
}
