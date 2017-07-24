package juja.microservices.keepers.dao;

import juja.microservices.common.KeeperAbstractTest;
import juja.microservices.common.matchers.KeeperMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

import juja.microservices.keepers.entity.Keeper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersRepository.class)
public class KeepersRepositoryTest extends KeeperAbstractTest {

    @Inject
    private KeepersRepository repository;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    public void deactivateKeeperTest() {
        Keeper keeper = createKeeper().withId("uuid").withUuid("uuid")
                .withFrom("from").withDirection("direction").isActive(false).create();
        String actual = repository.save(keeper);
        assertEquals("uuid", actual);

        verify(mongoTemplate).save(argThat(new KeeperMatcher("uuid", "uuid", "from",
                "direction", false)));
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void findOneActiveTest() {
        Keeper keeper = createKeeper().withId("uuid").isActive(true).create();
        when(mongoTemplate.findOne(new Query(Criteria.where("uuid").is("uuid"))
                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class))
                .thenReturn(keeper);

        assertEquals(keeper, repository.findOneActive("uuid"));
    }

    @Test
    public void findOneById() {
        //Given
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);

        //When
        when(mongoTemplate.findOne(new Query(Criteria.where("uuid").is("456rty")), Keeper.class)).thenReturn(keeper);

        //Then
        assertEquals(keeper, repository.findOneByUUId("456rty"));
    }

    @Test
    public void getActiveKeepers() {
        //Given
        List<Keeper> listActiveKeepers = new ArrayList<>();
        Keeper activeKeeper1 = new Keeper("uuidFrom1", "uuidTo1", "teems", new Date());
        Keeper activeKeeper2 = new Keeper("uuidFrom2", "uuidTo1", "sqlcmd", new Date());
        Keeper activeKeeper3 = new Keeper("uuidFrom1", "uuidTo2", "sqlcmd", new Date());
        listActiveKeepers.add(activeKeeper1);
        listActiveKeepers.add(activeKeeper2);
        listActiveKeepers.add(activeKeeper3);

        //When
        when(mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class))
                .thenReturn(listActiveKeepers);

        //Then
        assertEquals(listActiveKeepers, repository.getActiveKeepers());

    }
}