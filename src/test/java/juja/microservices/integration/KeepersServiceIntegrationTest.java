package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.ActiveKeeperDTO;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.exception.KeeperNonexistentException;
import juja.microservices.keepers.service.KeepersService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Lyashenko
 * @author Vadim Dyachenko
 */
@RunWith(SpringRunner.class)
public class KeepersServiceIntegrationTest extends BaseIntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private KeepersRepository repository;

    @Inject
    private KeepersService service;

    @Test(expected = KeeperAccessException.class)
    @UsingDataSet(locations = "/datasets/deactivateKeeper.json")
    public void deactivateKeeperWithKeeperAccessExceptionTest() {
        KeeperRequest request = new KeeperRequest("asdqwe", "max", "teams");

        service.deactivateKeeper(request);
    }

    @Test(expected = KeeperNonexistentException.class)
    @UsingDataSet(locations = "/datasets/severalKeepers.json")
    public void deactivateKeeperWithKeeperNonexistentExceptionTest() {
        KeeperRequest request = new KeeperRequest("asdqwe", "max", "teams");

        service.deactivateKeeper(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/severalKeepers.json")
    public void deactivateKeeperSuccessTest() {
        service.deactivateKeeper(new KeeperRequest("asdqwe", "max", "SomeDirection"));
        String result = repository.findOneActive("asdqwe").getUuid();

        assertNull(repository.findOneActive("max"));
        assertNotNull(result);
    }

    @Test(expected = KeeperAccessException.class)
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperNotExistKeeper() {
        KeeperRequest request = new KeeperRequest("123qwe", "asdqwe", "teams");

        service.addKeeper(request);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperWhichKeeperAlreadyKeepDirectionIsAlive() {
        KeeperRequest request = new KeeperRequest("asdqwe", "asdqwe", "teams");

        service.addKeeper(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeper() {
        service.addKeeper(new KeeperRequest("asdqwe", "00000", "codenjoy"));

        Keeper keeper = repository.findOneActive("00000");

        assertNotNull(keeper);
        assertEquals("asdqwe", keeper.getFrom());
        assertEquals("00000", keeper.getUuid());
        assertEquals("codenjoy", keeper.getDirection());
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnDirections() {
        List<String> expected = Arrays.asList("First active direction", "Second active direction");

        List<String> actual = service.getDirections("0000c9999");

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnEmptyList() {
        List<String> actual = service.getDirections("1111a9999");

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    @UsingDataSet(locations = "/datasets/getActiveKeepers.json")
    public void getActiveKeepers() {

        ActiveKeeperDTO firstKeeperDTO = new ActiveKeeperDTO("1111a9999", Arrays.asList("First direction"));
        ActiveKeeperDTO secondKeeperDTO = new ActiveKeeperDTO("0000c9999", Arrays.asList("First direction", "Second direction"));

        List<ActiveKeeperDTO> actual = service.getActiveKeepers();

        assertNotNull(actual);
        assertTrue(actual.contains(firstKeeperDTO));
        assertTrue(actual.contains(secondKeeperDTO));
    }
}