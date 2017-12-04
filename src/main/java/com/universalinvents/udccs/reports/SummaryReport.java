package com.universalinvents.udccs.reports;

/**
 * Created by dsherman on 2/27/17.
 */
public class SummaryReport
{
    private long partners;
    private long retailers;
    private long contents;
    private long studioCodes;
    private long studioCodesPaired;
    private long studioCodesRedeemed;

    public SummaryReport() {}

    public SummaryReport(long partners, long retailers, long contents, long studioCodes, long studioCodesPaired, long studioCodesRedeemed)
    {
        this.partners = partners;
        this.retailers = retailers;
        this.contents = contents;
        this.studioCodes = studioCodes;
        this.studioCodesPaired = studioCodesPaired;
        this.studioCodesRedeemed = studioCodesRedeemed;
    }

    public long getPartners()
    {
        return partners;
    }

    public void setPartners(long partners)
    {
        this.partners = partners;
    }

    public long getRetailers()
    {
        return retailers;
    }

    public void setRetailers(long retailers)
    {
        this.retailers = retailers;
    }

    public long getContents()
    {
        return contents;
    }

    public void setContents(long contents)
    {
        this.contents = contents;
    }

    public long getStudioCodes()
    {
        return studioCodes;
    }

    public void setStudioCodes(long studioCodes)
    {
        this.studioCodes = studioCodes;
    }

    public long getStudioCodesPaired()
    {
        return studioCodesPaired;
    }

    public void setStudioCodesPaired(long studioCodesPaired)
    {
        this.studioCodesPaired = studioCodesPaired;
    }

    public long getStudioCodesRedeemed()
    {
        return studioCodesRedeemed;
    }

    public void setStudioCodesRedeemed(long studioCodesRedeemed)
    {
        this.studioCodesRedeemed = studioCodesRedeemed;
    }
}
