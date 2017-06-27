package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import org.mockito.InjectMocks;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.Matchers.anyString;

/**
 * @author Dmitriy Lyashenko
 */

@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest {

    private static final String UUID = "00001111";

    @Inject
    @InjectMocks
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

    @Test
    public void getDirectionsTest() {
        //Given
        List<String> expectedList = Arrays.asList("direction1", "direction2", "direction3");
        //When
        when(repository.getDirections(UUID)).thenReturn(expectedList);
        List<String> actualList = service.getDirections(UUID);
        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void getDirectionsWithEmptyResultTest() {
        //Given
        List<String> expectedList = new ArrayList<>();
        //When
        when(repository.getDirections(UUID)).thenReturn(expectedList);
        List<String> actualList = service.getDirections(UUID);
        //Then
        assertTrue(actualList.isEmpty());
    }

    @Test(expected = KeeperAccessException.class)
    public void addKeeperWithKeeperAccessException(){
        //Given
        when(repository.findOneByUUId(anyString())).thenReturn(null);

        //When
        service.addKeeper(new KeeperRequest("123qwe", "asdqwe", "teems"));
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperWithKeeperDirectionActiveException(){
        //Given
        Keeper keeper = new Keeper("some", "some", "some", new Date());
        when(repository.findOneByUUId(anyString())).thenReturn(keeper);
        when(repository.findOneByUUIdAndDirectionIsActive(anyString(), anyString())).thenReturn(keeper);

        //When
        service.addKeeper(new KeeperRequest("123qwe", "asdqwe", "teems"));
    }

    @Test
    public void addKeeper(){
        //Given
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12,0).atZone(ZoneId.systemDefault()).toInstant());
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);
        when(repository.findOneByUUId(anyString())).thenReturn(keeper);
        when(repository.save(keeperRequest)).thenReturn("SomeID");
        String expected = "SomeID";

        //When
        String result = service.addKeeper(keeperRequest);

        //Then
        assertEquals(expected, result);
    }
}
