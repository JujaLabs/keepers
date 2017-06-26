package juja.microservices.keepers.controller;

import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.service.KeepersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import javax.inject.Inject;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

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

    @Inject
    @InjectMocks
    private KeepersController controller;

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
    public void addKeeper() throws Exception {
        //Given
        String jsonContentRequest = "{\"from\":\"asdqwe\",\"uuid\":\"max\", \"direction\":\"SomeDirection\"}";

        //When
        when(service.addKeeper(any(KeeperRequest.class))).thenReturn("SomeId");
        String result = getResult("/keepers", jsonContentRequest);

        //Then
        assertEquals("[\"SomeId\"]", result);
    }

    private String getResult(String uri, String jsonContentRequest) throws Exception {
        return mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test(expected = KeeperAccessException.class)
    public void addKeeperBadRequest(){
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new KeeperAccessException("Only the keeper can appoint another keeper"));

        //When
        controller.addKeeper(keeperRequest);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperBadRequestOther(){
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenThrow(
            new KeeperDirectionActiveException("Keeper with uuid " + keeperRequest.getUuid() + " already keep direction "
                + keeperRequest.getDirection() + " and he is active"));

        //When
        controller.addKeeper(keeperRequest);
    }
}
