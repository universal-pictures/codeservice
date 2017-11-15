package com.dreamworks.udccs.pairings;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by kkirkland on 10/25/17.
 */
@Entity
@Table(name = "pairing")
public class Pairing {

    @EmbeddedId
    private PairingPK id;  // masterCode & retailerCode

    private String pairedBy;
    private Date pairedOn;
    private String status;

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
}

