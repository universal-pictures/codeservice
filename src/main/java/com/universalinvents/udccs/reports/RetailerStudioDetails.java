package com.universalinvents.udccs.reports;

import java.util.List;

public class RetailerStudioDetails {

    private String name;
    private RetailerCodeDetails retailerCodes;
    private List<RetailerContentDetails> content;

    public RetailerStudioDetails() {}

    public RetailerStudioDetails(String name, RetailerCodeDetails retailerCodes, List<RetailerContentDetails> content) {
        this.name = name;
        this.retailerCodes = retailerCodes;
        this.content = content;
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

    public List<RetailerContentDetails> getContent() {
        return content;
    }

    public void setContent(List<RetailerContentDetails> content) {
        this.content = content;
    }
}
