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
import com.universalinvents.codeservice.retailers.Retailer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "retailer_code")
public class RetailerCode {

    // Legal status values
    public enum Status {
        PAIRED, REDEEMED, EXPIRED, ZOMBIED
    }

    @Id
    private String code;

    private Date createdOn;
    private Date modifiedOn;
    private Date expiresOn;
    private String format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('PAIRED', 'REDEEMED', 'EXPIRED', 'ZOMBIED')")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "retailerId")
    private Retailer retailer;

    @OneToOne(mappedBy = "retailerCode")
    @JsonIgnoreProperties(value = {"retailerCode"})
    private Pairing pairing;

    public RetailerCode() {
    }

    /**
     * @param content
     * @param retailer
     * @param code
     */
    public RetailerCode(final String code, final Content content, final String format, final Status status,
                        final Retailer retailer, final Date expiresOn) {
        this.code = code;
        this.content = content;
        this.format = format;
        this.retailer = retailer;
        this.status = status;
        this.createdOn = new Date();
        this.modifiedOn = createdOn; // Use the same date for new objects
        this.expiresOn = expiresOn;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getExpiresOn() { return expiresOn; }

    public void setExpiresOn(Date expiresOn) { this.expiresOn = expiresOn; }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
