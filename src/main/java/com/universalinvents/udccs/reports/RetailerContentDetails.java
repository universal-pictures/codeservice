package com.universalinvents.udccs.reports;

import java.util.List;

public class RetailerContentDetails {

    private String name;
    private RetailerCodeDetails retailerCodes;
    private List<RetailerDetails> retailers;

    public RetailerContentDetails() {}

    public RetailerContentDetails(String name, RetailerCodeDetails retailerCodes, List<RetailerDetails> retailers) {
        this.name = name;
        this.retailerCodes = retailerCodes;
        this.retailers = retailers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RetailerCodeDetails getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(RetailerCodeDetails retailerCodes) {
        this.retailerCodes = retailerCodes;
    }

    public List<RetailerDetails> getRetailers() {
        return retailers;
    }

    public void setRetailers(List<RetailerDetails> retailers) {
        this.retailers = retailers;
    }
}
