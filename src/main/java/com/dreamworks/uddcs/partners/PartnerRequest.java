package com.dreamworks.uddcs.partners;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
public class PartnerRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;
    private String description;
    private String contactName;
    private String email;
    private String phone;

    public PartnerRequest() {}

    public PartnerRequest(String name, String description, String contactName, String email, String phone)
    {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
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
}
