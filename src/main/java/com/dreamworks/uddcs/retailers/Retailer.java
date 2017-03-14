package com.dreamworks.uddcs.retailers;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.partners.Partner;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
public class Retailer
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String regionCode;
    private Date createdOn;
    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<Content> contents;
    @ManyToMany(mappedBy = "retailers")
    @JsonBackReference
    private Set<Partner> partners;

    public Retailer() {}

    public Retailer(String name, String regionCode, Date createdOn)
    {
        this.name = name;
        this.regionCode = regionCode;
        this.createdOn = createdOn;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Set<Content> getContents()
    {
        return contents;
    }

    public void setContents(Set<Content> contents)
    {
        this.contents = contents;
    }

    public Set<Partner> getPartners()
    {
        return partners;
    }

    public void setPartners(Set<Partner> partners)
    {
        this.partners = partners;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Retailer retailer = (Retailer) o;

        if (!id.equals(retailer.id)) return false;
        if (name != null ? !name.equals(retailer.name) : retailer.name != null) return false;
        if (regionCode != null ? !regionCode.equals(retailer.regionCode) : retailer.regionCode != null) return false;
        return createdOn != null ? createdOn.equals(retailer.createdOn) : retailer.createdOn == null;
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (regionCode != null ? regionCode.hashCode() : 0);
        result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Retailer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", createdOn=" + createdOn +
                ", contents=" + contents +
                ", partners=" + partners +
                '}';
    }
}
