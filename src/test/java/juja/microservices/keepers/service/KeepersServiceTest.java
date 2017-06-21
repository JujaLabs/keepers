package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest {
    private static final String UUID = "0000-0000";

    @Inject
    private MockMvc mockMvc;

    @Inject
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

    @Test
    public void getDirectionsTest() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("direction1");
        expectedList.add("direction2");
        expectedList.add("direction3");
        when(repository.getDirections(UUID)).thenReturn(expectedList);
        List<String> actualList = service.getDirections(UUID);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void getDirectionsWithEmptyResultTest() {
        List<String> expectedList = new ArrayList<>();
        when(repository.getDirections(UUID)).thenReturn(expectedList);
        List<String> actualList = service.getDirections(UUID);
        assertTrue(actualList.isEmpty());
    }
}
