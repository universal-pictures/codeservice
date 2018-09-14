package com.universalinvents.udccs;

import com.universalinvents.udccs.partners.ReferralPartner;
import com.universalinvents.udccs.partners.ReferralPartnerRepository;
import com.universalinvents.udccs.retailers.Retailer;
import com.universalinvents.udccs.retailers.RetailerRepository;
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

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RetailerIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Value("${mock.est-inventory-service.url}")
    private String MOCK_BASE_URL;

    private Retailer retailer1;
    private Retailer retailer2;
    private Retailer retailer3;

    private ReferralPartner partner;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {
        Date now = new Date();

        retailer1 = new Retailer(
                "Retailer 1",
                "US",
                "ACTIVE",
                null,
                null,
                "retailer-1-external-id",
                MOCK_BASE_URL
        );

        retailer2 = new Retailer(
                "Retailer 2",
                "UK",
                "ACTIVE",
                null,
                null,
                "retailer-2-external-id",
                MOCK_BASE_URL
        );

        retailer3 = new Retailer(
                "Retailer 3",
                "US",
                "INACTIVE",
                null,
                null,
                "retailer-3-external-id",
                ""
        );

        partner = new ReferralPartner(
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
        retailers.add(retailer1);

        Set<ReferralPartner> partners = new HashSet<ReferralPartner>();
        partners.add(partner);

        retailer1.setReferralPartners(partners);
        partner.setRetailers(retailers);

        this.referralPartnerRepository.save(partner);

        this.retailerRepository.save(retailer1);
        this.retailerRepository.save(retailer2);
        this.retailerRepository.save(retailer3);

        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
    }

    @After
    public void cleanup() throws Exception {
        this.referralPartnerRepository.deleteAll();
        this.retailerRepository.deleteAll();
    }

    @Test
    public void getRetailersWithInventoryTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("eidr", "1.0")
                .param("hasInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void getRetailersByExternalIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("externalId", this.retailer1.getExternalId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].externalId", is(this.retailer1.getExternalId())));
    }

    @Test
    public void getRetailersByNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("name", this.retailer1.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].name", is(this.retailer1.getName())));
    }

    @Test
    public void getRetailersByPartnerIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("partnerId", this.partner.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    public void getRetailersByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[1].status", is("ACTIVE")));
    }

    @Test
    public void getRetailersTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers")
                .param("eidr", "101010/1.0")
                .param("hasInventory", "true")
                .param("externalId", this.retailer1.getExternalId())
                .param("name", this.retailer1.getName())
                .param("partnerId", this.partner.getId().toString())
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    public void createRetailerTest() throws Exception {
        Retailer newRetailer = new Retailer();
        newRetailer.setBaseUrl(MOCK_BASE_URL);
        newRetailer.setExternalId("new-retailer-external-id");
        newRetailer.setLogoUrl("http://example.com");
        newRetailer.setName("New Retailer");
        newRetailer.setRedemptionUrl("http://example.com/redeem");
        newRetailer.setStatus("ACTIVE");

        String params = this.buildRequestPayload(newRetailer);

        mvc.perform(MockMvcRequestBuilders.post("/api/retailers")
                .contentType(contentType)
                .content(params));
    }

    @Test
    public void getRetailersByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/retailers/" + this.retailer1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.retailer1.getId()))));
    }

    @Test
    public void deleteRetailerTest() throws Exception {
        this.referralPartnerRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/api/retailers/" + this.retailer2.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchRetailerTest() throws Exception {
        Retailer updateRetailer = new Retailer();
        updateRetailer.setBaseUrl(MOCK_BASE_URL);
        updateRetailer.setExternalId("update-retailer-external-id");
        updateRetailer.setLogoUrl("http://example1.com");
        updateRetailer.setName("Update Retailer");
        updateRetailer.setRedemptionUrl("http://example1.com/redeem");
        updateRetailer.setStatus("INACTIVE");

        String params = this.buildRequestPayload(updateRetailer);

        mvc.perform(MockMvcRequestBuilders.patch("/api/retailers")
                .contentType(contentType)
                .content(params));
    }

    private String buildRequestPayload(Retailer retailer) {
        String payload = String.format(
                "{\"baseUrl\": \"%s\", "
                        + "\"externalId\": \"%s\", "
                        + "\"logoUrl\": \"%s\", "
                        + "\"name\": \"%s\", "
                        + "\"redemptionUrl\": \"%s\", "
                        + "\"status\": \"%s\" } ",
                retailer.getBaseUrl(), retailer.getExternalId(), retailer.getLogoUrl(),
                retailer.getName(), retailer.getRedemptionUrl(), retailer.getStatus()
        );
        return payload;
    }

}
