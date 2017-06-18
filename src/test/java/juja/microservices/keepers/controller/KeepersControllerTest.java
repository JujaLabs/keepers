package juja.microservices.keepers.controller;

import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.AddKeeperException;
import juja.microservices.keepers.service.KeepersService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import java.util.Collections;

import static org.mockito.Mockito.when;

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

    @Inject
    @InjectMocks
    KeepersController keepersController = new KeepersController();

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
    public void addKeeper(){
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenReturn("SomeId");
        ResponseEntity<?> expected = new ResponseEntity<>(Collections.singletonList("SomeId"), HttpStatus.OK);

        //When
        ResponseEntity<?> result = keepersController.addKeeper(keeperRequest);

        //Then
        Assert.assertEquals(expected, result);
    }

    @Test(expected = AddKeeperException.class)
    public void addKeeperBadRequest(){
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        when(service.addKeeper(keeperRequest)).thenThrow(
                new AddKeeperException("Only the keeper can appoint another keeper"));

        //When
        keepersController.addKeeper(keeperRequest);
    }
}
