package com.universalinvents.udccs.contents;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by dsherman on 2/27/17.
 * Updated by kkirkland on 11/7/17.
 */
@ApiModel("CreateContentRequest")
public class CreateContentRequest implements ContentRequest {

    @ApiModelProperty(value = "The movie title",
                      example = "Back to the Future",
                      required = true)
    private String title;
    @ApiModelProperty(value = "The EIDR value for this content",
                      example = "10.5240/09A3-1F6E-3538-DF46-5C6F-I",
                      required = true)
    private String eidr;
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
                      required = true)
    private Long studioId;

    public CreateContentRequest() {
    }

    public CreateContentRequest(String title, String eidr, String eidrv, String gtm, String status, BigDecimal msrp,
                                Long studioId) {
        this.title = title;
        this.eidr = eidr;
        this.eidrv = eidrv;
        this.gtm = gtm;
        this.status = status;
        this.msrp = msrp;
        this.studioId = studioId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEidr() {
        return eidr;
    }

    public void setEidr(String eidr) {
        this.eidr = eidr;
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
}
