/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice;

import com.universalinvents.codeservice.apps.App;
import com.universalinvents.codeservice.apps.AppRepository;
import com.universalinvents.codeservice.codes.MasterCode;
import com.universalinvents.codeservice.codes.MasterCodeRepository;
import com.universalinvents.codeservice.codes.RetailerCode;
import com.universalinvents.codeservice.codes.RetailerCodeRepository;
import com.universalinvents.codeservice.contents.Content;
import com.universalinvents.codeservice.contents.ContentRepository;
import com.universalinvents.codeservice.pairings.Pairing;
import com.universalinvents.codeservice.pairings.PairingRepository;
import com.universalinvents.codeservice.partners.ReferralPartner;
import com.universalinvents.codeservice.partners.ReferralPartnerRepository;
import com.universalinvents.codeservice.retailers.Retailer;
import com.universalinvents.codeservice.retailers.RetailerRepository;
import com.universalinvents.codeservice.studios.Studio;
import com.universalinvents.codeservice.studios.StudioRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RetailerCodeIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RetailerCodeRepository retailerCodeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ReferralPartnerRepository referralPartnerRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private MasterCodeRepository masterCodeRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Value("${mock.est-inventory-service.url}")
    private String MOCK_BASE_URL;

    private Content content;

    private Studio studio;

    private Retailer retailer;

    private RetailerCode retailerCode1;
    private RetailerCode retailerCode2;
    private RetailerCode retailerCode3;
    private RetailerCode retailerCode4;
    private RetailerCode retailerCode5;

    private ReferralPartner partner;

    private App app;

    private MasterCode masterCode;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {
        Date now = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, 7);
        Date later = c.getTime();

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
        this.studioRepository.save(studio);

        retailer = new Retailer(
                "Retailer",
                "US",
                "ACTIVE",
                null,
                null,
                "retailer-external-id",
                MOCK_BASE_URL
        );
        this.retailerRepository.save(retailer);

        content = new Content(
                null,
                "Test Content",
                "10101010/0.1",
                "gtm-1",
                studio
        );
        content.setEidrv("content-eidrv-1");
        content.setStatus("ACTIVE");
        this.contentRepository.save(content);

        retailerCode1 = new RetailerCode(
            "a231313",
                    content,
                    "SD",
                    RetailerCode.Status.PAIRED,
                    retailer,
                    later
        );
        this.retailerCodeRepository.save(retailerCode1);

        retailerCode2 = new RetailerCode(
            "b231313",
                    content,
                    "HD",
                    RetailerCode.Status.PAIRED,
                    retailer,
                    later
        );
        this.retailerCodeRepository.save(retailerCode2);

        retailerCode3 = new RetailerCode(
            "c231313",
                    content,
                    "HD",
                    RetailerCode.Status.PAIRED,
                    retailer,
                    later
        );
        this.retailerCodeRepository.save(retailerCode3);

        retailerCode4 = new RetailerCode(
            "d231313",
                    content,
                    "HD",
                    RetailerCode.Status.PAIRED,
                    retailer,
                    later
        );
        this.retailerCodeRepository.save(retailerCode4);

        retailerCode5 = new RetailerCode(
            "e231313",
                    content,
                    "SD",
                    RetailerCode.Status.EXPIRED,
                    retailer,
                    later
        );
        this.retailerCodeRepository.save(retailerCode5);

        partner = new ReferralPartner(
                "Partner",
                "This is a test referral partner.",
                "Jane Doe",
                "admin@universalinvents.com",
                "(818) 555-5555",
                null,
                new Date(),
                "UK",
                "https://api.universalinvents.com",
                "ACTIVE"
        );
        this.referralPartnerRepository.save(partner);

        Set<Retailer> retailers = new HashSet<Retailer>();
        retailers.add(retailer);
        partner.setRetailers(retailers);

        Set<ReferralPartner> partners = new HashSet<ReferralPartner>();
        partners.add(partner);
        retailer.setReferralPartners(partners);

        app = new App(
                null,
                "App 1",
                "This is a test app.",
                "access-token-1",
                "ACTIVE",
                partner
        );
        this.appRepository.save(app);

        masterCode = new MasterCode(
                "AZFEF",
                "HD",
                "swong",
                null,
                null,
                partner,
                app,
                this.content,
                MasterCode.Status.ISSUED,
                "master-code-1-external-id"
        );
        masterCode.setStatus(MasterCode.Status.PAIRED);
        this.masterCodeRepository.save(masterCode);

        Pairing pairing = new Pairing(masterCode, retailerCode1, "TEST", "ACTIVE");
        this.pairingRepository.save(pairing);

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
    public void getRetailerCodesByContentIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("contentId", this.content.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(5)))
                .andExpect(jsonPath("$.content[0].content.id", is(toIntExact(this.content.getId()))))
                .andExpect(jsonPath("$.content[1].content.id", is(toIntExact(this.content.getId()))))
                .andExpect(jsonPath("$.content[2].content.id", is(toIntExact(this.content.getId()))))
                .andExpect(jsonPath("$.content[3].content.id", is(toIntExact(this.content.getId()))))
                .andExpect(jsonPath("$.content[4].content.id", is(toIntExact(this.content.getId()))));
    }

    @Test
    public void getRetailerCodesByFormatTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("format", this.retailerCode2.getFormat()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(3)))
                .andExpect(jsonPath("$.content[0].format", is(retailerCode2.getFormat())))
                .andExpect(jsonPath("$.content[1].format", is(retailerCode3.getFormat())))
                .andExpect(jsonPath("$.content[2].format", is(retailerCode4.getFormat())));
    }

    @Test
    public void getRetailerCodesByRetailerIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("retailerId", this.retailer.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(5)))
                .andExpect(jsonPath("$.content[0].retailer.id", is(toIntExact(this.retailer.getId()))))
                .andExpect(jsonPath("$.content[1].retailer.id", is(toIntExact(this.retailer.getId()))))
                .andExpect(jsonPath("$.content[2].retailer.id", is(toIntExact(this.retailer.getId()))))
                .andExpect(jsonPath("$.content[3].retailer.id", is(toIntExact(this.retailer.getId()))))
                .andExpect(jsonPath("$.content[4].retailer.id", is(toIntExact(this.retailer.getId()))));
    }

    @Test
    public void getRetailerCodesByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("status", this.retailerCode1.getStatus().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(4)))
                .andExpect(jsonPath("$.content[0].status", is(this.retailerCode1.getStatus().toString())))
                .andExpect(jsonPath("$.content[1].status", is(this.retailerCode2.getStatus().toString())))
                .andExpect(jsonPath("$.content[2].status", is(this.retailerCode3.getStatus().toString())))
                .andExpect(jsonPath("$.content[3].status", is(this.retailerCode4.getStatus().toString())));
    }

    @Test
    public void getRetailerCodesByStudioIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("studioId", this.studio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(5)))
                .andExpect(jsonPath("$.content[0].content.studio.id", is(toIntExact(this.studio.getId()))))
                .andExpect(jsonPath("$.content[1].content.studio.id", is(toIntExact(this.studio.getId()))))
                .andExpect(jsonPath("$.content[2].content.studio.id", is(toIntExact(this.studio.getId()))))
                .andExpect(jsonPath("$.content[3].content.studio.id", is(toIntExact(this.studio.getId()))))
                .andExpect(jsonPath("$.content[4].content.studio.id", is(toIntExact(this.studio.getId()))));
    }

    @Test
    public void getRetailerCodesTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer")
                .param("contentId", this.retailerCode1.getContent().getId().toString())
                .param("format", this.retailerCode1.getFormat())
                .param("retailerId", this.retailerCode1.getRetailer().getId().toString())
                .param("status", this.retailerCode1.getStatus().toString())
                .param("studioId", this.retailerCode1.getContent().getStudio().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].content.id", is(toIntExact(this.retailerCode1.getContent().getId()))))
                .andExpect(jsonPath("$.content[0].format", is(this.retailerCode1.getFormat())))
                .andExpect(jsonPath("$.content[0].retailer.id", is(toIntExact(this.retailerCode1.getRetailer().getId()))))
                .andExpect(jsonPath("$.content[0].status", is(this.retailerCode1.getStatus().toString())))
                .andExpect(jsonPath("$.content[0].content.studio.id", is(toIntExact(this.retailerCode1.getContent().getStudio().getId()))));
    }

    @Test
    public void getRetailerCodeByCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/codes/retailer/" + this.retailerCode1.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(this.retailerCode1.getCode())));
    }

    /*
     * Test expiring a retailer code
     * Assume that when checking the true statues from the EST Inventory Service, the status returned is EXPIRED
     */
    @Test
    public void expireRetailerCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/expire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(RetailerCode.Status.EXPIRED.toString())));
    }

    /*
     * Try to redeem a PAIRED retailer code
     */
    @Test
    public void redeemRetailerCodeTest1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/redeem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(RetailerCode.Status.REDEEMED.toString())));
    }

    /*
     * Try to redeem an already REDEEMED retailer code
     */
    @Test
    public void redeemRetailerCodeTest2() throws Exception {
        // Redeem once
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/redeem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(RetailerCode.Status.REDEEMED.toString())));

        // Then, redeem again
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/redeem"))
                .andExpect(status().isBadRequest());

    }

    /*
     * Try to redeem an EXPIRED retailer code
     */
    @Test
    public void redeemRetailerCodeTest3() throws Exception {
        // First, expire the code
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/expire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(RetailerCode.Status.EXPIRED.toString())));

        // Then, tyr to redeem
        mvc.perform(MockMvcRequestBuilders.put("/api/codes/retailer/" + this.retailerCode1.getCode() + "/redeem"))
                .andExpect(status().isBadRequest());
    }

}
