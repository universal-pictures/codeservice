package com.universalinvents.udccs.reports;

import java.util.List;

public class RetailerCodeReport {

    private RetailerCodeDetails retailerCodes;
    private List<RetailerStudioDetails> studios;

    public RetailerCodeReport() {}

    public RetailerCodeReport(RetailerCodeDetails retailerCodes, List<RetailerStudioDetails> studios) {
        this.retailerCodes = retailerCodes;
        this.studios = studios;
    }

    public RetailerCodeDetails getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(RetailerCodeDetails retailerCodes) {
        this.retailerCodes = retailerCodes;
    }

    public List<RetailerStudioDetails> getStudios() {
        return studios;
    }

    public void setStudios(List<RetailerStudioDetails> studios) {
        this.studios = studios;
    }
}
