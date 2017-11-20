package juja.microservices.keepers.dao;

import juja.microservices.common.KeeperAbstractTest;
import juja.microservices.common.matchers.KeeperMatcher;
import juja.microservices.keepers.entity.Keeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KeepersRepository.class)
public class KeepersRepositoryTest extends KeeperAbstractTest {

    @Value("${spring.data.mongodb.collection}")
    private String collectionName;

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
                "direction", false)), eq(collectionName));
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void findOneActiveTest() {
        repository.findOneActive("uuid");

        verify(mongoTemplate).findOne(new Query(Criteria.where("uuid").is("uuid"))
                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class, collectionName);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void getActiveKeepers() {
        repository.getActiveKeepers();

        verify(mongoTemplate).find(new Query(Criteria.where("isActive").is(true)), Keeper.class, collectionName);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void getDirections() {
        repository.getDirections("uuid");

        verify(mongoTemplate).find(new Query(
                Criteria.where("uuid").is("uuid").and("isActive").is(true)), Keeper.class, collectionName);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void findOneByUUIdAndDirectionIsActive() {
        repository.findOneByUUIdAndDirectionIsActive("uuid", "direction");

        verify(mongoTemplate).findOne(new Query(Criteria.where("uuid").is("uuid"))
                .addCriteria(Criteria.where("direction").is("direction"))
                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class, collectionName);
        verifyNoMoreInteractions(mongoTemplate);
    }
}