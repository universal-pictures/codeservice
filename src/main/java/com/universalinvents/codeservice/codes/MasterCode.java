/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.codes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.pairings.Pairing;
import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.partners.ReferralPartner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dsherman on 2/27/17.
 * Modified by kkirkland on 10/25/2017.
 */
@Entity
@Table(name = "master_code", uniqueConstraints = {@UniqueConstraint(name = "uk_master_code", columnNames = {"code"})})
public class MasterCode {

    // Legal status values
    public enum Status {
        ISSUED, PAIRED, REDEEMED, EXPIRED
    }

    @Id
    private String code;

    private String createdBy;
    private Date createdOn;
    private Date modifiedOn;
    private Date expiresOn;
    private String format;
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('ISSUED', 'PAIRED', 'REDEEMED', 'EXPIRED')")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "partnerId")
    @JsonIgnoreProperties(value = {"codes", "apps", "retailers", "studios"})
    private ReferralPartner referralPartner;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "appId")
    @JsonIgnoreProperties(value = {"masterCodes", "referralPartner"})
    private App app;

    @OneToOne(mappedBy = "masterCode")
    @JsonIgnoreProperties(value = {"masterCode"})
    private Pairing pairing;

    public MasterCode() {
    }

    public MasterCode(String code, String format, String createdBy, Date createdOn, Date expiresOn,
                      ReferralPartner referralPartner, App app, Content content, Status status, String externalId) {
        this.code = code;
        this.format = format;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.expiresOn = expiresOn;
        this.referralPartner = referralPartner;
        this.app = app;
        this.content = content;
        this.status = status;
        this.externalId = externalId;
        this.modifiedOn = createdOn; // Make the dates match for new objects
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
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

    public Pairing getPairing() {
        return pairing;
    }

    public void setPairing(Pairing pairing) {
        this.pairing = pairing;
    }

    public boolean isPaired() {
        return pairing != null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
//
//    public boolean isRedeemed() { return redeemedOn != null; }
}
