package com.universalinvents.codeservice.reports;

import java.util.List;

public class PartnerContentDetails {

    private String name;
    private MasterCodeDetails masterCodes;
    private List<PartnerDetails> partners;

    public PartnerContentDetails() {}

    public PartnerContentDetails(String name, MasterCodeDetails masterCodes, List<PartnerDetails> partners) {
        this.name = name;
        this.masterCodes = masterCodes;
        this.partners = partners;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MasterCodeDetails getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(MasterCodeDetails masterCodes) {
        this.masterCodes = masterCodes;
    }

    public List<PartnerDetails> getPartners() {
        return partners;
    }

    public void setPartners(List<PartnerDetails> partners) {
        this.partners = partners;
    }
}
