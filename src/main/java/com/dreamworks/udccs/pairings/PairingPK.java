package com.dreamworks.udccs.pairings;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PairingPK implements Serializable {

    private String masterCode;
    private String retailerCode;

    public PairingPK() {}

    public PairingPK(String masterCode, String retailerCode) {
        this.masterCode = masterCode;
        this.retailerCode = retailerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PairingPK pairingPK = (PairingPK) o;

        if (!masterCode.equals(pairingPK.masterCode)) return false;
        return retailerCode.equals(pairingPK.retailerCode);
    }

    @Override
    public int hashCode() {
        int result = masterCode.hashCode();
        result = 31 * result + retailerCode.hashCode();
        return result;
    }
}
