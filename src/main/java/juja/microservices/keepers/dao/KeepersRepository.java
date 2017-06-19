package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.KeeperDirectionRequest;
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

    public List<KeeperDirectionRequest> getDirections(String uuid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uuid").is(uuid).and("isActive").is(Boolean.TRUE));
        query.fields().include("direction");

        return mongoTemplate.find(query, KeeperDirectionRequest.class);
    }

    //TODO Should be implemented work with MongoDB
}
