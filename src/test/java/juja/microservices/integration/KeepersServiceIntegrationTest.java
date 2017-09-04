package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.keepers.dao.KeepersRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitriy Lyashenko
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
        service.addKeeper(new KeeperRequest("asdqwe", "123qwe", "teams"));
        String result = repository.findOneByUUId("asdqwe").getUuid();
        String result2 = repository.findOneByUUId("123qwe").getUuid();

        assertNotNull(result);
        assertNotNull(result2);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnDirections() {
        List<String> expectedList = Arrays.asList("First active direction", "Second active direction");

        List<Keeper> keepers = repository.getDirections("0000c9999");
        List<String> actualList = new ArrayList<>();
        for (Keeper keeper : keepers) {
            actualList.add(keeper.getDirection());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnCorrectSize() {
        List<Keeper> actualList = repository.getDirections("0000c9999");

        assertEquals(2, actualList.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnEmptyList() {
        List<Keeper> actualList = repository.getDirections("1111a9999");

        assertEquals(0, actualList.size());
    }
}
