package juja.microservices.keepers.controller;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vadim Dyachenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersController.class)
public class KeepersControllerTest {
    @Inject
    private MockMvc mockMvc;

    @MockBean
    private KeepersService service;

    @Test
    public void addKeeperTest() throws Exception {
        //given
        String jsonContentRequest ="{\"from\":\"admin\",\"direction\":\"LMS\"}";

        //then
        checkBadRequest("/keepers", jsonContentRequest);
    }

    private void checkBadRequest(String uri, String jsonContentRequest) throws Exception {
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getKeeperDirections() throws Exception {
        final String expectedString = "[\"direction1\",\"direction2\",\"direction3\"]";
        List<String> expectedList = Arrays.asList("direction1", "direction2", "direction3");

        when(service.getDirections("0000c9999")).thenReturn(expectedList);
        String result = mockMvc.perform(get("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(expectedString, result);
    }

    @Test
    public void getKeeperDirectionsWithEmptyResult() throws Exception {
        final String expectedString = "[]";
        List<String> expectedList = new ArrayList<>();

        when(service.getDirections("0000c9999")).thenReturn(expectedList);
        String result = mockMvc.perform(get("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(expectedString, result);
    }

    @Test()
    public void getHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post("/keepers/0000c9999")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isMethodNotAllowed());
    }
}
