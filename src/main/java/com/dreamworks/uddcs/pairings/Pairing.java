package com.dreamworks.uddcs.pairings;

import com.dreamworks.uddcs.codes.MasterCode;
import com.dreamworks.uddcs.codes.RetailerCode;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by kkirkland on 10/25/17.
 */
@Entity
@Table(name = "pairing")
public class Pairing {

    @EmbeddedId
    @JsonProperty("id")
    private PairingPK id;  // masterCode & retailerCode

    @JsonProperty("pairedBy")
    private String pairedBy;
    
    @JsonProperty("pairedOn")
    private Date pairedOn;
    
    @JsonProperty("status")
    private String status;

    @ManyToOne
    @JsonProperty("masterCode")
    public MasterCode masterCode;

    @ManyToOne
    @JsonProperty("retailerCode")
    public RetailerCode retailerCode;

    public Pairing () {}

    public Pairing(PairingPK id, String pairedBy, String status) {
        this.id = id;
        this.pairedBy = pairedBy;
        this.pairedOn = new Date();
        this.status = status;
    }

    public PairingPK getId() {
        return id;
    }

    public void setId(PairingPK id) {
        this.id = id;
    }

    public String getPairedBy() {
        return pairedBy;
    }

    public void setPairedBy(String pairedBy) {
        this.pairedBy = pairedBy;
    }

    public Date getPairedOn() {
        return pairedOn;
    }

    public void setPairedOn(Date pairedOn) {
        this.pairedOn = pairedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MasterCode getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(MasterCode masterCode) {
        this.masterCode = masterCode;
    }

    public RetailerCode getRetailerCode() {
        return retailerCode;
    }

    public void setRetailerCode(RetailerCode retailerCode) {
        this.retailerCode = retailerCode;
    }
}

