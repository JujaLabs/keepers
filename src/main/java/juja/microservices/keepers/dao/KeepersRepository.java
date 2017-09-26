package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 * @author Konstantin Sergey
 * @author Oleksii Petrokhalko
 */
@Repository
public class KeepersRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.data.mongodb.collection}")
    private String mongoCollectionName;

    @Inject
    private MongoTemplate mongoTemplate;

    public List<Keeper> getDirections(String uuid) {
        return mongoTemplate.find(new Query(
                Criteria.where("uuid").is(uuid).and("isActive").is(true)), Keeper.class, mongoCollectionName);
    }

    public String save(Keeper keeper) {
        mongoTemplate.save(keeper, mongoCollectionName);
        return keeper.getId();
    }

    public Keeper findOneByUUId(String uuid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(uuid)), Keeper.class, mongoCollectionName);
    }

    public Keeper findOneByUUIdAndDirectionIsActive(String uuid, String direction) {
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(uuid))
                .addCriteria(Criteria.where("direction").is(direction))
                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class, mongoCollectionName);
    }

    public Keeper findOneActive(String uuid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(uuid))
                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class, mongoCollectionName);
    }

    public List<Keeper> getActiveKeepers() {
        return mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class, mongoCollectionName);
    }
}
