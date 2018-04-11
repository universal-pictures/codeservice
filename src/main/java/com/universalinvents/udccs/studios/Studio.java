package com.universalinvents.udccs.studios;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.partners.ReferralPartner;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by kkirkland on 10/25/17.
 */
@Entity
@Table(name = "studio", uniqueConstraints = {@UniqueConstraint(name = "uk_studio", columnNames = {"id"})})
public class Studio {
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

    @NotNull
    @Column(nullable = false)
    private String codePrefix;

    private String status;
    private Long flags; // bitwise placeholder for potential configuration

    @OneToMany(mappedBy = "studio")
    @JsonIgnoreProperties("studio")
    private List<Content> contents;

    @ManyToMany(mappedBy = "studios")
    @JsonBackReference
    private Set<ReferralPartner> referralPartners;

    public Studio() {}

    public Studio(String name, String description, String contactName, String contactEmail, String contactPhone,
                  String codePrefix, String status, Long flags) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.codePrefix = codePrefix;
        this.status = status;
        this.flags = flags;
        this.createdOn = new Date();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public String getCodePrefix() {
        return codePrefix;
    }

    public void setCodePrefix(String codePrefix) {
        this.codePrefix = codePrefix;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<ReferralPartner> getReferralPartners() {
        return referralPartners;
    }

    public void setReferralPartners(Set<ReferralPartner> referralPartners) {
        this.referralPartners = referralPartners;
    }
}
