package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest {

    @Inject
    @InjectMocks
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

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
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("uuidTo2", Arrays.asList("sqlcmd"));
        expected.put("uuidTo1", Arrays.asList("teems","sqlcmd"));

        List<Keeper> listActiveKeepers = new ArrayList<>();
        Keeper activeKeeper1 = new Keeper("uuidFrom1", "uuidTo1", "teems", new Date());
        Keeper activeKeeper2 = new Keeper("uuidFrom2", "uuidTo1", "sqlcmd", new Date());
        Keeper activeKeeper3 = new Keeper("uuidFrom1", "uuidTo2", "sqlcmd", new Date());
        listActiveKeepers.add(activeKeeper1);
        listActiveKeepers.add(activeKeeper2);
        listActiveKeepers.add(activeKeeper3);

        //When
        when(repository.getActiveKeepers()).thenReturn(listActiveKeepers);

        Map<String, List<String>> mapActiveKeepers = new HashMap(){};
        for (Keeper keeper : listActiveKeepers) {
            List<String> directions = new ArrayList<>();
            if(mapActiveKeepers.containsKey(keeper.getUuid())) {
                directions = mapActiveKeepers.get(keeper.getUuid());
                directions.add(keeper.getDirection());
                mapActiveKeepers.replace(keeper.getUuid(),directions);
            }else{
                directions.add(keeper.getDirection());
                mapActiveKeepers.put(keeper.getUuid(), directions);
            }
        }

        //Then
        assertEquals(expected, mapActiveKeepers);
    }
}