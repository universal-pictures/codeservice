package com.dreamworks.udccs.retailers;

import com.dreamworks.udccs.contents.Content;
import com.dreamworks.udccs.partners.ReferralPartner;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
@Table(name = "retailer", uniqueConstraints = {@UniqueConstraint(name = "uk_retailer", columnNames = {"id"})})
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String regionCode;
    private Date createdOn;
    private String status;

    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<Content> contents;

    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<ReferralPartner> referralPartners;

    public Retailer() {
    }

    public Retailer(String name, String regionCode, Date createdOn, String status) {
        this.name = name;
        this.regionCode = regionCode;
        this.createdOn = createdOn;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Retailer retailer = (Retailer) o;

        if (!id.equals(retailer.id)) return false;
        if (name != null ? !name.equals(retailer.name) : retailer.name != null) return false;
        if (regionCode != null ? !regionCode.equals(retailer.regionCode) : retailer.regionCode != null) return false;
        if (status != null ? !status.equals(retailer.status) : retailer.status != null) return false;
        return createdOn != null ? createdOn.equals(retailer.createdOn) : retailer.createdOn == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (regionCode != null ? regionCode.hashCode() : 0);
        result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Retailer{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", regionCode='" + regionCode + '\'' + ", createdOn=" + createdOn + ", status=" + status + ", contents=" + contents + ", referralPartners=" + referralPartners + '}';
    }
}
