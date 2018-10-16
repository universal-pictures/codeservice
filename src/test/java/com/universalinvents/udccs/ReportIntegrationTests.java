package com.universalinvents.udccs;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.codes.MasterCode;
import com.universalinvents.udccs.codes.MasterCodeRepository;
import com.universalinvents.udccs.codes.RetailerCode;
import com.universalinvents.udccs.codes.RetailerCodeRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.pairings.Pairing;
import com.universalinvents.udccs.pairings.PairingRepository;
import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReportIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Value("${mock.est-inventory-service.url}")
    private String MOCK_BASE_URL;

    private Studio studio1;
    private Studio studio2;

    private Retailer retailer1;
    private Retailer retailer2;

    private ReferralPartner partner1;
    private ReferralPartner partner2;

    private Content content1;
    private Content content2;

    private App app1;
    private App app2;

    private MasterCode masterCode1;
    private MasterCode masterCode2;
    private MasterCode masterCode3;
    private MasterCode masterCode4;
    private MasterCode masterCode5;

    private RetailerCode retailerCode1;
    private RetailerCode retailerCode2;
    private RetailerCode retailerCode3;
    private RetailerCode retailerCode4;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {
        Date now = new Date();

        studio1 = new Studio(
                "Test Studio 1",
                "This is a test studio",
                "John Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                "TS",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "studio-external-id"
        );
        this.studioRepository.save(studio1);

        studio2 = new Studio(
                "Test Studio 2",
                "This is a test studio",
                "John Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                "TS",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "studio-external-id"
        );
        this.studioRepository.save(studio2);

        retailer1 = new Retailer(
                "Retailer 1",
                "US",
                "ACTIVE",
                null,
                null,
                "retailer-external-id",
                MOCK_BASE_URL
        );

        partner1 = new ReferralPartner(
                "Partner 1",
                "This is a test referral partner.",
                "Jane Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                null,
                now,
                "US",
                "https://api.universalinvents.com",
                "ACTIVE"
        );

        Set<Retailer> retailers1 = new HashSet<Retailer>();
        retailers1.add(retailer1);
        partner1.setRetailers(retailers1);

        Set<ReferralPartner> partners1 = new HashSet<ReferralPartner>();
        partners1.add(partner1);
        retailer1.setReferralPartners(partners1);

        this.referralPartnerRepository.save(partner1);
        this.retailerRepository.save(retailer1);

        retailer2 = new Retailer(
                "Retailer 2",
                "UK",
                "ACTIVE",
                null,
                null,
                "retailer-external-id",
                MOCK_BASE_URL
        );

        partner2 = new ReferralPartner(
                "Partner 2",
                "This is a test referral partner.",
                "Jane Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                null,
                now,
                "UK",
                "https://api.universalinvents.com",
                "ACTIVE"
        );

        Set<Retailer> retailers2 = new HashSet<Retailer>();
        retailers2.add(retailer2);
        partner1.setRetailers(retailers2);

        Set<ReferralPartner> partners2 = new HashSet<ReferralPartner>();
        partners2.add(partner2);
        retailer1.setReferralPartners(partners2);

        this.referralPartnerRepository.save(partner2);
        this.retailerRepository.save(retailer2);

        app1 = new App(
                null,
                "App 1",
                "This is a test app.",
                "access-token-1",
                "ACTIVE",
                partner1
        );
        this.appRepository.save(app1);

        app2 = new App(
                null,
                "App 2",
                "This is a test app.",
                "access-token-1",
                "ACTIVE",
                partner2
        );
        this.appRepository.save(app2);

        content1 = new Content(
                null,
                "Test Content",
                "10101010/0.1",
                "gtm-1",
                studio1
        );
        content1.setEidrv("content-eidrv-1");
        content1.setStatus("ACTIVE");
        this.contentRepository.save(content1);

        // Create content2
        content2 = new Content(
                null,
                "Test Content",
                "10101010/0.2",
                "gtm-2",
                studio2
        );
        content2.setEidrv("content-eidrv-2");
        content2.setStatus("ACTIVE");
        this.contentRepository.save(content2);

        masterCode1 = new MasterCode(
                "EEFEA1",
                "HD",
                "swong",
                null,
                null,
                partner1,
                app1,
                content1,
                MasterCode.Status.ISSUED,
                "master-code-1-external-id"
        );
        this.masterCodeRepository.save(masterCode1);

        masterCode2 = new MasterCode(
                "ASDFEF",
                "HD",
                "swong",
                null,
                null,
                partner1,
                app1,
                content1,
                MasterCode.Status.PAIRED,
                "master-code-2-external-id"
        );
        this.masterCodeRepository.save(masterCode2);

        masterCode3 = new MasterCode(
                "3FWFWE",
                "HD",
                "swong",
                null,
                null,
                partner2,
                app2,
                content2,
                MasterCode.Status.PAIRED,
                "master-code-3-external-id"
        );
        this.masterCodeRepository.save(masterCode3);

        masterCode4 = new MasterCode(
                "BEWFG",
                "HD",
                "swong",
                null,
                now,
                partner2,
                app2,
                content2,
                MasterCode.Status.EXPIRED,
                "master-code-4-external-id"
        );
        this.masterCodeRepository.save(masterCode4);

        masterCode5 = new MasterCode(
                "JGFDB",
                "HD",
                "swong-2",
                null,
                null,
                partner2,
                app2,
                content2,
                MasterCode.Status.REDEEMED,
                "master-code-5-external-id"
        );
        this.masterCodeRepository.save(masterCode5);

        retailerCode1 = new RetailerCode(
                "a231313",
                content1,
                "HD",
                RetailerCode.Status.PAIRED,
                retailer1,
                null
        );
        this.retailerCodeRepository.save(retailerCode1);

        retailerCode2 = new RetailerCode(
                "b231313",
                content1,
                "HD",
                RetailerCode.Status.EXPIRED,
                retailer1,
                now
        );
        this.retailerCodeRepository.save(retailerCode2);

        retailerCode3 = new RetailerCode(
                "c231313",
                content2,
                "HD",
                RetailerCode.Status.REDEEMED,
                retailer2,
                null
        );
        this.retailerCodeRepository.save(retailerCode3);

        retailerCode4 = new RetailerCode(
                "d231313",
                content2,
                "HD",
                RetailerCode.Status.REDEEMED,
                retailer2,
                null
        );
        this.retailerCodeRepository.save(retailerCode4);

        // Pair the test codes
        this.pairingRepository.save(new Pairing(
                masterCode2, retailerCode1, "TEST", "ACTIVE"));

        this.pairingRepository.save(new Pairing(
                masterCode3, retailerCode2, "TEST", "ACTIVE"));

        this.pairingRepository.save(new Pairing(
                masterCode4, retailerCode3, "TEST", "ACTIVE"));

        this.pairingRepository.save(new Pairing(
                masterCode5, retailerCode4, "TEST", "ACTIVE"));

        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
    }

    @After
    public void cleanup() throws Exception {
        this.pairingRepository.deleteAll();
        this.retailerCodeRepository.deleteAll();
        this.masterCodeRepository.deleteAll();
        this.contentRepository.deleteAll();
        this.appRepository.deleteAll();
        this.referralPartnerRepository.deleteAll();
        this.studioRepository.deleteAll();
        this.retailerRepository.deleteAll();
    }

    @Test
    public void getReportTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studios", is(2)))
                .andExpect(jsonPath("$.partners", is(2)))
                .andExpect(jsonPath("$.retailers", is(2)))
                .andExpect(jsonPath("$.contents", is(2)));
    }

    @Test
    public void getCodesReportTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/reports/codes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.masterCodes.PAIRED", is(2)))
                .andExpect(jsonPath("$.masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.masterCodes.REDEEMED", is(1)))
                .andExpect(jsonPath("$.retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.retailerCodes.REDEEMED", is(2)))
                .andExpect(jsonPath("$.retailerCodes.ZOMBIED", is(0)));
    }

    @Test
    public void getMasterCodesReportTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/reports/codes/master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.masterCodes.PAIRED", is(2)))
                .andExpect(jsonPath("$.masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.masterCodes.REDEEMED", is(1)))
                .andExpect(jsonPath("$.studios.*", hasSize(2)))
                .andExpect(jsonPath("$.studios[0].name", is(this.studio1.getName())))
                .andExpect(jsonPath("$.studios[0].masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.studios[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].masterCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[0].masterCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].name", is(this.content1.getTitle())))
                .andExpect(jsonPath("$.studios[0].content[0].masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].masterCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].masterCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].partners.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].name", is(this.partner1.getName())))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].masterCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].masterCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps[0].name", is(this.app1.getName())))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps[0].masterCodes.ISSUED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps[0].masterCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].partners[0].apps[0].masterCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[1].name", is(this.studio2.getName())))
                .andExpect(jsonPath("$.studios[1].masterCodes.ISSUED", is(0)))
                .andExpect(jsonPath("$.studios[1].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].masterCodes.REDEEMED", is(1)))
                .andExpect(jsonPath("$.studios[1].content.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].name", is(this.content2.getTitle())))
                .andExpect(jsonPath("$.studios[1].content[0].masterCodes.ISSUED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].masterCodes.REDEEMED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].name", is(this.partner2.getName())))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].masterCodes.ISSUED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].masterCodes.REDEEMED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps[0].name", is(this.app2.getName())))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps[0].masterCodes.ISSUED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps[0].masterCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps[0].masterCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[1].content[0].partners[0].apps[0].masterCodes.REDEEMED", is(1)));
    }

    @Test
    public void getRetailerCodesReportTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/reports/codes/retailer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.retailerCodes.REDEEMED", is(2)))
                .andExpect(jsonPath("$.studios.*", hasSize(2)))
                .andExpect(jsonPath("$.studios[0].name", is(this.studio1.getName())))
                .andExpect(jsonPath("$.studios[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[0].retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].retailerCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].name", is(this.content1.getTitle())))
                .andExpect(jsonPath("$.studios[0].content[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailerCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].name", is(this.retailer1.getName())))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].retailerCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats[0].name", is(this.retailerCode1.getFormat())))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats[0].retailerCodes.PAIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats[0].retailerCodes.EXPIRED", is(1)))
                .andExpect(jsonPath("$.studios[0].content[0].retailers[0].formats[0].retailerCodes.REDEEMED", is(0)))
                .andExpect(jsonPath("$.studios[1].name", is(this.studio2.getName())))
                .andExpect(jsonPath("$.studios[1].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[1].retailerCodes.PAIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].retailerCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].retailerCodes.REDEEMED", is(2)))
                .andExpect(jsonPath("$.studios[1].content.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].name", is(this.content2.getTitle())))
                .andExpect(jsonPath("$.studios[1].content[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailerCodes.PAIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailerCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailerCodes.REDEEMED", is(2)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].name", is(this.retailer2.getName())))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].retailerCodes.PAIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].retailerCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].retailerCodes.REDEEMED", is(2)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats.*", hasSize(1)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats[0].name", is(this.retailerCode1.getFormat())))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats[0].retailerCodes.ZOMBIED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats[0].retailerCodes.PAIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats[0].retailerCodes.EXPIRED", is(0)))
                .andExpect(jsonPath("$.studios[1].content[0].retailers[0].formats[0].retailerCodes.REDEEMED", is(2)));
    }

}
