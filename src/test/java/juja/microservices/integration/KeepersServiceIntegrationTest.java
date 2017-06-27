package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.service.KeepersService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperNotExistKeeper() {
        //Given
        KeeperRequest request = new KeeperRequest("123qwe", "asdqwe", "teems");

        //When
        service.addKeeper(request);
    }

    @Test(expected = KeeperDirectionActiveException.class)
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeperWhichKeeperAlreadyKeepDirectionIsAlive() {
        //Given
        KeeperRequest request = new KeeperRequest("asdqwe", "asdqwe", "teems");

        //When
        service.addKeeper(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json")
    public void addKeeper() {
        //When
        service.addKeeper(new KeeperRequest("asdqwe", "123qwe", "teems"));
        String result = repository.findOneByUUId("asdqwe").getUuid();
        String result2 = repository.findOneByUUId("123qwe").getUuid();

        //Then
        assertNotNull(result);
        assertNotNull(result2);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnDirections() {
        //Given
        List<String> expectedList = Arrays.asList("First active direction", "Second active direction");
        //When
        List<String> actualList = repository.getDirections("0000c9999");
        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnCorrectSize() {
        //Given
        List<String> expectedList = Arrays.asList("First active direction", "Second active direction");
        //When
        List<String> actualList = repository.getDirections("0000c9999");
        //Then
        assertEquals(2, actualList.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnEmptyList() {
        //When
        List<String> actualList = repository.getDirections("1111a9999");
        //Then
        assertEquals(0, actualList.size());
    }
}
