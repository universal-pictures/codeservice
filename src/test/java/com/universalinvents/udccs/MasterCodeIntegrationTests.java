package com.universalinvents.udccs;

import com.universalinvents.udccs.apps.App;
import com.universalinvents.udccs.apps.AppRepository;
import com.universalinvents.udccs.codes.MasterCode;
import com.universalinvents.udccs.codes.MasterCodeRepository;
import com.universalinvents.udccs.codes.RetailerCode;
import com.universalinvents.udccs.codes.RetailerCodeRepository;
import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MasterCodeIntegrationTests {

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

    private MasterCode masterCode1;
    private MasterCode masterCode2;
    private MasterCode masterCode3;
    private MasterCode masterCode4;
    private MasterCode masterCode5;

    private Content content1;
    private Content content2;

    private App app1;
    private App app2;

    private Studio studio1;
    private Studio studio2;

    private ReferralPartner partner1;
    private ReferralPartner partner2;

    private Retailer retailer;

    private RetailerCode retailerCode;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {

        // Setup dummy data
        Date now = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, 7);
        Date later = c.getTime();

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

        retailer = new Retailer(
                "Retailer",
                "US",
                "ACTIVE",
                null,
                null,
                "retailer-external-id",
                MOCK_BASE_URL
        );

        partner1 = new ReferralPartner(
                "Partner",
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

        Set<Retailer> retailers = new HashSet<Retailer>();
        retailers.add(retailer);
        partner1.setRetailers(retailers);

        Set<ReferralPartner> partners = new HashSet<ReferralPartner>();
        partners.add(partner1);
        retailer.setReferralPartners(partners);

        this.referralPartnerRepository.save(partner1);
        this.retailerRepository.save(retailer);

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
        this.referralPartnerRepository.save(partner2);

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
                "AZFEF",
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

        masterCode1 = new MasterCode(
                "AZFEF",
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
                now,
                partner1,
                app1,
                content1,
                MasterCode.Status.ISSUED,
                "master-code-2-external-id"
        );
        this.masterCodeRepository.save(masterCode2);

        masterCode3 = new MasterCode(
                "3FWFWE",
                "HD",
                "swong",
                null,
                later,
                partner2,
                app2,
                content2,
                MasterCode.Status.ISSUED,
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
                now,
                partner2,
                app2,
                content2,
                MasterCode.Status.ISSUED,
                "master-code-5-external-id"
        );
        this.masterCodeRepository.save(masterCode5);

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
    public void getMasterCodesByAppIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("appId", this.masterCode1.getApp().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].app.id", is(toIntExact(this.masterCode1.getApp().getId()))))
                .andExpect(jsonPath("$.content[1].app.id", is(toIntExact(this.masterCode2.getApp().getId()))));
    }

    @Test
    public void getMasterCodesByContentIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("contentId", this.masterCode1.getContent().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].content.id", is(toIntExact(this.masterCode1.getContent().getId()))))
                .andExpect(jsonPath("$.content[1].content.id", is(toIntExact(this.masterCode1.getContent().getId()))));
    }

    @Test
    public void getMasterCodesByExternalIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("externalId", this.masterCode1.getExternalId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].externalId", is(this.masterCode1.getExternalId())));
    }

    @Test
    public void getMasterCodesByPartnerIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("partnerId", this.masterCode1.getReferralPartner().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].referralPartner.id", is(toIntExact(this.masterCode1.getReferralPartner().getId()))))
                .andExpect(jsonPath("$.content[1].referralPartner.id", is(toIntExact(this.masterCode1.getReferralPartner().getId()))));
    }

    @Test
    public void getMasterCodesByStatusTest1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("status", MasterCode.Status.ISSUED.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(4)))
                .andExpect(jsonPath("$.content[0].status", is(MasterCode.Status.ISSUED.toString())))
                .andExpect(jsonPath("$.content[1].status", is(MasterCode.Status.ISSUED.toString())))
                .andExpect(jsonPath("$.content[2].status", is(MasterCode.Status.ISSUED.toString())))
                .andExpect(jsonPath("$.content[3].status", is(MasterCode.Status.ISSUED.toString())));
    }

    @Test
    public void getMasterCodesByStatusTest2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("status", MasterCode.Status.EXPIRED.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status", is(MasterCode.Status.EXPIRED.toString())));
    }

    @Test
    public void getMasterCodesTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master")
                .param("appId", this.masterCode1.getApp().getId().toString())
                .param("contentId", this.masterCode1.getContent().getId().toString())
                .param("externalId", this.masterCode1.getExternalId())
                .param("partnerId", this.masterCode1.getReferralPartner().getId().toString())
                .param("status", this.masterCode1.getStatus().toString())
                .param("studioId", this.masterCode1.getContent().getStudio().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].app.id", is(toIntExact(this.masterCode1.getApp().getId()))))
                .andExpect(jsonPath("$.content[0].content.id", is(toIntExact(this.masterCode1.getContent().getId()))))
                .andExpect(jsonPath("$.content[0].externalId", is(this.masterCode1.getExternalId())))
                .andExpect(jsonPath("$.content[0].referralPartner.id", is(toIntExact(this.masterCode1.getReferralPartner().getId()))))
                .andExpect(jsonPath("$.content[0].status", is(this.masterCode1.getStatus().toString())))
                .andExpect(jsonPath("$.content[0].content.studio.id", is(toIntExact(this.masterCode1.getContent().getStudio().getId()))));

    }

    @Test
    public void createMasterCodeTest() throws Exception {
        MasterCode newMasterCode = new MasterCode();
        newMasterCode.setApp(this.app1);
        newMasterCode.setContent(this.content1);
        newMasterCode.setCreatedBy("swong");
        newMasterCode.setExternalId(("new-master-code-external-id"));
        newMasterCode.setFormat("HD");
        newMasterCode.setReferralPartner(this.partner1);

        String params = this.buildRequestPayload(newMasterCode);

        mvc.perform(MockMvcRequestBuilders.post("/api/codes/master")
                .contentType((contentType))
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.app.id", is(toIntExact(newMasterCode.getApp().getId()))))
                .andExpect(jsonPath("$.code", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.content.id", is(toIntExact(newMasterCode.getContent().getId()))))
                .andExpect(jsonPath("$.createdBy", is(newMasterCode.getCreatedBy())))
                .andExpect(jsonPath("$.externalId", is(newMasterCode.getExternalId())))
                .andExpect(jsonPath("$.format", is(newMasterCode.getFormat())))
                .andExpect(jsonPath("$.paired", is(false)))
                .andExpect(jsonPath("$.referralPartner.id", is(toIntExact(newMasterCode.getReferralPartner().getId()))))
                .andExpect(jsonPath("$.status", is(MasterCode.Status.ISSUED.toString())));
    }

    @Test
    public void getMasterCodeByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/master/" + this.masterCode1.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(this.masterCode1.getCode())));
    }

    @Test
    public void patchMasterCodeTest() throws Exception {
        String params = String.format("{ \"contentId\": %d }", this.content2.getId());
        mvc.perform(MockMvcRequestBuilders.patch("/api/codes/master/" + this.masterCode1.getCode())
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.id", is(toIntExact(this.content2.getId()))));
    }

    @Test
    public void expireMasterCodeTest1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/expire"))
                .andExpect(status().isNotModified());
    }

    @Test
    public void expireMasterCodeTest2() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/expire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(MasterCode.Status.EXPIRED.toString())));
    }

    @Test
    public void expireMasterCodeTest3() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode3.getCode() + "/expire"))
                .andExpect(status().isNotModified());
    }

    /*
     * Test pairing an ISSUED master code
     */
    @Test
    public void pairMasterCodeTest1() throws Exception {
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));
    }

    /*
     * Test trying to re-pair a NON-EXPIRED master code with an expired retailer code
     */
    @Test
    public void pairMasterCodeTest2() throws Exception {
        // First, pair the master code to a retailer code
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));

        // Then, try to pair again.
        // Assume that the check for the retailer code status will return 'EXPIRED'
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));
    }

    /*
     * Test trying to re-pair an EXPIRED master code with an expired retailer code
     */
    @Test
    public void pairMasterCodeTest3() throws Exception {
        // First, pair the master code to a retailer code
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));

        // Expire the master code
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/expire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(MasterCode.Status.EXPIRED.toString())));

        // Then, try to pair again.
        // Assume that the check for the retailer code status will return 'EXPIRED'
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isBadRequest());
    }

    /*
     * Try to unpair a NON-EXPIRED paired master code
     */
    @Test
    public void unpairMasterCodeTest1() throws Exception {
        // First, pair the master code
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));

        // Then, try to unpair the code
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/unpair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(false)));
    }

    /*
     * Try to unpair a REDEEMED paired master code
     */
    @Test
    public void unpairMasterCodeTest2() throws Exception {
        // First, pair the master code
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));

        // Set the master code status to REDEEMED
        this.masterCode2.setStatus(MasterCode.Status.REDEEMED);
        this.masterCodeRepository.save(this.masterCode2);

        // Then, try to unpair the code
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode2.getCode() + "/unpair"))
                .andExpect(status().isBadRequest());
    }

    /*
     * Try to unpair an EXPIRED paired master code
     */
    @Test
    public void unpairMasterCodeTest3() throws Exception {
        // First, pair the master code
        String params = String.format("{ \"retailerId\": %d }", this.retailer.getId());
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/pair")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paired", is(true)))
                .andExpect(jsonPath("$.pairing", notNullValue()))
                .andExpect(jsonPath("$.pairing.retailerCode", notNullValue()));

        // Set the master code status to EXPIRED
        this.masterCode1.setStatus(MasterCode.Status.EXPIRED);
        this.masterCodeRepository.save(this.masterCode1);

        // Then, try to unpair the code
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/master/" + this.masterCode1.getCode() + "/unpair"))
                .andExpect(status().isBadRequest());
    }

    private String buildRequestPayload(MasterCode mc) {
        String payload = String.format(
                "{\"appId\": %d, "
                        + "\"contentId\": %d, "
                        + "\"partnerId\": %d, "
                        + "\"createdBy\": \"%s\", "
                        + "\"externalId\": \"%s\", "
                        + "\"format\": \"%s\"}",
                mc.getApp().getId(), mc.getContent().getId(), mc.getReferralPartner().getId(), mc.getCreatedBy(),
                mc.getExternalId(), mc.getFormat()
        );
        return payload;
    }
}
