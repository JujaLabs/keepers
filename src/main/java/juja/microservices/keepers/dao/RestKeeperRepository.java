package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.*;

@Repository
public class RestKeeperRepository implements KeeperRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Override
    public Map<String, List<String>> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        String urlTemplate = urlBase + urlGetKeepers;
        Map<String, List<String>> outMap = new HashMap<>();

        List<Keeper> keepers = mongoTemplate.find(new Query(Criteria.where("dismissDate").exists(false)), Keeper.class,"keepers");
        for (Keeper k:keepers) {
            List<String> tmpList = new ArrayList<>();
            if(outMap.containsKey(k.getUuid())) {
                tmpList = outMap.get(k.getUuid());
                tmpList.add(k.getDirection());
                outMap.replace(k.getUuid(),tmpList);
            }else{
                tmpList.add(k.getDirection());
                outMap.put(k.getUuid(), tmpList);
            }
        }
        return outMap;
    }
}