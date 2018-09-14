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

import java.math.BigDecimal;
import java.util.Date;

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ContentIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private StudioRepository studioRepository;

    private Studio studio;
    private Studio studio2;

    private Content content;
    private Content content2;
    private Content content3;

    private MediaType contentType;

    @Before
    public void setup() throws Exception {

        //Setup a dummy studio
        Date now = new Date();
        studio = new Studio(
                "Test Studio",
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
        this.studioRepository.save(studio);

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

        // Create content1
        content = new Content(
                null,
                "Test Content",
                "10101010/0.1",
                "gtm-1",
                this.studio
        );
        content.setEidrv("content-eidrv-1");
        content.setStatus("ACTIVE");
        this.contentRepository.save(content);

        // Create content2
        content2 = new Content(
                null,
                "Test Content",
                "10101010/0.2",
                "gtm-2",
                this.studio
        );
        content2.setEidrv("content-eidrv-2");
        content2.setStatus("ACTIVE");
        this.contentRepository.save(content2);

        // Create content3
        content3 = new Content(
                null,
                "Test Content 3",
                "10101010/0.3",
                "gtm-3",
                this.studio2
        );
        content3.setEidrv("content-eidrv-3");
        content3.setStatus("INACTIVE");
        this.contentRepository.save(content3);


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
    public void getContentsByEidrTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("eidr", this.content.getEidr()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].eidr", is(this.content.getEidr())));
    }

    @Test
    public void getContentsByEidrvTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("eidrv", this.content.getEidrv()))
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eidrv", is(this.content.getEidrv())));
    }

    @Test
    public void getContentsByStatusTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("status", this.content.getStatus()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].status", is(this.content.getStatus())))
                .andExpect(jsonPath("$.content[1].status", is(this.content2.getStatus())));
    }

    @Test
    public void getContentsByStudioIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("studioId", this.content.getStudio().getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].studio.id", is(toIntExact(this.content.getStudio().getId()))))
                .andExpect(jsonPath("$.content[1].studio.id", is(toIntExact(this.content2.getStudio().getId()))));
    }

    @Test
    public void getContentsByTitleTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("title", this.content.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is(this.content.getTitle())))
                .andExpect(jsonPath("$.content[1].title", is(this.content2.getTitle())));
    }

    @Test
    public void getContentsTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents")
                .param("eidr", this.content.getEidr())
                .param("eidrv", this.content.getEidrv())
                .param("gtm", this.content.getGtm())
                .param("status", this.content.getStatus())
                .param("studioId", this.content.getStudio().getId().toString())
                .param("title", this.content.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.*", hasSize(1)))
                .andExpect(jsonPath("$.content[0].eidr", is(this.content.getEidr())))
                .andExpect(jsonPath("$.content[0].eidrv", is(this.content.getEidrv())))
                .andExpect(jsonPath("$.content[0].gtm", is(this.content.getGtm())))
                .andExpect(jsonPath("$.content[0].status", is(this.content.getStatus())))
                .andExpect(jsonPath("$.content[0].studio.id", is(toIntExact(this.content.getStudio().getId()))))
                .andExpect(jsonPath("$.content[0].title", is(this.content.getTitle())));
    }

    @Test
    public void createContentTest() throws Exception {
        Content newContent = new Content();
        newContent.setEidr("10101010/1.01");
        newContent.setEidrv("v1");
        newContent.setGtm("392");
        newContent.setMsrp(new BigDecimal(9.99));
        newContent.setStatus("ACTIVE");
        newContent.setStudio(this.studio);
        newContent.setTitle("Create Test Content");

        String params = this.buildRequestPayload(newContent);

        mvc.perform(MockMvcRequestBuilders.post("/api/contents")
                .contentType(contentType)
                .content(params))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eidr", is(newContent.getEidr())))
                .andExpect(jsonPath("$.eidrv", is(newContent.getEidrv())))
                .andExpect(jsonPath("$.gtm", is(newContent.getGtm())))
                .andExpect(jsonPath("$.msrp", is(newContent.getMsrp().doubleValue())))
                .andExpect(jsonPath("$.status", is(newContent.getStatus())))
                .andExpect(jsonPath("$.studio.id", is(toIntExact(newContent.getStudio().getId()))))
                .andExpect(jsonPath("$.title", is(newContent.getTitle())));
    }

    @Test
    public void getContentsByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/contents/" + this.content.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.content.getId()))));
    }


    @Test
    public void deleteContentTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/contents/" + this.content.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchContentTest() throws Exception {
        Content updateContent = new Content();
        updateContent.setEidrv("v2");
        updateContent.setGtm("gtm2");
        updateContent.setMsrp(new BigDecimal(1.99));
        updateContent.setStatus("INACTIVE");
        updateContent.setStudio(this.studio2);
        updateContent.setTitle("Update Title");

        String params = this.buildRequestPayload(updateContent);

        mvc.perform(MockMvcRequestBuilders.patch("/api/contents/" + this.content.getId())
                .contentType(contentType)
                .content(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eidrv", is(updateContent.getEidrv())))
                .andExpect(jsonPath("$.gtm", is(updateContent.getGtm())))
                .andExpect(jsonPath("$.msrp", is(updateContent.getMsrp().doubleValue())))
                .andExpect(jsonPath("$.status", is(updateContent.getStatus())))
                .andExpect(jsonPath("$.studio.id", is(toIntExact(updateContent.getStudio().getId()))))
                .andExpect(jsonPath("$.title", is(updateContent.getTitle())));
    }

    private String buildRequestPayload(Content content) {
        String payload = String.format(
                "{\"eidr\": \"%s\", "
                        + "\"eidrv\": \"%s\", "
                        + "\"gtm\": \"%s\", "
                        + "\"msrp\": %3.2f, "
                        + "\"studioId\": %d, "
                        + "\"title\": \"%s\", "
                        + "\"status\": \"%s\"}",
                content.getEidr(), content.getEidrv(), content.getGtm(), content.getMsrp().doubleValue(),
                content.getStudio().getId(), content.getTitle(), content.getStatus()
        );
        return payload;
    }

}
