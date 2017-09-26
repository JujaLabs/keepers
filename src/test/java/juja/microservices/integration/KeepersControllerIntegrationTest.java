package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
public class KeepersControllerIntegrationTest extends BaseIntegrationTest {

    private static final String JSON_CONTENT_REQ = "{\"from\":\"asdqwe\",\"uuid\":\"max\", \"direction\":\"SomeDirection\"}";
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Value("${keepers.endpoint.addKeeper}")
    private String keepersAddKeeperUrl;
    @Value("${keepers.endpoint.deactivateKeeper}")
    private String keepersDeactivateKeeperUrl;
    @Value("${keepers.endpoint.getDirections}")
    private String keepersGetDirectionsUrl;
    @Value("${keepers.endpoint.getActiveKeepers}")
    private String keepersGetActiveKeepersUrl;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/severalKeepers.json")
    public void deactivateKeeperSuccessTest() throws Exception {
        mockMvc.perform(put(keepersDeactivateKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CONTENT_REQ))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/deactivateKeeper.json")
    public void deactivateKeeperWithKeeperAccessExceptionTest() throws Exception {
        String expected = "{" +
                "\"httpStatus\":400," +
                "\"internalErrorCode\":\"KPR-F1-D4\"," +
                "\"clientMessage\":\"Sorry, but you're not a keeper\"," +
                "\"developerMessage\":\"Exception - KeeperAccessException\"," +
                "\"exceptionMessage\":\"Request 'deactivate keeper' rejected. User 'asdqwe' tried to deactivate keeper, but he is not an active keeper\"," +
                "\"detailErrors\":[]}";

        String result = mockMvc.perform(put(keepersDeactivateKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CONTENT_REQ))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void deactivateKeeperWithKeeperNonexistentExceptionTest() throws Exception {
        String expected = "{" +
                "\"httpStatus\":400," +
                "\"internalErrorCode\":\"KPR-F2\"," +
                "\"clientMessage\":\"Sorry, but keeper with uuid and direction is't exist or inactive\"," +
                "\"developerMessage\":\"Exception - KeeperNonexistentException\"," +
                "\"exceptionMessage\":\"Keeper with uuid 'max' and direction 'SomeDirection' is't exist or not active\"," +
                "\"detailErrors\":[]}";

        String result = mockMvc.perform(put(keepersDeactivateKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CONTENT_REQ))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperNotExistUUID() throws Exception {
        String expected = "{" +
                "\"httpStatus\":400," +
                "\"internalErrorCode\":\"KPR-F1-D4\"," +
                "\"clientMessage\":\"Sorry, but you're not a keeper\"," +
                "\"developerMessage\":\"Exception - KeeperAccessException\"," +
                "\"exceptionMessage\":\"Request 'add keeper' rejected. User 'bill' tried to add new keeper, but he is not an active keeper\"," +
                "\"detailErrors\":[]}";

        String json = "{" +
                "  \"from\":\"bill\"," +
                "  \"uuid\":\"max\"," +
                "  \"direction\":\"SomeDirection\"" +
                "}";

        String result = mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperAlreadyKeepDirectionIsActive() throws Exception {
        String expected = "{" +
                "\"httpStatus\":400," +
                "\"internalErrorCode\":\"KPR-F1-D4\"," +
                "\"clientMessage\":\"Sorry, but keeper with the requested uuid already keeps the requested direction and he is active\"," +
                "\"developerMessage\":\"Exception - KeeperDirectionActiveException\"," +
                "\"exceptionMessage\":\"Keeper with uuid 'asdqwe' already keeps direction 'teams' and he is active\"," +
                "\"detailErrors\":[]}";

        String json = "{" +
                "  \"from\":\"asdqwe\"," +
                "  \"uuid\":\"asdqwe\"," +
                "  \"direction\":\"teams\"" +
                "}";

        String result = mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperOk() throws Exception {
        String json = "{" +
                "  \"from\":\"asdqwe\"," +
                "  \"uuid\":\"max\"," +
                "  \"direction\":\"SomeDirection\"" +
                "}";

        mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addKeeperInEmptyDb() throws Exception {
        String json = "{" +
                "  \"from\":\"bob\"," +
                "  \"uuid\":\"max\"," +
                "  \"direction\":\"SomeDirection\"" +
                "}";

        mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void getDirectionsAndReturnJson() throws Exception {
        String expected = "[\"First active direction\",\"Second active direction\"]";
        String uuid="0000c9999";
        mockMvc.perform(get(keepersGetDirectionsUrl+uuid)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(keepersGetDirectionsUrl+uuid)
                .contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(expected, content);
    }
}
