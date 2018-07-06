package com.universalinvents.udccs.external;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRetailerCodeStatusResponse {

    private String status;

    public ExternalRetailerCodeStatusResponse() {}

    public String getStatus() { return status; }

    public void setStatus() { this.status = status; }
}
