package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.ActiveKeeperDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.mockito.Matchers.anyString;

/**
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest {

    private static final String UUID = "00001111";

    @Inject
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

    @Test
    public void getDirectionsTest() {
        //Given
        List<Keeper> keepers = new ArrayList<>();
        keepers.add(new Keeper("1",
                "0000c9999",
                "direction1",
                null)
        );
        keepers.add(new Keeper("2",
                "0000c9999",
                "direction2",
                null)
        );
        when(repository.getDirections(UUID)).thenReturn(keepers);
        List<String> expectedList = new ArrayList<>();
        for (Keeper keeper : keepers) {
            expectedList.add(keeper.getDirection());
        }
        //When
        List<String> actualList = service.getDirections(UUID);
        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void getDirectionsWithEmptyResultTest() {
        //When
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
    @Test
    public void getActiveKeepers(){
        //Given
        List<ActiveKeeperDTO> expected = new ArrayList<>();
        expected.add(new ActiveKeeperDTO("uuidTo2", Arrays.asList("sqlcmd")));
        expected.add(new ActiveKeeperDTO("uuidTo1", Arrays.asList("teems","sqlcmd")));

        List<Keeper> listActiveKeepers = new ArrayList<>();
        Keeper activeKeeper1 = new Keeper("uuidFrom1", "uuidTo1", "teems", new Date());
        Keeper activeKeeper2 = new Keeper("uuidFrom2", "uuidTo1", "sqlcmd", new Date());
        Keeper activeKeeper3 = new Keeper("uuidFrom1", "uuidTo2", "sqlcmd", new Date());
        listActiveKeepers.add(activeKeeper1);
        listActiveKeepers.add(activeKeeper2);
        listActiveKeepers.add(activeKeeper3);

        //When
        when(repository.getActiveKeepers()).thenReturn(listActiveKeepers);

        //Then
        assertEquals(expected, service.getActiveKeepers());
    }
}

