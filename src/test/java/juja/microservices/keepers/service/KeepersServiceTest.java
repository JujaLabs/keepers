package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
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
import java.util.Date;
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