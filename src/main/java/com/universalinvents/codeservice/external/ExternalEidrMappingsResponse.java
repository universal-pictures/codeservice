package com.universalinvents.codeservice.external;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalEidrMappingsResponse implements Serializable {

    private Long count;
    private Long minInventoryCount;

    public ExternalEidrMappingsResponse() {}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getMinInventoryCount() {
        return minInventoryCount;
    }

    public void setMinInventoryCount(Long minInventoryCount) {
        this.minInventoryCount = minInventoryCount;
    }
}
