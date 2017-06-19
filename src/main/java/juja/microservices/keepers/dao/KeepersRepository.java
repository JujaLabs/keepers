package juja.microservices.keepers.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public List getDirections(String uuid) {

        Query query = new Query();
        query.addCriteria(Criteria.where("uuid").is(uuid).and("isActive").is(Boolean.TRUE));

        return mongoTemplate.getCollection("keepers")
                .distinct("direction", query.getQueryObject());
    }
}
