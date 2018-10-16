package com.universalinvents.udccs;

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

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReferralPartnerIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Value("${mock.est-inventory-service.url}")
    private String MOCK_BASE_URL;

    private ReferralPartner partner1;
    private ReferralPartner partner2;
    private ReferralPartner partner3;

    private Retailer retailer;

    private Studio studio;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {
        Date now = new Date();

        studio = new Studio(
                "Test Studio 1",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "TS",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "studio-external-id"
        );

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
                "Partner 1",
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

        partner2 = new ReferralPartner(
                "Partner 2",
                "This is a test referral partner.",
                "Jane Doe",
                "admi2@universalinvents.com",
                "(818) 555-5555",
                null,
                now,
                "UK",
                "https://api.universalinvents.com",
                "ACTIVE"
        );
        this.referralPartnerRepository.save(partner2);

        partner3 = new ReferralPartner(
                "Partner3 ",
                "This is a test referral partner.",
                "Jane Doe",
                "admin3@universalinvents.com",
                "(818) 555-5555",
                null,
                now,
                "UK",
                "https://api.universalinvents.com",
                "INACTIVE"
        );

        Set<Studio> studios = new HashSet<Studio>();
        studios.add(studio);

        Set<Retailer> retailers = new HashSet<Retailer>();
        retailers.add(retailer);

        Set<ReferralPartner> partners = new HashSet<ReferralPartner>();
        partners.add(partner1);


        this.studio.setReferralPartners(partners);
        this.partner1.setStudios(studios);
        this.retailer.setReferralPartners(partners);
        this.partner1.setRetailers(retailers);

        this.referralPartnerRepository.save(partner1);
        this.referralPartnerRepository.save(partner2);
        this.referralPartnerRepository.save(partner3);

        this.retailerRepository.save(retailer);

        this.studioRepository.save(studio);

        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
    }

    @After
    public void cleanup() throws Exception {
        this.referralPartnerRepository.deleteAll();
        this.studioRepository.deleteAll();
    }

    @Test
    public void getReferralPartnersByContactEmailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("contactEmail", this.partner1.getContactEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].contactEmail", is(this.partner1.getContactEmail())));
    }

    @Test
    public void getReferralPartnersByContactNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("contactName", this.partner1.getContactName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(3)))
                .andExpect(jsonPath("$.content[0].contactName", is(this.partner1.getContactName())))
                .andExpect(jsonPath("$.content[1].contactName", is(this.partner2.getContactName())))
                .andExpect(jsonPath("$.content[2].contactName", is(this.partner3.getContactName())));
    }

    @Test
    public void getReferralPartnersByNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("name", this.partner1.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(this.partner1.getName())));
    }

    @Test
    public void getReferralPartnersByRetailerIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("retailerId", this.retailer.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].retailers.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].retailers[0].id", is(toIntExact(this.retailer.getId()))));
    }

    @Test
    public void getReferralPartnersByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.content[1].status", is("ACTIVE")));
    }

    @Test
    public void getReferralPartnersByStudioIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("studioId", this.studio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].studios.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].studios[0].id", is(toIntExact(this.studio.getId()))));
    }

    @Test
    public void getReferralPartnersTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners")
                .param("contactEmail", this.partner1.getContactEmail())
                .param("contactName", this.partner1.getContactName())
                .param("name", this.partner1.getName())
                .param("retailerId", this.retailer.getId().toString())
                .param("status", this.partner1.getStatus())
                .param("studioId", this.studio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].contactEmail", is(this.partner1.getContactEmail())))
                .andExpect(jsonPath("$.content[0].contactName", is(this.partner1.getContactName())))
                .andExpect(jsonPath("$.content[0].name", is(this.partner1.getName())))
                .andExpect(jsonPath("$.content[0].retailers[0].id", is(toIntExact(this.retailer.getId()))))
                .andExpect(jsonPath("$.content[0].status", is(this.partner1.getStatus())))
                .andExpect(jsonPath("$.content[0].studios[0].id", is(toIntExact(this.studio.getId()))));
    }

    @Test
    public void createReferralPartnerTest() throws Exception {
        ReferralPartner newPartner = new ReferralPartner();
        newPartner.setContactEmail("admin@universalinvents.com");
        newPartner.setContactName("Jane Doe");
        newPartner.setContactPhone("(818) 555-1234");
        newPartner.setDescription("New partner");
        newPartner.setLogoUrl("");
        newPartner.setName("New Partner");
        newPartner.setStatus("ACTIVE");

        String params = this.buildRequestPayload(newPartner);

        mvc.perform(MockMvcRequestBuilders.post("/api/partners")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactEmail", is(newPartner.getContactEmail())))
                .andExpect(jsonPath("$.contactName", is(newPartner.getContactName())))
                .andExpect(jsonPath("$.contactPhone", is(newPartner.getContactPhone())))
                .andExpect(jsonPath("$.description", is(newPartner.getDescription())))
                .andExpect(jsonPath("$.logoUrl", is(newPartner.getLogoUrl())))
                .andExpect(jsonPath("$.name", is(newPartner.getName())))
                .andExpect(jsonPath("$.status", is(newPartner.getStatus())));

    }

    @Test
    public void getReferralPartnersByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/partners/" + this.partner1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.partner1.getId()))));
    }

    @Test
    public void deleteReferralPartnerTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/partners/" + this.partner1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchReferralPartnerTest() throws Exception {
        ReferralPartner updatePartner = new ReferralPartner();
        updatePartner.setContactEmail("bob@universalinvents.com");
        updatePartner.setContactName("Joseph Doe");
        updatePartner.setContactPhone("(818) 555-0123");
        updatePartner.setDescription("Updating partner");
        updatePartner.setLogoUrl("http://example.com");
        updatePartner.setName("Update Partner");
        updatePartner.setRetailers(new HashSet<Retailer>());
        updatePartner.setStatus("INACTIVE");
        updatePartner.setStudios(new HashSet<Studio>());

        String params = this.buildRequestPayload(updatePartner);

        mvc.perform(MockMvcRequestBuilders.patch("/api/partners/" + this.partner1.getId())
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactEmail", is(updatePartner.getContactEmail())))
                .andExpect(jsonPath("$.contactName", is(updatePartner.getContactName())))
                .andExpect(jsonPath("$.contactPhone", is(updatePartner.getContactPhone())))
                .andExpect(jsonPath("$.description", is(updatePartner.getDescription())))
                .andExpect(jsonPath("$.logoUrl", is(updatePartner.getLogoUrl())))
                .andExpect(jsonPath("$.name", is(updatePartner.getName())))
                .andExpect(jsonPath("$.status", is(updatePartner.getStatus())));
    }

    private String buildRequestPayload(ReferralPartner partner) {
        String payload = String.format(
                "{\"contactEmail\": \"%s\", "
                        + "\"contactName\": \"%s\", "
                        + "\"contactPhone\": \"%s\", "
                        + "\"description\": \"%s\", "
                        + "\"logoUrl\": \"%s\", "
                        + "\"name\": \"%s\", "
                        + "\"retailerIds\": [], "
                        + "\"status\": \"%s\", "
                        + "\"studioIds\": [] }",
                partner.getContactEmail(), partner.getContactName(), partner.getContactPhone(), partner.getDescription(),
                partner.getLogoUrl(), partner.getName(), partner.getStatus()
        );
        return payload;
    }
}
