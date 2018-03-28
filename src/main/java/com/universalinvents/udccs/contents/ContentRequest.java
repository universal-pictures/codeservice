package com.universalinvents.udccs.contents;

import java.math.BigDecimal;
import java.util.Set;

public interface ContentRequest {

    String getTitle();
    void setTitle(String title);

    String getEidrv();
    void setEidrv(String eidrv);

    String getGtm();
    void setGtm(String gtm);

    String getStatus();
    void setStatus(String status);

    BigDecimal getMsrp();
    void setMsrp(BigDecimal msrp);

    Long getStudioId();
    void setStudioId(Long studioId);

    Set<Long> getRetailerIds();
    void setRetailerIds(Set<Long> retailerIds);
}
