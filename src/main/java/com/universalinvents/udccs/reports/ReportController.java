package com.universalinvents.udccs.reports;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.codes.MasterCode;
import com.universalinvents.udccs.codes.MasterCodeRepository;
import com.universalinvents.udccs.codes.RetailerCode;
import com.universalinvents.udccs.codes.RetailerCodeRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(tags = {"Report Controller"},
     description = "Basic Reporting operations")
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private StudioRepository studioRepository;

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

    @Autowired
    private AppRepository appRepository;

    @CrossOrigin
    @ApiOperation(value = "Get a summarized report of objects")
    @RequestMapping(method = RequestMethod.GET,
                    produces = "application/json")
    public ResponseEntity<SummaryReport> getSummaryReport() {
        SummaryReport report = new SummaryReport();
        report.setStudios(studioRepository.count());
        report.setPartners(referralPartnerRepository.count());
        report.setRetailers(retailerRepository.count());
        report.setContents(contentRepository.count());

        return new ResponseEntity<SummaryReport>(report, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Get a summarized report of code stats")
    @RequestMapping(method = RequestMethod.GET,
                    value = "/codes",
                    produces = "application/json")
    public ResponseEntity<CodeSummaryReport> getCodeSummaryReport() {
        MasterCodeDetails masterDetails = new MasterCodeDetails();
        masterDetails.setIssued(masterCodeRepository.countByStatus(MasterCode.Status.ISSUED));
        masterDetails.setPaired(masterCodeRepository.countByStatus(MasterCode.Status.PAIRED));
        masterDetails.setRedeemed(masterCodeRepository.countByStatus(MasterCode.Status.REDEEMED));
        masterDetails.setUnallocated(masterCodeRepository.countByStatus(MasterCode.Status.UNALLOCATED));

        RetailerCodeDetails retailerDetails = new RetailerCodeDetails();
        retailerDetails.setPaired(retailerCodeRepository.countByStatus(RetailerCode.Status.PAIRED));
        retailerDetails.setRedeemed(retailerCodeRepository.countByStatus(RetailerCode.Status.REDEEMED));
        retailerDetails.setExpired(retailerCodeRepository.countByStatus(RetailerCode.Status.EXPIRED));

        CodeSummaryReport report = new CodeSummaryReport();
        report.setMasterCodes(masterDetails);
        report.setRetailerCodes(retailerDetails);

        return new ResponseEntity<CodeSummaryReport>(report, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Get a report of Master Code stats")
    @RequestMapping(method = RequestMethod.GET,
                    value = "/codes/master",
                    produces = "application/json")
    public ResponseEntity<MasterCodeReport> getMasterCodeReport() {

        // Start from the top and populate the report in this order:
        // totals, studios, content, partners, apps
        MasterCodeReport masterCodeReport = new MasterCodeReport();

        // ============= TOTALS ==============
        MasterCodeDetails masterTotalDetails = new MasterCodeDetails();
        masterTotalDetails.setUnallocated(masterCodeRepository.countByStatus(MasterCode.Status.UNALLOCATED));
        masterTotalDetails.setIssued(masterCodeRepository.countByStatus(MasterCode.Status.ISSUED));
        masterTotalDetails.setPaired(masterCodeRepository.countByStatus(MasterCode.Status.PAIRED));
        masterTotalDetails.setRedeemed(masterCodeRepository.countByStatus(MasterCode.Status.REDEEMED));
        masterCodeReport.setMasterCodes(masterTotalDetails);


        // ============= STUDIOS =============
        List<MasterStudioDetails> studioDetails = new ArrayList<MasterStudioDetails>();
        List<Studio> studios = studioRepository.findAll();
        for (Studio studio : studios) {
            // Get all the content for this studio
            List<Content> contents = contentRepository.findByStudio(studio);

            MasterCodeDetails masterStudioDetails = new MasterCodeDetails();
            masterStudioDetails.setUnallocated(
                    masterCodeRepository.countByContentInAndStatus(contents, MasterCode.Status.UNALLOCATED));
            masterStudioDetails.setIssued(
                    masterCodeRepository.countByContentInAndStatus(contents, MasterCode.Status.ISSUED));
            masterStudioDetails.setPaired(
                    masterCodeRepository.countByContentInAndStatus(contents, MasterCode.Status.PAIRED));
            masterStudioDetails.setRedeemed(
                    masterCodeRepository.countByContentInAndStatus(contents, MasterCode.Status.REDEEMED));


            // ============= CONTENT =============
            List<PartnerContentDetails> partnerContentDetails = new ArrayList<>();
            for (Content content : contents) {

                MasterCodeDetails masterContentDetails = new MasterCodeDetails();
                masterContentDetails.setUnallocated(
                        masterCodeRepository.countByContentAndStatus(content, MasterCode.Status.UNALLOCATED));
                masterContentDetails.setIssued(
                        masterCodeRepository.countByContentAndStatus(content, MasterCode.Status.ISSUED));
                masterContentDetails.setPaired(
                        masterCodeRepository.countByContentAndStatus(content, MasterCode.Status.PAIRED));
                masterContentDetails.setRedeemed(
                        masterCodeRepository.countByContentAndStatus(content, MasterCode.Status.REDEEMED));

                // ============= PARTNERS ============
                List<PartnerDetails> partnerDetails = new ArrayList<>();
                // we don't have a direct relationship from content to partners
                // so first get all of the master codes related to the content then
                // find partners for the found codes.
                List<MasterCode> contentCodes = masterCodeRepository.findByContent(content);
                List<ReferralPartner> partners = referralPartnerRepository.findDistinctByCodesIn(contentCodes);
                for (ReferralPartner partner : partners) {

                    MasterCodeDetails masterPartnerDetails = new MasterCodeDetails();
                    masterPartnerDetails.setUnallocated(
                            masterCodeRepository.countByContentAndReferralPartnerAndStatus(content, partner,
                                                                                           MasterCode.Status
                                                                                                   .UNALLOCATED));
                    masterPartnerDetails.setIssued(
                            masterCodeRepository.countByContentAndReferralPartnerAndStatus(content, partner,
                                                                                           MasterCode.Status.ISSUED));
                    masterPartnerDetails.setPaired(
                            masterCodeRepository.countByContentAndReferralPartnerAndStatus(content, partner,
                                                                                           MasterCode.Status.PAIRED));
                    masterPartnerDetails.setRedeemed(
                            masterCodeRepository.countByContentAndReferralPartnerAndStatus(content, partner,
                                                                                           MasterCode.Status.REDEEMED));


                    // ============= APPS ================
                    // Loop through all available apps and get master code counts for each
                    List<AppDetails> appDetails = new ArrayList<AppDetails>();
                    List<App> apps = appRepository.findByReferralPartner(partner);
                    for (App app : apps) {
                        MasterCodeDetails masterAppDetails = new MasterCodeDetails();
                        masterAppDetails.setIssued(
                                masterCodeRepository.countByContentAndReferralPartnerAndAppAndStatus(content, partner,
                                                                                                     app,
                                                                                                     MasterCode
                                                                                                             .Status
                                                                                                             .ISSUED));
                        masterAppDetails.setPaired(
                                masterCodeRepository.countByContentAndReferralPartnerAndAppAndStatus(content, partner,
                                                                                                     app,
                                                                                                     MasterCode
                                                                                                             .Status
                                                                                                             .PAIRED));
                        masterAppDetails.setRedeemed(
                                masterCodeRepository.countByContentAndReferralPartnerAndAppAndStatus(content, partner,
                                                                                                     app,
                                                                                                     MasterCode
                                                                                                             .Status
                                                                                                             .REDEEMED));
                        masterAppDetails.setUnallocated(
                                masterCodeRepository.countByContentAndReferralPartnerAndAppAndStatus(content, partner,
                                                                                                     app,
                                                                                                     MasterCode
                                                                                                             .Status
                                                                                                             .UNALLOCATED));
                        appDetails.add(new AppDetails(app.getName(), masterAppDetails));
                    }
                    partnerDetails.add(new PartnerDetails(partner.getName(), masterPartnerDetails, appDetails));
                }
                partnerContentDetails.add(
                        new PartnerContentDetails(content.getTitle(), masterContentDetails, partnerDetails));
            }
            studioDetails.add(new MasterStudioDetails(studio.getName(), masterStudioDetails, partnerContentDetails));
        }
        masterCodeReport.setStudios(studioDetails);

        return new ResponseEntity<MasterCodeReport>(masterCodeReport, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Get a report of Retailer Code stats")
    @RequestMapping(method = RequestMethod.GET,
                    value = "/codes/retailer",
                    produces = "application/json")
    public ResponseEntity<RetailerCodeReport> getRetailerCodeReport() {

        // Start from the top and populate the report in this order:
        // totals, studios, content, retailers, formats
        RetailerCodeReport retailerCodeReport = new RetailerCodeReport();

        // ============= TOTALS ==============
        RetailerCodeDetails retailerTotalDetails = new RetailerCodeDetails();
        retailerTotalDetails.setPaired(retailerCodeRepository.countByStatus(RetailerCode.Status.PAIRED));
        retailerTotalDetails.setRedeemed(retailerCodeRepository.countByStatus(RetailerCode.Status.REDEEMED));
        retailerTotalDetails.setExpired(retailerCodeRepository.countByStatus(RetailerCode.Status.EXPIRED));
        retailerCodeReport.setRetailerCodes(retailerTotalDetails);


        // ============= STUDIOS =============
        List<RetailerStudioDetails> studioDetails = new ArrayList<RetailerStudioDetails>();
        List<Studio> studios = studioRepository.findAll();
        for (Studio studio : studios) {
            // Get all the content for this studio
            List<Content> contents = contentRepository.findByStudio(studio);

            RetailerCodeDetails retailerStudioDetails = new RetailerCodeDetails();
            retailerStudioDetails.setPaired(
                    retailerCodeRepository.countByContentInAndStatus(contents, RetailerCode.Status.PAIRED));
            retailerStudioDetails.setRedeemed(
                    retailerCodeRepository.countByContentInAndStatus(contents, RetailerCode.Status.REDEEMED));
            retailerStudioDetails.setExpired(
                    retailerCodeRepository.countByContentInAndStatus(contents, RetailerCode.Status.EXPIRED));


            // ============= CONTENT =============
            List<RetailerContentDetails> contentDetails = new ArrayList<>();
            for (Content content : contents) {

                RetailerCodeDetails retailerContentDetails = new RetailerCodeDetails();
                retailerContentDetails.setPaired(
                        retailerCodeRepository.countByContentAndStatus(content, RetailerCode.Status.PAIRED));
                retailerContentDetails.setRedeemed(
                        retailerCodeRepository.countByContentAndStatus(content, RetailerCode.Status.REDEEMED));
                retailerContentDetails.setExpired(
                        retailerCodeRepository.countByContentAndStatus(content, RetailerCode.Status.EXPIRED));

                // ============= RETAILERS ============
                List<RetailerDetails> retailerDetails = new ArrayList<>();
                List<Retailer> retailers = retailerRepository.findByContentsIn(Collections.singletonList(content));
                for (Retailer retailer : retailers) {

                    RetailerCodeDetails retailerRetailerDetails = new RetailerCodeDetails();
                    retailerRetailerDetails.setPaired(
                            retailerCodeRepository.countByContentAndRetailerAndStatus(content, retailer,
                                                                                      RetailerCode.Status.PAIRED));
                    retailerRetailerDetails.setRedeemed(
                            retailerCodeRepository.countByContentAndRetailerAndStatus(content, retailer,
                                                                                      RetailerCode.Status.REDEEMED));
                    retailerRetailerDetails.setExpired(
                            retailerCodeRepository.countByContentAndRetailerAndStatus(content, retailer,
                                    RetailerCode.Status.EXPIRED));


                    // ============= FORMATS ================
                    List<FormatDetails> formatDetails = new ArrayList<FormatDetails>();
                    List<String> formats = retailerCodeRepository.findDistinctFormatsByContentAndRetailer(content,
                                                                                                          retailer);
                    for (String format : formats) {
                        RetailerCodeDetails retailerFormatDetails = new RetailerCodeDetails();
                        retailerFormatDetails.setPaired(
                                retailerCodeRepository.countByContentAndRetailerAndFormatAndStatus(content, retailer,
                                                                                                   format,
                                                                                                   RetailerCode
                                                                                                           .Status
                                                                                                           .PAIRED));
                        retailerFormatDetails.setRedeemed(
                                retailerCodeRepository.countByContentAndRetailerAndFormatAndStatus(content, retailer,
                                                                                                   format,
                                                                                                   RetailerCode
                                                                                                           .Status
                                                                                                           .REDEEMED));
                        retailerFormatDetails.setExpired(
                                retailerCodeRepository.countByContentAndRetailerAndFormatAndStatus(content, retailer,
                                        format,
                                        RetailerCode
                                                .Status
                                                .EXPIRED));

                        formatDetails.add(new FormatDetails(format, retailerFormatDetails));
                    }
                    retailerDetails.add(
                            new RetailerDetails(retailer.getName(), retailerRetailerDetails, formatDetails));
                }
                contentDetails.add(
                        new RetailerContentDetails(content.getTitle(), retailerContentDetails, retailerDetails));
            }
            studioDetails.add(new RetailerStudioDetails(studio.getName(), retailerStudioDetails, contentDetails));
        }
        retailerCodeReport.setStudios(studioDetails);

        return new ResponseEntity<RetailerCodeReport>(retailerCodeReport, HttpStatus.OK);
    }
}
