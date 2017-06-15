package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * @author Vadim Dyachenko
 */

@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public String save(KeeperRequest keeperRequest) {
        Keeper keeper = new Keeper(keeperRequest.getFrom(),
                                    keeperRequest.getUuid(),
                                    keeperRequest.getDirection(),
                                    getFormattedCurrentDate());
        mongoTemplate.save(keeper);
        return keeper.getId();
    }

    public Keeper findOneById(String id){
        return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(id)), Keeper.class);
    }

    private String getFormattedCurrentDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentDate);
    }

    //TODO Should be implemented work with MongoDB
}
