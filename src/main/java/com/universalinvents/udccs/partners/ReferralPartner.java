package com.universalinvents.udccs.partners;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
@Table(name = "referral_partner", uniqueConstraints = {@UniqueConstraint(name = "uk_partner", columnNames = {"id"})})
public class ReferralPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private Date createdOn;
    private Date modifiedOn;
    private String regionCode;
    private String status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "partner_retailer",
               joinColumns = @JoinColumn(name = "partner_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "retailer_id", referencedColumnName = "id"))
    private Set<Retailer> retailers;

    @OneToMany(mappedBy = "referralPartner")
    private List<App> apps;

    public ReferralPartner() {
    }

    public ReferralPartner(String name, String description, String contactName, String contactEmail,
                           String contactPhone, Date createdOn, Date modifiedOn, String regionCode, String status) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.regionCode = regionCode;
        this.status = status;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Set<Retailer> getRetailers() {
        return retailers;
    }

    public void setRetailers(Set<Retailer> retailers) {
        this.retailers = retailers;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }
}
