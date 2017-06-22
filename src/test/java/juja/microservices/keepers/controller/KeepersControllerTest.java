package juja.microservices.keepers.controller;

import juja.microservices.keepers.service.KeepersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
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
}
