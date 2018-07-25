package com.universalinvents.udccs.apps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.udccs.codes.MasterCode;
import com.universalinvents.udccs.partners.ReferralPartner;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private String accessToken;
    private String status;

    @ManyToOne
    @JoinColumn(name = "partnerId")
    @JsonIgnoreProperties({"apps", "codes"})
    private ReferralPartner referralPartner;

    @OneToMany(mappedBy = "app")
    @JsonIgnore
    private List<MasterCode> masterCodes;

    public App() {
    }

    public App(Long id, String name, String description, String accessToken, String status,
               ReferralPartner referralPartner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdOn = new Date();
        this.accessToken = accessToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<MasterCode> getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(List<MasterCode> masterCodes) {
        this.masterCodes = masterCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return Objects.equals(id, app.id) &&
                Objects.equals(name, app.name) &&
                Objects.equals(description, app.description) &&
                Objects.equals(createdOn, app.createdOn) &&
                Objects.equals(modifiedOn, app.modifiedOn) &&
                Objects.equals(accessToken, app.accessToken) &&
                Objects.equals(status, app.status) &&
                Objects.equals(referralPartner, app.referralPartner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdOn, modifiedOn, accessToken, status, referralPartner);
    }
}
