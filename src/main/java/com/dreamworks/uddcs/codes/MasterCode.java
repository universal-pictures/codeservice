package com.dreamworks.uddcs.codes;

import com.dreamworks.uddcs.apps.App;
import com.dreamworks.uddcs.contents.Content;
import com.dreamworks.uddcs.pairings.Pairing;
import com.dreamworks.uddcs.partners.ReferralPartner;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dsherman on 2/27/17.
 * Modified by kkirkland on 10/25/2017.
 */
@Entity
@Table(name = "master_code", uniqueConstraints = {@UniqueConstraint(name = "uk_master_code", columnNames = {"code"})})
public class MasterCode {
    @Id
    private String code;

    private String createdBy;
    private Date createdOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partnerId")
    private ReferralPartner referralPartner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contentId")
    private Content content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appId")
    private App app;

    @OneToMany(mappedBy = "masterCode")
    private List<Pairing> pairings;

    public MasterCode() {
    }

    public MasterCode(String code, String createdBy, Date createdOn, ReferralPartner referralPartner, Content content) {
        this.code = code;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.referralPartner = referralPartner;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ReferralPartner getReferralPartner() {
        return referralPartner;
    }

    public void setReferralPartner(ReferralPartner referralPartner) {
        this.referralPartner = referralPartner;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
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
//
//    public boolean isRedeemed() { return redeemedOn != null; }
}
