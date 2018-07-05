package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mmonti on 3/20/17.
 * Updated by kkirkland on 10/25/2017.
 */
@Entity
@Table(name = "retailer_code")
public class RetailerCode {

    // Legal status values
    public enum Status {
        PAIRED, REDEEMED, EXPIRED
    }

    @Id
    private String code;

    private Date createdOn;
    private Date modifiedOn;
    private Date expiresOn;
    private String format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('PAIRED', 'REDEEMED', 'EXPIRED')")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "retailerId")
    private Retailer retailer;

    @OneToOne(mappedBy = "retailerCode")
    private Pairing pairing;

    public RetailerCode() {
    }

    /**
     * @param content
     * @param retailer
     * @param code
     */
    public RetailerCode(final String code, final Content content, final String format, final Status status,
                        final Retailer retailer) {
        this.code = code;
        this.content = content;
        this.format = format;
        this.retailer = retailer;
        this.status = status;
        this.createdOn = new Date();
        this.modifiedOn = createdOn; // Use the same date for new objects
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getExpiresOn() { return expires_on; }

    public void setExpiresOn(Date expiresOn) { this.expiresOn = expiresOn; }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
