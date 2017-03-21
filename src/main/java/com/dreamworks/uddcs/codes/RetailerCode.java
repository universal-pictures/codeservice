package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mmonti on 3/20/17.
 */
@Entity
@Table(name = "retailer_code", uniqueConstraints = {
        @UniqueConstraint(name = "uk_retailer_code", columnNames = {"code"})
})
public class RetailerCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdOn;
    private Date pairedOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contentId")
    private Content content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "retailerId")
    private Retailer retailer;

    private String code;

    public RetailerCode() {
    }

    /**
     *
     * @param content
     * @param retailer
     * @param code
     */
    public RetailerCode(final Content content, final Retailer retailer, final String code) {
        this.content = content;
        this.retailer = retailer;
        this.code = code;
        this.createdOn = new Date();
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isPaired() {
        return this.getPairedOn() != null;
    }
}
