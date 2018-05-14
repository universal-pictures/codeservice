package com.universalinvents.udccs.reports;

import java.util.List;

public class MasterStudioDetails {

    private String name;
    private MasterCodeDetails masterCodes;
    private List<PartnerContentDetails> content;

    public MasterStudioDetails() {}

    public MasterStudioDetails(String name, MasterCodeDetails masterCodes, List<PartnerContentDetails> content) {
        this.name = name;
        this.masterCodes = masterCodes;
        this.content = content;
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

    public List<PartnerContentDetails> getContent() {
        return content;
    }

    public void setContent(List<PartnerContentDetails> content) {
        this.content = content;
    }
}
