package com.universalinvents.codeservice.reports;

import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.apps.AppRepository;
import com.universalinvents.codeservice.codes.MasterCode;
import com.universalinvents.codeservice.codes.MasterCodeRepository;
import com.universalinvents.codeservice.codes.RetailerCode;
import com.universalinvents.codeservice.codes.RetailerCodeRepository;
import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.contents.ContentRepository;
import com.universalinvents.codeservice.studios.Studio;
import com.universalinvents.codeservice.studios.StudioRepository;
import com.universalinvents.codeservice.utilities.ApiDefinitions;
import com.universalinvents.codeservice.partners.ReferralPartner;
import com.universalinvents.codeservice.partners.ReferralPartnerRepository;
import com.universalinvents.codeservice.retailers.Retailer;
import com.universalinvents.codeservice.retailers.RetailerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<SummaryReport> getSummaryReport(
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {
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
    public ResponseEntity<CodeSummaryReport> getCodeSummaryReport(
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {
        MasterCodeDetails masterDetails = new MasterCodeDetails();
        masterDetails.setIssued(masterCodeRepository.countByStatus(MasterCode.Status.ISSUED));
        masterDetails.setPaired(masterCodeRepository.countByStatus(MasterCode.Status.PAIRED));
        masterDetails.setRedeemed(masterCodeRepository.countByStatus(MasterCode.Status.REDEEMED));
        masterDetails.setExpired(masterCodeRepository.countByStatus(MasterCode.Status.EXPIRED));

        RetailerCodeDetails retailerDetails = new RetailerCodeDetails();
        retailerDetails.setPaired(retailerCodeRepository.countByStatus(RetailerCode.Status.PAIRED));
        retailerDetails.setRedeemed(retailerCodeRepository.countByStatus(RetailerCode.Status.REDEEMED));
        retailerDetails.setExpired(retailerCodeRepository.countByStatus(RetailerCode.Status.EXPIRED));
        retailerDetails.setZombied(retailerCodeRepository.countByStatus(RetailerCode.Status.ZOMBIED));

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
    public ResponseEntity<MasterCodeReport> getMasterCodeReport(
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        // Start from the top and populate the report in this order:
        // totals, studios, content, partners, apps
        MasterCodeReport masterCodeReport = new MasterCodeReport();

        // ============= TOTALS ==============
        MasterCodeDetails masterTotalDetails = new MasterCodeDetails();
        masterTotalDetails.setExpired(masterCodeRepository.countByStatus(MasterCode.Status.EXPIRED));
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
            masterStudioDetails.setExpired(
                    masterCodeRepository.countByContentInAndStatus(contents, MasterCode.Status.EXPIRED));
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
                masterContentDetails.setExpired(
                        masterCodeRepository.countByContentAndStatus(content, MasterCode.Status.EXPIRED));
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
                    masterPartnerDetails.setExpired(
                            masterCodeRepository.countByContentAndReferralPartnerAndStatus(content, partner,
                                    MasterCode.Status
                                            .EXPIRED));
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
                        masterAppDetails.setExpired(
                                masterCodeRepository.countByContentAndReferralPartnerAndAppAndStatus(content, partner,
                                        app,
                                        MasterCode
                                                .Status
                                                .EXPIRED));
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
    public ResponseEntity<RetailerCodeReport> getRetailerCodeReport(
            @RequestHeader(value = "Request-Context", required = false)
            @ApiParam(value = ApiDefinitions.REQUEST_CONTEXT_HEADER_DESC)
                    String requestContext) {

        // Start from the top and populate the report in this order:
        // totals, studios, content, retailers, formats
        RetailerCodeReport retailerCodeReport = new RetailerCodeReport();

        // ============= TOTALS ==============
        RetailerCodeDetails retailerTotalDetails = new RetailerCodeDetails();
        retailerTotalDetails.setPaired(retailerCodeRepository.countByStatus(RetailerCode.Status.PAIRED));
        retailerTotalDetails.setRedeemed(retailerCodeRepository.countByStatus(RetailerCode.Status.REDEEMED));
        retailerTotalDetails.setExpired(retailerCodeRepository.countByStatus(RetailerCode.Status.EXPIRED));
        retailerTotalDetails.setZombied(retailerCodeRepository.countByStatus(RetailerCode.Status.ZOMBIED));
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
            retailerStudioDetails.setZombied(
                    retailerCodeRepository.countByContentInAndStatus(contents, RetailerCode.Status.ZOMBIED));


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
                retailerContentDetails.setZombied(
                        retailerCodeRepository.countByContentAndStatus(content, RetailerCode.Status.ZOMBIED));

                // ============= RETAILERS ============
                List<RetailerDetails> retailerDetails = new ArrayList<>();
                List<Retailer> retailers = retailerCodeRepository.findDistinctRetailersByContentId(content.getId());
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
                    retailerRetailerDetails.setZombied(
                            retailerCodeRepository.countByContentAndRetailerAndStatus(content, retailer,
                                    RetailerCode.Status.ZOMBIED));


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
                        retailerFormatDetails.setZombied(
                                retailerCodeRepository.countByContentAndRetailerAndFormatAndStatus(content, retailer,
                                        format,
                                        RetailerCode
                                                .Status
                                                .ZOMBIED));

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
