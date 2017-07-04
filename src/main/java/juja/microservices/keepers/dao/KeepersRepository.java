package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 */
@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public String save(KeeperRequest keeperRequest) {
        Date startDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper(keeperRequest.getFrom(),
                                    keeperRequest.getUuid(),
                                    keeperRequest.getDirection(),
                                    startDate);
        mongoTemplate.save(keeper);
        return keeper.getId();
    }

    public Keeper findOneByUUId(String uuid){
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(uuid)), Keeper.class);
    }

    public Keeper findOneByUUIdAndDirectionIsActive(String uuid, String direction){
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(uuid))
                                                .addCriteria(Criteria.where("direction").is(direction))
                                                .addCriteria(Criteria.where("isActive").is(true)), Keeper.class);
    }

    public List<Keeper> getActiveKeepers(){
        return mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class);
    }
}
