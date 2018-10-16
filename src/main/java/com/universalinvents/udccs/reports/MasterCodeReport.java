package com.universalinvents.udccs.reports;

import java.util.List;

public class MasterCodeReport {

    private MasterCodeDetails masterCodes;
    private List<MasterStudioDetails> studios;

    public MasterCodeReport() {}

    public MasterCodeReport(MasterCodeDetails masterCodes, List<MasterStudioDetails> studios) {
        this.masterCodes = masterCodes;
        this.studios = studios;
    }

    public MasterCodeDetails getMasterCodes() {
        return masterCodes;
    }

    public void setMasterCodes(MasterCodeDetails masterCodes) {
        this.masterCodes = masterCodes;
    }

    public List<MasterStudioDetails> getStudios() {
        return studios;
    }

    public void setStudios(List<MasterStudioDetails> studios) {
        this.studios = studios;
    }
}
