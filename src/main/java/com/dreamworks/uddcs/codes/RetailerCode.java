package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mmonti on 3/20/17.
 */
@Entity
@Table(name = "retailer_code")

public class RetailerCode {

    @Id
    private String code;

    private Date createdOn;
    private Date pairedOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contentId")
    private Content content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "retailerId")
    private Retailer retailer;

    public RetailerCode() {}

    /**
     *
     * @param content
     * @param retailer
     * @param code
     */
    public RetailerCode(final String code, final Content content, final Retailer retailer)
    {
        this.code = code;
        this.content = content;
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

    public Date getPairedOn() {
        return pairedOn;
    }

    public void setPairedOn(Date pairedOn) {
        this.pairedOn = pairedOn;
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

    public boolean isPaired() {
        return this.getPairedOn() != null;
    }
}
