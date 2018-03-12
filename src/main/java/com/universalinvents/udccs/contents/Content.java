package com.universalinvents.udccs.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.studios.Studio;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
@Table(name = "content", uniqueConstraints = {@UniqueConstraint(name = "uk_content", columnNames = {"id"})})
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String eidr;
    private String eidrv;
    private String gtm;
    private String status;
    private BigDecimal msrp;
    private Date createdOn;
    private Date modifiedOn;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "content_retailer",
               joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "retailer_id", referencedColumnName = "id"))
    private Set<Retailer> retailers;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studioId")
    @JsonIgnoreProperties("contents")
    private Studio studio;

    public Content() {
    }

    public Content(Long id, String title, String eidr, String gtm, Studio studio) {
        this.id = id;
        this.title = title;
        this.eidr = eidr;
        this.gtm = gtm;
        this.studio = studio;
        this.createdOn = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEidr() {
        return eidr;
    }

    public void setEidr(String eidr) {
        this.eidr = eidr;
    }

    public String getGtm() {
        return gtm;
    }

    public void setGtm(String gtm) {
        this.gtm = gtm;
    }

    public Set<Retailer> getRetailers() {
        return retailers;
    }

    public void setRetailers(Set<Retailer> retailers) {
        this.retailers = retailers;
    }

    public String getEidrv() {
        return eidrv;
    }

    public void setEidrv(String eidrv) {
        this.eidrv = eidrv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
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

    public String toString() {
        StringBuffer sb = new StringBuffer("Content Object:");
        sb.append("\n\tId: ").append(getId());
        sb.append("\n\tTitle: ").append(getTitle());
        sb.append("\n\tEidr: ").append(getEidr());
        sb.append("\n\tEidrv: ").append(getEidrv());
        sb.append("\n\tGtm: ").append(getGtm());
        sb.append("\n\tStudio: ");
        if (getStudio() == null) {
            sb.append("null");
        } else {
            sb.append(getStudio().getName());
        }
        sb.append("\n\tMSRP: ").append(getMsrp());
        sb.append("\n\tCreated On: ").append(getCreatedOn());
        sb.append("\n\tModified On: ").append(getModifiedOn());
        sb.append("\n\tStatus: ").append(getStatus());
        sb.append("\n\tRetailers: ");
        if (retailers == null) {
            sb.append("\n\t\t- null");
        } else {
            for (Retailer retailer : retailers) {
                sb.append("\n\t\t- ").append(retailer.getName());
            }
        }

        return sb.toString();
    }
}
