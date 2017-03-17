package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.partners.Partner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
@Table(name = "studio_code", uniqueConstraints = {
        @UniqueConstraint(name = "uk_studio_code", columnNames = {"code"})
})
public class StudioCode
{
    @Id
    private String code;
    private String createdBy;
    private Date createdOn;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partnerId")
    private Partner partner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contentId")
    private Content content;

    private String retailerCode;
    private String retailerId;
    private String pairedBy;
    private Date pairedOn;

    private String redeemedBy;
    private Date redeemedOn;

    public StudioCode() {}

    public StudioCode(String code, String createdBy, Date createdOn, Partner partner, Content content)
    {
        this.code = code;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.partner = partner;
        this.content = content;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Partner getPartner()
    {
        return partner;
    }

    public void setPartner(Partner partner)
    {
        this.partner = partner;
    }

    public Content getContent()
    {
        return content;
    }

    public void setContent(Content content)
    {
        this.content = content;
    }

    public String getRetailerCode()
    {
        return retailerCode;
    }

    public void setRetailerCode(String retailerCode)
    {
        this.retailerCode = retailerCode;
    }

    public String getRetailerId()
    {
        return retailerId;
    }

    public void setRetailerId(String retailerId)
    {
        this.retailerId = retailerId;
    }

    public String getPairedBy()
    {
        return pairedBy;
    }

    public void setPairedBy(String pairedBy)
    {
        this.pairedBy = pairedBy;
    }

    public Date getPairedOn()
    {
        return pairedOn;
    }

    public void setPairedOn(Date pairedOn)
    {
        this.pairedOn = pairedOn;
    }

    public boolean isPaired()
    {
        return pairedOn != null;
    }

    public String getRedeemedBy()
    {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy)
    {
        this.redeemedBy = redeemedBy;
    }

    public Date getRedeemedOn()
    {
        return redeemedOn;
    }

    public void setRedeemedOn(Date redeemedOn)
    {
        this.redeemedOn = redeemedOn;
    }

    public boolean isRedeemed()
    {
        return redeemedOn != null;
    }
}
