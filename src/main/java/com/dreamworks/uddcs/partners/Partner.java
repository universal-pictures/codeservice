package com.dreamworks.uddcs.partners;

import com.dreamworks.uddcs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
public class Partner
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String description;
    private String contactName;
    private String email;
    private String phone;
    private Date createdOn;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "parnter_retailer", joinColumns = @JoinColumn(name = "partner_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "retailer_id", referencedColumnName = "id"))
    private Set<Retailer> retailers;


    public Partner() {}

    public Partner(String name, String description, String contactName, String email, String phone, Date createdOn)
    {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
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

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Set<Retailer> getRetailers()
    {
        return retailers;
    }

    public void setRetailers(Set<Retailer> retailers)
    {
        this.retailers = retailers;
    }
}
