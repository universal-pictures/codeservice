package com.universalinvents.udccs.contents;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
@ApiModel("UpdateContentRequest")
public class UpdateContentRequest implements ContentRequest {

    @ApiModelProperty(value = "The movie title",
                      example = "Back to the Future",
                      required = false)
    private String title;
    @ApiModelProperty(value = "The EIDRV (version) value for this content",
                      example = "'20099'",
                      required = false)
    private String eidrv;
    @ApiModelProperty(value = "The GTM value for this content",
                      example = "'392'",
                      required = false)
    private String gtm;
    @ApiModelProperty(value = "ACTIVE or INACTIVE",
                      example = "ACTIVE",
                      required = false)
    private String status;
    @ApiModelProperty(value = "The MSRP value for this content",
                      example = "9.99",
                      required = false)
    private BigDecimal msrp;
    @ApiModelProperty(value = "The id of the related Studio (see [Studio Controller](#/Studio_Controller))",
                      example = "1",
                      required = false)
    private Long studioId;
    @ApiModelProperty(value = "A list of related Retailer id's",
                      example = "[1, 2]",
                      required = false)
    private Set<Long> retailerIds;

    public UpdateContentRequest() {
    }

    public UpdateContentRequest(String title, String eidrv, String gtm, String status, BigDecimal msrp,
                                Long studioId, Set<Long> retailerIds) {
        this.title = title;
        this.eidrv = eidrv;
        this.gtm = gtm;
        this.status = status;
        this.msrp = msrp;
        this.studioId = studioId;
        this.retailerIds = retailerIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEidrv() {
        return eidrv;
    }

    public void setEidrv(String eidrv) {
        this.eidrv = eidrv;
    }

    public String getGtm() {
        return gtm;
    }

    public void setGtm(String gtm) {
        this.gtm = gtm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public Set<Long> getRetailerIds() {
        return retailerIds;
    }

    public void setRetailerIds(Set<Long> retailerIds) {
        this.retailerIds = retailerIds;
    }
}
