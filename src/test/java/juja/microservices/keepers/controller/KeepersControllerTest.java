package juja.microservices.keepers.controller;

import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.service.KeepersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static juja.microservices.common.TestUtils.toJson;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersController.class)
public class KeepersControllerTest {

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private KeepersService service;

    @Inject
    private KeepersController controller;

    @Test
    public void deactivateKeeperBadRequestTest() throws Exception {
        //given
        String badJsonRequest = "{\"from\":\"admin\",\"direction\":\"LMS\"}";

        //then
        mockMvc.perform(put("/keepers")
                .contentType(APPLICATION_JSON_UTF8)
                .content(badJsonRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deactivateKeeperSuccessTest() throws Exception {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        List<String> ids = new ArrayList<>();
        ids.add("uuid");
        when(service.deactivateKeeper(any(KeeperRequest.class))).thenReturn(ids);

        String actual = mockMvc.perform(put("/keepers")
                .contentType(APPLICATION_JSON_UTF8)
                .content(toJson(keeperRequest)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[\"uuid\"]", actual);
    }

    @Test
    public void addKeeperTest() throws Exception {
        //given
        String jsonContentRequest = "{\"from\":\"admin\",\"direction\":\"LMS\"}";

        //then
        checkBadRequest(jsonContentRequest);
    }

    private void checkBadRequest(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/keepers")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getKeeperDirections() throws Exception {
        //Given
        final String expectedString = "[\"direction1\",\"direction2\",\"direction3\"]";
        List<String> expectedList = Arrays.asList("direction1", "direction2", "direction3");
        //When
        when(service.getDirections("0000c9999")).thenReturn(expectedList);
        String result = mockMvc.perform(get("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        assertEquals(expectedString, result);
    }

    @Test
    public void getKeeperDirectionsWithEmptyResult() throws Exception {
        //Given
        final String expectedString = "[]";
        List<String> expectedList = new ArrayList<>();
        //When
        when(service.getDirections("0000c9999")).thenReturn(expectedList);
        String result = mockMvc.perform(get("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        assertEquals(expectedString, result);
    }

    @Test()
    public void getHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void addKeeper() throws Exception {
        //Given
        String jsonContentRequest = "{\"from\":\"asdqwe\",\"uuid\":\"max\", \"direction\":\"SomeDirection\"}";

        //When
        when(service.addKeeper(any(KeeperRequest.class))).thenReturn("SomeId");
        String result = getResult(jsonContentRequest);

        //Then
        assertEquals("[\"SomeId\"]", result);
    }

    private String getResult(String jsonContentRequest) throws Exception {
        return mockMvc.perform(post("/keepers")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test(expected = KeeperAccessException.class)
    public void addKeeperBadRequest() {
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new KeeperAccessException("Only the keeper can appoint another keeper"));

        //When
        controller.addKeeper(keeperRequest);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperBadRequestOther() {
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new KeeperDirectionActiveException("Keeper with uuid " + keeperRequest.getUuid() + " already keeps direction "
                        + keeperRequest.getDirection() + " and he is active"));

        //When
        controller.addKeeper(keeperRequest);
    }
}
