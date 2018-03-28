package com.universalinvents.udccs.reports;

import com.universalinvents.udccs.codes.MasterCode;
import com.universalinvents.udccs.codes.MasterCodeRepository;
import com.universalinvents.udccs.codes.RetailerCode;
import com.universalinvents.udccs.codes.RetailerCodeRepository;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.RetailerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Report Controller"},
     description = "Basic Reporting operations")
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

    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @CrossOrigin
    @ApiOperation(value = "Get a summarized report of code stats")
    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SummaryReport> getSummaryReport()
    {
        SummaryReport report = new SummaryReport();
        report.setPartners(referralPartnerRepository.count());
        report.setRetailers(retailerRepository.count());
        report.setContents(contentRepository.count());
        report.setMasterCodes(masterCodeRepository.count());
        report.setMasterCodesUnallocated(masterCodeRepository.countByStatus(MasterCode.Status.UNALLOCATED));
        report.setMasterCodesPaired(masterCodeRepository.countByStatus(MasterCode.Status.PAIRED));
        report.setMasterCodesRedeemed(masterCodeRepository.countByStatus(MasterCode.Status.REDEEMED));
        report.setRetailerCodes(retailerCodeRepository.count());
        report.setRetailerCodesUnallocated(retailerCodeRepository.countByStatus(RetailerCode.Status.UNALLOCATED));
        report.setRetailerCodesPaired(retailerCodeRepository.countByStatus(RetailerCode.Status.PAIRED));
        report.setRetailerCodesRedeemed(retailerCodeRepository.countByStatus(RetailerCode.Status.REDEEMED));

        return new ResponseEntity<SummaryReport>(report, HttpStatus.OK);
    }
}
