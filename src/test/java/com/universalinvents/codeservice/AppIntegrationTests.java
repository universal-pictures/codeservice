package com.universalinvents.codeservice;

import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.apps.AppRepository;
import com.universalinvents.codeservice.partners.ReferralPartner;
import com.universalinvents.codeservice.partners.ReferralPartnerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AppIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;


    private ReferralPartner partner;
    private ReferralPartner partner2;
    private App app1;
    private MediaType contentType;

    @Before
    public void setup() throws Exception {

        // Setup a dummy referral partner
        Date now = new Date();
        partner = new ReferralPartner(
                "Partner",
                "This is a test referral partner 1.",
                "Johnny Doe",
                "admin1@universalinvents.com",
                "(818) 555-55551",
                now,
                now,
                "UK",
                "https://api.universalinvents.com",
                "ACTIVE"
        );
        partner2 = new ReferralPartner(
                "Partner 2",
                "This is a test referral partner.",
                "Jane Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                now,
                now,
                "UK",
                "http://api.universalinvents.com",
                "INACTIVE"
        );

        this.referralPartnerRepository.save(partner);
        this.referralPartnerRepository.save(partner2);

        app1 = new App(
                null,
                "App 1",
                "This is a test app.",
                "access-token-1",
                "ACTIVE",
                this.partner
        );
        this.appRepository.save(app1);

        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
    }

    @After
    public void cleanup() throws Exception {
        this.appRepository.deleteAll();
        this.referralPartnerRepository.deleteAll();
    }

    @Test
    public void getAppsByAccessTokenTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps")
                .param("accessToken", this.app1.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].accessToken", is(this.app1.getAccessToken())));
    }

    @Test
    public void getAppsByNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps")
                .param("name", this.app1.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(this.app1.getName())));
    }

    @Test
    public void getAppsByPartnerIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps")
                .param("partnerId", this.partner.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].referralPartner.id", is(toIntExact(this.partner.getId()))));
    }

    @Test
    public void getAppsByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps")
                .param("status", this.app1.getStatus()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status", is(this.app1.getStatus())));
    }

    @Test
    public void getAppsTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps")
                .param("accessToken", this.app1.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].accessToken", is(this.app1.getAccessToken())))
                .andExpect(jsonPath("$.content[0].name", is(this.app1.getName())))
                .andExpect(jsonPath("$.content[0].referralPartner.id", is(toIntExact(this.partner.getId()))))
                .andExpect(jsonPath("$.content[0].status", is(this.app1.getStatus())));
    }

    @Test
    public void createAppTest() throws Exception {
        App app = new App();
        app.setAccessToken("access-token-create-app-test");
        app.setDescription("This is a test app");
        app.setName("create-app-test");
        app.setReferralPartner(this.partner);
        app.setStatus("ACTIVE");

        String params = this.buildRequestPayload(app);

        mvc.perform(MockMvcRequestBuilders.post("/api/apps")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken", is(app.getAccessToken())))
                .andExpect(jsonPath("$.description", is(app.getDescription())))
                .andExpect(jsonPath("$.name", is(app.getName())))
                .andExpect(jsonPath("$.referralPartner.id", is(toIntExact(app.getReferralPartner().getId()))))
                .andExpect(jsonPath("$.status", is(app.getStatus())));
    }

    @Test
    public void getAppsByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/apps/" + this.app1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.app1.getId()))));
    }

    @Test
    public void deleteAppTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/apps/" + this.app1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchAppTest() throws Exception {
        App updatedApp = new App();
        updatedApp.setAccessToken("access-token-update-app-test");
        updatedApp.setDescription("This is a test app update");
        updatedApp.setName("update-app-test");
        updatedApp.setReferralPartner(this.partner2);
        updatedApp.setStatus("INACTIVE");

        String params = this.buildRequestPayload(updatedApp);

        mvc.perform(MockMvcRequestBuilders.patch("/api/apps/" + this.app1.getId())
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is(updatedApp.getAccessToken())))
                .andExpect(jsonPath("$.description", is(updatedApp.getDescription())))
                .andExpect(jsonPath("$.name", is(updatedApp.getName())))
                .andExpect(jsonPath("$.referralPartner.id", is(toIntExact(updatedApp.getReferralPartner().getId()))))
                .andExpect(jsonPath("$.status", is(updatedApp.getStatus())));
    }

    private String buildRequestPayload(App app) {
        String payload = String.format(
            "{\"accessToken\": \"%s\", "
                    + "\"description\": \"%s\", "
                    + "\"name\": \"%s\", "
                    + "\"partnerId\": %d, "
                    + "\"status\": \"%s\"}",
                app.getAccessToken(), app.getDescription(), app.getName(),
                app.getReferralPartner().getId(), app.getStatus()
        );
        return payload;
    }
}
