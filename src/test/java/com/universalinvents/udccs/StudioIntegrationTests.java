package com.universalinvents.udccs;

import com.universalinvents.udccs.contents.Content;
import com.universalinvents.udccs.contents.ContentRepository;
import com.universalinvents.udccs.studios.Studio;
import com.universalinvents.udccs.studios.StudioRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StudioIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ContentRepository contentRepository;

    private Studio studio1;
    private Studio studio2;
    private Studio studio3;

    private Content content;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {
        Date now = new Date();

        studio1 = new Studio(
                "Test Studio 1",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "TS1",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "studio-1-external-id"
        );
        this.studioRepository.save(studio1);

        studio2 = new Studio(
                "Test Studio 2",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "TS2",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "studio-2-external-id"
        );
        this.studioRepository.save(studio2);

        studio3 = new Studio(
                "Test Studio 3",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "TS3",
                "https://api.universalinvents.com",
                "INACTIVE",
                null,
                "studio-3-external-id"
        );
        this.studioRepository.save(studio3);

        content = new Content(
                null,
                "Test Content",
                "10101010/0.1",
                "gtm-1",
                studio1
        );
        content.setEidrv("content-eidrv-1");
        content.setStatus("ACTIVE");
        this.contentRepository.save(content);

        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());
    }

    @After
    public void cleanup() throws Exception {
        this.contentRepository.deleteAll();
        this.studioRepository.deleteAll();
    }

    @Test
    public void getStudiosByCodePrefixTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("codePrefix", this.studio1.getCodePrefix()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].codePrefix", is(this.studio1.getCodePrefix())));
    }

    @Test
    public void getStudiosByContactEmailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("contactEmail", this.studio1.getContactEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(3)))
                .andExpect(jsonPath("$.content[0].contactEmail", is(this.studio1.getContactEmail())))
                .andExpect(jsonPath("$.content[1].contactEmail", is(this.studio2.getContactEmail())))
                .andExpect(jsonPath("$.content[2].contactEmail", is(this.studio3.getContactEmail())));
    }

    @Test
    public void getStudiosByContactNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("contactName", this.studio1.getContactName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(3)))
                .andExpect(jsonPath("$.content[0].contactName", is(this.studio1.getContactName())))
                .andExpect(jsonPath("$.content[1].contactName", is(this.studio2.getContactName())))
                .andExpect(jsonPath("$.content[2].contactName", is(this.studio3.getContactName())));
    }

    @Test
    public void getStudiosByContentIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("contentId", this.content.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].contents.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].contents[0].id", is(toIntExact(this.content.getId()))));
    }

    @Test
    public void getStudiosByExternalIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("externalId", this.studio1.getExternalId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].externalId", is(this.studio1.getExternalId())));
    }

    @Test
    public void getStudiosByNameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("name", this.studio1.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(this.studio1.getName())));
    }

    @Test
    public void getStudiosByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.content[1].status", is("ACTIVE")));
    }

    @Test
    public void getStudiosTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios")
                .param("codePrefix", this.studio1.getCodePrefix())
                .param("contactEmail", this.studio1.getContactEmail())
                .param("contactName", this.studio1.getContactName())
                .param("contentId", this.content.getId().toString())
                .param("externalId", this.studio1.getExternalId())
                .param("name", this.studio1.getName())
                .param("status", this.studio1.getStatus()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].codePrefix", is(this.studio1.getCodePrefix())))
                .andExpect(jsonPath("$.content[0].contactEmail", is(this.studio1.getContactEmail())))
                .andExpect(jsonPath("$.content[0].contactName", is(this.studio1.getContactName())))
                .andExpect(jsonPath("$.content[0].contents[0].id", is(toIntExact(this.content.getId()))))
                .andExpect(jsonPath("$.content[0].name", is(this.studio1.getName())))
                .andExpect(jsonPath("$.content[0].status", is(this.studio1.getStatus())));
    }

    @Test
    public void createStudioTest() throws Exception {
        Studio newStudio = new Studio(
                "New Studio",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "NS",
                "https://api.universalinvents.com",
                "ACTIVE",
                null,
                "new-studio-external-id"
        );

        String params = this.buildRequestPayload(newStudio);

        mvc.perform(MockMvcRequestBuilders.post("/api/studios")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newStudio.getName())))
                .andExpect(jsonPath("$.description", is(newStudio.getDescription())))
                .andExpect(jsonPath("$.contactEmail", is(newStudio.getContactEmail())))
                .andExpect(jsonPath("$.contactName", is(newStudio.getContactName())))
                .andExpect(jsonPath("$.contactPhone", is(newStudio.getContactPhone())))
                .andExpect(jsonPath("$.codePrefix", is(newStudio.getCodePrefix())))
                .andExpect(jsonPath("$.logoUrl", is(newStudio.getLogoUrl())))
                .andExpect(jsonPath("$.status", is(newStudio.getStatus().toString())))
                .andExpect(jsonPath("$.externalId", is(newStudio.getExternalId())));

    }

    @Test
    public void getStudiosByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/studios/" + this.studio1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.studio1.getId()))));
    }

    @Test
    public void deleteStudiosByIdTest() throws Exception {
        // Remove any potential dependant content
        this.contentRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/api/studios/" + this.studio1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchStudioTest() throws Exception {
        Studio updateStudio = new Studio(
                "Update Studio",
                "This is a test studio",
                "John Doe",
                "admin1@universalinvents.com",
                "(818) 555-5555",
                "US",
                "https://api.universalinvents1.com",
                "INACTIVE",
                null,
                "update-studio-external-id"
        );

        String params = this.buildRequestPayload(updateStudio);

        mvc.perform(MockMvcRequestBuilders.post("/api/studios")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(updateStudio.getName())))
                .andExpect(jsonPath("$.description", is(updateStudio.getDescription())))
                .andExpect(jsonPath("$.contactEmail", is(updateStudio.getContactEmail())))
                .andExpect(jsonPath("$.contactName", is(updateStudio.getContactName())))
                .andExpect(jsonPath("$.contactPhone", is(updateStudio.getContactPhone())))
                .andExpect(jsonPath("$.codePrefix", is(updateStudio.getCodePrefix())))
                .andExpect(jsonPath("$.logoUrl", is(updateStudio.getLogoUrl())))
                .andExpect(jsonPath("$.status", is(updateStudio.getStatus().toString())))
                .andExpect(jsonPath("$.externalId", is(updateStudio.getExternalId())));

    }

    private String buildRequestPayload(Studio studio) {
        String payload = String.format(
                "{\"contactEmail\": \"%s\", "
                        + "\"contactName\": \"%s\", "
                        + "\"contactPhone\": \"%s\", "
                        + "\"description\": \"%s\", "
                        + "\"logoUrl\": \"%s\", "
                        + "\"name\": \"%s\", "
                        + "\"codePrefix\": \"%s\", "
                        + "\"status\": \"%s\", "
                        + "\"externalId\": \"%s\" }",
                studio.getContactEmail(), studio.getContactName(), studio.getContactPhone(),
                studio.getDescription(), studio.getLogoUrl(), studio.getName(), studio.getCodePrefix(),
                studio.getStatus(), studio.getExternalId()
        );
        return payload;
    }
}
