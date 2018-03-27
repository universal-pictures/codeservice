package com.universalinvents.udccs.reports;

/**
 * Created by dsherman on 2/27/17.
 */
public class SummaryReport {
    private long partners;
    private long retailers;
    private long contents;
    private long masterCodes;
    private long masterCodesUnallocated;
    private long masterCodesPaired;
    private long masterCodesRedeemed;
    private long retailerCodes;
    private long retailerCodesUnallocated;
    private long retailerCodesPaired;
    private long retailerCodesRedeemed;

    public SummaryReport() {}

    public SummaryReport(long partners, long retailers, long contents, long masterCodes, long masterCodesUnallocated,
                         long masterCodesPaired, long masterCodesRedeemed, long retailerCodes,
                         long retailerCodesUnallocated, long retailerCodesPaired, long retailerCodesRedeemed) {
        this.partners = partners;
        this.retailers = retailers;
        this.contents = contents;
        this.masterCodes = masterCodes;
        this.masterCodesUnallocated = masterCodesUnallocated;
        this.masterCodesPaired = masterCodesPaired;
        this.masterCodesRedeemed = masterCodesRedeemed;
        this.retailerCodes = retailerCodes;
        this.retailerCodesUnallocated = retailerCodesUnallocated;
        this.retailerCodesPaired = retailerCodesPaired;
        this.retailerCodesRedeemed = retailerCodesRedeemed;
    }

    public long getPartners() {
        return partners;
    }

    public void setPartners(long partners) {
        this.partners = partners;
    }

    public long getRetailers() {
        return retailers;
    }

    public void setRetailers(long retailers) {
        this.retailers = retailers;
    }

    public long getContents() {
        return contents;
    }

    public void setContents(long contents) {
        this.contents = contents;
    }

    public long getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(long masterCodes) {
        this.masterCodes = masterCodes;
    }

    public long getMasterCodesUnallocated() {
        return masterCodesUnallocated;
    }

    public void setMasterCodesUnallocated(long masterCodesUnallocated) {
        this.masterCodesUnallocated = masterCodesUnallocated;
    }

    public long getMasterCodesPaired() {
        return masterCodesPaired;
    }

    public void setMasterCodesPaired(long masterCodesPaired) {
        this.masterCodesPaired = masterCodesPaired;
    }

    public long getMasterCodesRedeemed() {
        return masterCodesRedeemed;
    }

    public void setMasterCodesRedeemed(long masterCodesRedeemed) {
        this.masterCodesRedeemed = masterCodesRedeemed;
    }

    public long getRetailerCodes() {
        return retailerCodes;
    }

    public void setRetailerCodes(long retailerCodes) {
        this.retailerCodes = retailerCodes;
    }

    public long getRetailerCodesUnallocated() {
        return retailerCodesUnallocated;
    }

    public void setRetailerCodesUnallocated(long retailerCodesUnallocated) {
        this.retailerCodesUnallocated = retailerCodesUnallocated;
    }

    public long getRetailerCodesPaired() {
        return retailerCodesPaired;
    }

    public void setRetailerCodesPaired(long retailerCodesPaired) {
        this.retailerCodesPaired = retailerCodesPaired;
    }

    public long getRetailerCodesRedeemed() {
        return retailerCodesRedeemed;
    }

    public void setRetailerCodesRedeemed(long retailerCodesRedeemed) {
        this.retailerCodesRedeemed = retailerCodesRedeemed;
    }
}
