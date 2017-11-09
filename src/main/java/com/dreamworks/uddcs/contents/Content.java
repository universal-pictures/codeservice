package com.dreamworks.uddcs.contents;

import com.dreamworks.uddcs.retailers.Retailer;
import com.dreamworks.uddcs.studios.Studio;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
@Table(name = "content", uniqueConstraints = {@UniqueConstraint(name = "uk_content", columnNames = {"id"})})
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;
    
    @JsonProperty("eidr")
    private String eidr;
    
    @JsonProperty("eidrv")
    private String eidrv;

    @JsonProperty("gtm")
    private String gtm;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("msrp")
    private BigDecimal msrp;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "content_retailer",
               joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "retailer_id", referencedColumnName = "id"))
    @JsonProperty("retailers")
    private Set<Retailer> retailers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studioId")
    @JsonProperty("studio")
    private Studio studio;

    public Content() {
    }

    public Content(Long id, String title, String eidr, String gtm) {
        this.id = id;
        this.title = title;
        this.eidr = eidr;
        this.gtm = gtm;
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
}
