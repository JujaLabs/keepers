package juja.microservices.keepers.dao;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.integration.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
public class KeepersRepositoryTest extends BaseIntegrationTest {

    @Inject
    private KeepersRepository repository;

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnDirections() {
        List<String> expectedList = Arrays.asList("First active direction", "Second active direction");
        List<String> actualList = repository.getDirections("0000c9999");
        assertEquals(2, expectedList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json")
    public void shouldReturnEmptyList() {
        List<String> actualList = repository.getDirections("1111a9999");
        assertEquals(0, actualList.size());
    }
}
