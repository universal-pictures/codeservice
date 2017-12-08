package com.universalinvents.udccs.apps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.udccs.partners.ReferralPartner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kkirkland on 10/19/17.
 */
@Entity
@Table(name = "app", uniqueConstraints = {@UniqueConstraint(name = "uk_app", columnNames = {"id"})})
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Date createdOn;
    private Date modifiedOn;
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partnerId")
    @JsonIgnoreProperties("apps")
    private ReferralPartner referralPartner;

    public App() {
    }

    public App(Long id, String name, String description, Date modifiedOn, String status,
               ReferralPartner referralPartner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdOn = new Date();
        this.modifiedOn = modifiedOn;
        this.status = status;
        this.referralPartner = referralPartner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReferralPartner getReferralPartner() {
        return referralPartner;
    }

    public void setReferralPartner(ReferralPartner referralPartner) {
        this.referralPartner = referralPartner;
    }

}
