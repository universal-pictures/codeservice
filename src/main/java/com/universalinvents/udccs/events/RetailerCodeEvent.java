package com.universalinvents.udccs.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalinvents.udccs.codes.RetailerCode;

import java.util.Date;

public class RetailerCodeEvent extends AbstractEvent {

    private String code;
    private Long contentId;
    private Date createdOn;
    private String format;
    private Date modifiedOn;
    private String pairedMasterCode;
    private Long retailerId;
    private RetailerCode.Status status;

    public RetailerCodeEvent(RetailerCode retailerCode) {
        this.code = retailerCode.getCode();
        this.contentId = retailerCode.getContent().getId();
        this.createdOn = retailerCode.getCreatedOn();
        this.format = retailerCode.getFormat();
        this.modifiedOn = retailerCode.getModifiedOn();
        this.pairedMasterCode = retailerCode.isPaired()
                ? retailerCode.getPairing().getMasterCode().getCode()
                : null;
        this.retailerId = retailerCode.getRetailer().getId();
        this.status = retailerCode.getStatus();
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

}
