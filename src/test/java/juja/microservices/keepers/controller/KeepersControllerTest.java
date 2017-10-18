package juja.microservices.keepers.controller;

import juja.microservices.keepers.entity.ActiveKeeperDTO;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.service.KeepersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static juja.microservices.common.TestUtils.toJson;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    @Value("${keepers.endpoint.addKeeper}")
    private String keepersAddKeeperUrl;
    @Value("${keepers.endpoint.deactivateKeeper}")
    private String keepersDeactivateKeeperUrl;
    @Value("${keepers.endpoint.getDirections}")
    private String keepersGetDirectionsUrl;
    @Value("${keepers.endpoint.getActiveKeepers}")
    private String keepersGetActiveKeepersUrl;

    @Test
    public void deactivateKeeperBadRequestTest() throws Exception {
        String badJsonRequest = "{\"from\":\"admin\",\"direction\":\"LMS\"}";

        mockMvc.perform(put(keepersDeactivateKeeperUrl)
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
        when(service.deactivateKeeper(eq(keeperRequest))).thenReturn(ids);

        String actual = mockMvc.perform(put(keepersDeactivateKeeperUrl)
                .contentType(APPLICATION_JSON_UTF8)
                .content(toJson(keeperRequest)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[\"uuid\"]", actual);
        verify(service).deactivateKeeper(eq(keeperRequest));
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addKeeperTest() throws Exception {
        String jsonContentRequest = "{\"from\":\"admin\",\"direction\":\"LMS\"}";

        checkBadRequest(jsonContentRequest);
    }

    private void checkBadRequest(String jsonContentRequest) throws Exception {
        mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getKeeperDirections() throws Exception {
        final String expectedString = "[\"direction1\",\"direction2\",\"direction3\"]";
        List<String> expectedList = Arrays.asList("direction1", "direction2", "direction3");
        String uuid = "0000c9999";
        when(service.getDirections(uuid)).thenReturn(expectedList);

        String result = mockMvc.perform(get(keepersGetDirectionsUrl + uuid)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expectedString, result);
        verify(service).getDirections(uuid);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void getKeeperDirectionsWithEmptyResult() throws Exception {
        final String expectedString = "[]";
        List<String> expectedList = new ArrayList<>();
        String uuid = "0000c9999";
        when(service.getDirections(uuid)).thenReturn(expectedList);

        String result = mockMvc.perform(get(keepersGetDirectionsUrl + uuid)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expectedString, result);
        verify(service).getDirections(uuid);
        verifyNoMoreInteractions(service);
    }

    @Test()
    public void getHttpRequestMethodNotSupportedException() throws Exception {
        String uuid = "0000c9999";
        mockMvc.perform(post(keepersGetDirectionsUrl + uuid)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void addKeeper() throws Exception {
        KeeperRequest keeperRequest = new KeeperRequest("asdqwe", "max", "SomeDirection");
        List<String> id = new ArrayList<>();
        id.add("SomeId");
        when(service.addKeeper(eq(keeperRequest))).thenReturn(id);

        String result = mockMvc.perform(post(keepersAddKeeperUrl)
                .contentType(APPLICATION_JSON_UTF8)
                .content(toJson(keeperRequest)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals("[\"SomeId\"]", result);
        verify(service).addKeeper(eq(keeperRequest));
        verifyNoMoreInteractions(service);
    }

    @Test(expected = KeeperAccessException.class)
    public void addKeeperBadRequest() {
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teams");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new KeeperAccessException("Only the keeper can appoint another keeper"));

        controller.addKeeper(keeperRequest);

        verify(service).addKeeper(keeperRequest);
        verifyNoMoreInteractions(service);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperBadRequestOther() {
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teams");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new KeeperDirectionActiveException("Keeper with uuid " + keeperRequest.getUuid() + " already keeps direction "
                        + keeperRequest.getDirection() + " and he is active"));

        controller.addKeeper(keeperRequest);
        verify(service).addKeeper(keeperRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void getActiveKeepers() throws Exception {
        List<ActiveKeeperDTO> list = new ArrayList<>();
        list.add(new ActiveKeeperDTO("uuidTo2", Collections.singletonList("sqlcmd")));
        list.add(new ActiveKeeperDTO("uuidTo1", Arrays.asList("teams", "sqlcmd")));
        when(service.getActiveKeepers()).thenReturn(list);
        String expected = "[{\"uuid\":\"uuidTo2\",\"directions\":[\"sqlcmd\"]},{\"uuid\":\"uuidTo1\"," +
                "\"directions\":[\"teams\",\"sqlcmd\"]}]";

        String result = mockMvc.perform(get(keepersGetActiveKeepersUrl)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
        verify(service).getActiveKeepers();
        verifyNoMoreInteractions(service);
    }
}
