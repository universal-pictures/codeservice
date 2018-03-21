package com.universalinvents.udccs.codes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.partners.ReferralPartner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dsherman on 2/27/17.
 * Modified by kkirkland on 10/25/2017.
 */
@Entity
@Table(name = "master_code", uniqueConstraints = {@UniqueConstraint(name = "uk_master_code", columnNames = {"code"})})
public class MasterCode {
    // Legal status values
    public enum Status {
        UNALLOCATED, ISSUED, PAIRED, REDEEMED
    }

    @Id
    private String code;

    private String createdBy;
    private Date createdOn;
    private Date modifiedOn;
    private String format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('UNALLOCATED', 'ISSUED', 'PAIRED', 'REDEEMED')")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "partnerId")
    @JsonIgnoreProperties(value = {"codes", "apps"})
    private ReferralPartner referralPartner;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "appId")
    @JsonIgnoreProperties("masterCodes")
    private App app;

    @OneToOne(mappedBy = "masterCode")
    private Pairing pairing;

    public MasterCode() {
    }

    public MasterCode(String code, String format, String createdBy, Date createdOn, ReferralPartner referralPartner,
                      App app, Content content, Status status) {
        this.code = code;
        this.format = format;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.referralPartner = referralPartner;
        this.app = app;
        this.content = content;
        this.status = status;
        this.modifiedOn = createdOn; // Make the dates match for new objects
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public ReferralPartner getReferralPartner() {
        return referralPartner;
    }

    public void setReferralPartner(ReferralPartner referralPartner) {
        this.referralPartner = referralPartner;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public void setPairing(Pairing pairing) {
        this.pairing = pairing;
    }

    public boolean isPaired() {
        return pairing != null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
//
//    public boolean isRedeemed() { return redeemedOn != null; }
}
