package com.universalinvents.udccs.retailers;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 12/13/17.
 */
@Entity
@Table(name = "retailer",
       uniqueConstraints = {@UniqueConstraint(name = "uk_retailer",
                                              columnNames = {"id"})})
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String regionCode;
    private Date createdOn;
    private Date modifiedOn;
    @Column(nullable = false)
    private Boolean generateCodes = Boolean.FALSE;
    private String logoUrl;
    private String redemptionUrl;
    private String status;
    private String externalId;

    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<Content> contents;

    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<ReferralPartner> referralPartners;

    public Retailer() {
    }

    public Retailer(String name, String regionCode, String status, Boolean generateCodes, String logoUrl,
                    String redemptionUrl, String externalId) {
        this.name = name;
        this.regionCode = regionCode;
        this.createdOn = new Date();
        this.status = status;
        this.generateCodes = generateCodes;
        this.logoUrl = logoUrl;
        this.redemptionUrl = redemptionUrl;
        this.externalId = externalId;
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

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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

    public Boolean getGenerateCodes() {
        return generateCodes;
    }

    public void setGenerateCodes(Boolean generateCodes) {
        this.generateCodes = generateCodes;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRedemptionUrl() {
        return redemptionUrl;
    }

    public void setRedemptionUrl(String redemptionUrl) {
        this.redemptionUrl = redemptionUrl;
    }

    public Set<Content> getContents() {
        return contents;
    }

    public void setContents(Set<Content> contents) {
        this.contents = contents;
    }

    public Set<ReferralPartner> getReferralPartners() {
        return referralPartners;
    }

    public void setReferralPartners(Set<ReferralPartner> referralPartners) {
        this.referralPartners = referralPartners;
    }

    public String getExternalId() { return externalId; }

    public void setExternalId(String externalId) { this.externalId = externalId; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Retailer retailer = (Retailer) o;
        return Objects.equals(id, retailer.id) && Objects.equals(name, retailer.name) && Objects.equals(regionCode,
                                                                                                        retailer.regionCode) && Objects
                .equals(createdOn, retailer.createdOn) && Objects.equals(modifiedOn,
                                                                         retailer.modifiedOn) && Objects.equals(
                generateCodes, retailer.generateCodes) && Objects.equals(logoUrl, retailer.logoUrl) && Objects.equals(
                redemptionUrl, retailer.redemptionUrl) && Objects.equals(status, retailer.status) && Objects.equals(
                contents, retailer.contents) && Objects.equals(referralPartners, retailer.referralPartners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regionCode, createdOn, modifiedOn, generateCodes, logoUrl, redemptionUrl, status,
                            contents, referralPartners);
    }

    @Override
    public String toString() {
        return "Retailer{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", regionCode='" + regionCode + '\'' +
                ", createdOn=" + createdOn + ", status=" + status + ", generateCodes=" + generateCodes +
                ", logoUrl=" + logoUrl + ", redemptionUrl=" + redemptionUrl +
                ", contents=" + contents + ", referralPartners=" + referralPartners + '}';
    }
}
