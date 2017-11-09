package com.dreamworks.uddcs.studios;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kkirkland on 10/25/17.
 */
@Entity
@Table(name = "studio", uniqueConstraints = {@UniqueConstraint(name = "uk_studio", columnNames = {"id"})})
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("contactName")
    private String contactName;
    
    @JsonProperty("contactEmail")
    private String contactEmail;
    
    @JsonProperty("contactPhone")
    private String contactPhone;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("flags")
    private Long flags; // bitwise placeholder for potential configuration

    public Studio() {}

    public Studio(String name, String description, String contactName, String contactEmail, String contactPhone,
                  String status, Long flags) {
        this.name = name;
        this.description = description;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.status = status;
        this.flags = flags;
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
}
