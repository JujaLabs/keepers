package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Roy
 */
@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public Map<String, List<String>> getAllActiveKeepers(){
        Map<String, List<String>> outMap = new HashMap<>();

        List<Keeper> keepers = mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class,"keeper");
        for (Keeper keeper : keepers) {
            List<String> directions = new ArrayList<>();
            if(outMap.containsKey(keeper.getUuid())) {
                directions = outMap.get(keeper.getUuid());
                directions.add(keeper.getDirection());
                outMap.replace(keeper.getUuid(),directions);
            }else{
                directions.add(keeper.getDirection());
                outMap.put(keeper.getUuid(), directions);
            }
        }
        return outMap;
    }
}
