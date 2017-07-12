package juja.microservices.keepers.service;

import juja.microservices.common.KeeperAbstractTest;
import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.exception.KeeperNonexistentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.*;

import static juja.microservices.common.TestUtils.reflectionEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest extends KeeperAbstractTest {
    private static final String UUID = "00001111";

    @Inject
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

    @Test(expected = KeeperAccessException.class)
    public void inactiveKeeperWithKeeperAccessExceptionTest() {
        when(repository.findOneActive(anyString())).thenReturn(null);

        service.inactiveKeeper(new KeeperRequest("from", "uuid", "direction"));
    }

    @Test(expected = KeeperNonexistentException.class)
    public void inactiveKeeperWithKeeperNonexistentExceptionTest() {
        when(repository.findOneActive(anyString())).thenReturn(createKeeper().withId("uuid").create());
        when(repository.findOneByUUIdAndDirectionIsActive(anyString(), anyString())).thenReturn(null);

        service.inactiveKeeper(new KeeperRequest("from", "uuid", "direction"));
    }

    @Test
    public void inactiveKeeperSuccessTest() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        Keeper keeper = createKeeper().withId("uuid").create();
        when(repository.findOneActive(anyString())).thenReturn(createKeeper().withId("uuid").create());
        when(repository.findOneByUUIdAndDirectionIsActive(anyString(), anyString())).thenReturn(keeper);
        when(repository.inactive(keeper)).thenReturn("uuid");

        List<String> actual = service.inactiveKeeper(keeperRequest);
        assertEquals(Collections.singletonList("uuid"), actual);

        verify(repository).findOneActive(eq(keeperRequest.getFrom()));
        verify(repository).findOneByUUIdAndDirectionIsActive(eq(keeperRequest.getUuid()), eq(keeperRequest.getDirection()));
        verify(repository).inactive(argThat(reflectionEqual(keeper)));
        verifyNoMoreInteractions(repository);
    }

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
    public void addKeeperWithKeeperAccessException() {
        //Given
        when(repository.findOneByUUId(anyString())).thenReturn(null);

        //When
        service.addKeeper(new KeeperRequest("123qwe", "asdqwe", "teems"));
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperWithKeeperDirectionActiveException() {
        //Given
        Keeper keeper = new Keeper("some", "some", "some", new Date());
        when(repository.findOneByUUId(anyString())).thenReturn(keeper);
        when(repository.findOneByUUIdAndDirectionIsActive(anyString(), anyString())).thenReturn(keeper);

        //When
        service.addKeeper(new KeeperRequest("123qwe", "asdqwe", "teems"));
    }

    @Test
    public void addKeeper() {
        //Given
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).atZone(ZoneId.systemDefault()).toInstant());
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
