package com.universalinvents.udccs.codes;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by mmonti on 3/20/17.
 * Updated by kkirkland on 10/25/2017.
 */
@Entity
@Table(name = "retailer_code")
public class RetailerCode {

    @Id
    private String code;

    private Date createdOn;
    private String format;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "retailerId")
    private Retailer retailer;

    @OneToMany(mappedBy = "id.retailerCode")
    private List<Pairing> pairings;

    public RetailerCode() {}

    /**
     *
     * @param content
     * @param retailer
     * @param code
     */
    public RetailerCode(final String code, final Content content, final String format, final Retailer retailer)
    {
        this.code = code;
        this.content = content;
        this.format = format;
        this.retailer = retailer;
        this.createdOn = new Date();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Pairing> getPairings() {
        return pairings;
    }

    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }

    public boolean isPaired() {
        return pairings != null && pairings.size() > 0;
    }
}
