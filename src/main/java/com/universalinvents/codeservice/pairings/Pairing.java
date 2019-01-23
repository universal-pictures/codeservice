package com.universalinvents.codeservice.pairings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.codeservice.codes.MasterCode;
import com.universalinvents.codeservice.codes.RetailerCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kkirkland on 10/25/17.
 */
@Entity
@Table(name = "pairing")
public class Pairing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @JsonIgnore
    private Long id;

    private String pairedBy;
    private Date pairedOn;
    @JsonIgnore
    private String status;

    @OneToOne
    @JoinColumn(name = "masterCode")
    @JsonIgnoreProperties(value = {"pairing", "content"})
    private MasterCode masterCode;

    @OneToOne
    @JoinColumn(name = "retailerCode")
    @JsonIgnoreProperties(value = {"pairing", "content"})
    private RetailerCode retailerCode;

    public Pairing () {}

    public Pairing(MasterCode masterCode, RetailerCode retailerCode, String pairedBy, String status) {
        this.masterCode = masterCode;
        this.retailerCode = retailerCode;
        this.pairedBy = pairedBy;
        this.pairedOn = new Date();
        this.status = status;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

