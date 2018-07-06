package com.universalinvents.udccs.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRetailerCodeResponse {

    private String code;
    private Date expiresOn;
    private String status;

    public ExternalRetailerCodeResponse() {}

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Date getExpiresOn() { return expiresOn; }

    public void setExpiresOn(Date expiresOn) { this.expiresOn = expiresOn; }

    public String getStatus() { return status; }

    public void setStatus() { this.status = status; }
}
