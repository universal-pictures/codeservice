package com.dreamworks.uddcs.reports;

import com.dreamworks.uddcs.codes.StudioCodeRepository;
import com.dreamworks.uddcs.contents.ContentRepository;
import com.dreamworks.uddcs.partners.PartnerRepository;
import com.dreamworks.uddcs.retailers.RetailerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController
{
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private StudioCodeRepository studioCodeRepository;

    @CrossOrigin
    @ApiOperation("Get a summarized report of UDDCS stats")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<SummaryReport> getSummaryReport()
    {
        SummaryReport report = new SummaryReport();
        report.setPartners(partnerRepository.count());
        report.setRetailers(retailerRepository.count());
        report.setContents(contentRepository.count());

        long totalCodes = studioCodeRepository.count();
        report.setStudioCodes(totalCodes);
        report.setStudioCodesPaired(totalCodes - studioCodeRepository.countByPairedBy(null));
        report.setStudioCodesRedeemed(totalCodes - studioCodeRepository.countByRedeemedBy(null));

        return new ResponseEntity<SummaryReport>(report, HttpStatus.OK);
    }
}
