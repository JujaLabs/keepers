package juja.microservices.keepers.service;

import juja.microservices.common.KeeperAbstractTest;
import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.ActiveKeeperDTO;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.exception.KeeperNonexistentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 * @author Petrohalko Oleksii
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
    public void deactivateKeeperWithKeeperAccessExceptionTest() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(null);

        service.deactivateKeeper(keeperRequest);

        verify(repository).findOneActive(keeperRequest.getFrom());
        verifyNoMoreInteractions(repository);

    }

    @Test(expected = KeeperNonexistentException.class)
    public void deactivateKeeperWithKeeperNonexistentExceptionTest() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(createKeeper().withId("uuid").create());
        when(repository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()))
                .thenReturn(null);

        service.deactivateKeeper(keeperRequest);

        verify(repository).findOneActive(keeperRequest.getFrom());
        verify(repository).findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deactivateKeeperSuccessTest() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        Keeper keeper = createKeeper().withId("uuid").withUuid("1").create();
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(keeper);
        when(repository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()))
                .thenReturn(keeper);
        when(repository.save(keeper)).thenReturn("uuid");

        List<String> actual = service.deactivateKeeper(keeperRequest);
        assertEquals(Collections.singletonList("uuid"), actual);

        verify(repository).findOneActive(keeperRequest.getFrom());
        verify(repository).findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection());
        verify(repository).save(keeper);
        verifyNoMoreInteractions(repository);

    }

    @Test
    public void getDirectionsTest() {
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

        List<String> actualList = service.getDirections(UUID);

        assertEquals(expectedList, actualList);
        verify(repository).getDirections(UUID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getDirectionsWithEmptyResultTest() {
        when(repository.getDirections(UUID)).thenReturn(new ArrayList<>());

        List<String> actualList = service.getDirections(UUID);

        assertTrue(actualList.isEmpty());
        verify(repository).getDirections(UUID);
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = KeeperAccessException.class)
    public void addKeeperWithKeeperAccessException() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(null);

        service.addKeeper(keeperRequest);

        verify(repository).findOneActive(keeperRequest.getFrom());
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    public void addKeeperWithKeeperDirectionActiveException() {
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        Keeper keeper = new Keeper("from", "uuid", "direction", new Date());
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(keeper);
        when(repository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()))
                .thenReturn(keeper);

        service.addKeeper(keeperRequest);

        verify(repository).findOneActive(keeperRequest.getFrom());
        verify(repository).findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addKeeper() {
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).atZone(ZoneId.systemDefault()).toInstant());
        KeeperRequest keeperRequest = new KeeperRequest("from", "uuid", "direction");
        Keeper keeper = new Keeper("from", "uuid", "direction", startDate);
        when(repository.findOneActive(keeperRequest.getFrom())).thenReturn(keeper);
        when(repository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()))
                .thenReturn(null);
        when(repository.save(any(Keeper.class))).thenReturn("SomeID");
        String expected = "SomeID";

        String result = service.addKeeper(keeperRequest);

        assertEquals(expected, result);
        verify(repository).findOneActive(keeperRequest.getFrom());
        verify(repository).findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection());
        verify(repository).save(any(Keeper.class));
        verifyNoMoreInteractions(repository);

    }

    @Test
    public void getActiveKeepers() {
        List<ActiveKeeperDTO> expected = new ArrayList<>();
        expected.add(new ActiveKeeperDTO("uuidTo2", Collections.singletonList("sqlcmd")));
        expected.add(new ActiveKeeperDTO("uuidTo1", Arrays.asList("teems", "sqlcmd")));
        List<Keeper> listActiveKeepers = new ArrayList<>();
        Keeper activeKeeper1 = new Keeper("uuidFrom1", "uuidTo1", "teems", new Date());
        Keeper activeKeeper2 = new Keeper("uuidFrom2", "uuidTo1", "sqlcmd", new Date());
        Keeper activeKeeper3 = new Keeper("uuidFrom1", "uuidTo2", "sqlcmd", new Date());
        listActiveKeepers.add(activeKeeper1);
        listActiveKeepers.add(activeKeeper2);
        listActiveKeepers.add(activeKeeper3);
        when(repository.getActiveKeepers()).thenReturn(listActiveKeepers);

        List<ActiveKeeperDTO> actual = service.getActiveKeepers();

        assertEquals(expected, actual);
        verify(repository).getActiveKeepers();
        verifyNoMoreInteractions(repository);
    }
}

