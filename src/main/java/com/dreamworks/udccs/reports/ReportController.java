package com.dreamworks.udccs.reports;

import com.dreamworks.udccs.codes.MasterCodeRepository;
import com.dreamworks.udccs.contents.ContentRepository;
import com.dreamworks.udccs.partners.ReferralPartnerRepository;
import com.dreamworks.udccs.retailers.RetailerRepository;
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
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @CrossOrigin
    @ApiOperation("Get a summarized report of UDCCS stats")
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SummaryReport> getSummaryReport()
    {
        SummaryReport report = new SummaryReport();
        report.setPartners(referralPartnerRepository.count());
        report.setRetailers(retailerRepository.count());
        report.setContents(contentRepository.count());

        long totalCodes = masterCodeRepository.count();
        report.setStudioCodes(totalCodes);
//        report.setStudioCodesPaired(totalCodes - masterCodeRepository.countByPairedBy(null));
//        report.setStudioCodesRedeemed(totalCodes - masterCodeRepository.countByRedeemedBy(null));

        return new ResponseEntity<SummaryReport>(report, HttpStatus.OK);
    }
}
