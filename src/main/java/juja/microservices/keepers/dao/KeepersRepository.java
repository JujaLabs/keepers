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

/**
 * @author Vadim Dyachenko
 */
@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    //TODO Should be implemented work with MongoDB

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    public Map<String, List<String>> getAllActiveKeepers(){
        String urlTemplate = urlBase + urlGetKeepers;
        Map<String, List<String>> outMap = new HashMap<>();
//        mongoTemplate.insert(new Keeper("111", "den","dir11", new Date(),  new Date(), false));
//        mongoTemplate.insert(new Keeper("222", "vadim","dir12", new Date(),  new Date(), false));
//        mongoTemplate.insert(new Keeper("333", "sanek","dir13", new Date(),  new Date(), false));
//        mongoTemplate.insert(new Keeper("444", "vadim","dir22", new Date(),  new Date(), false));
//        mongoTemplate.insert(new Keeper("dima", "den","dir11", new Date(),  null, true));
//        mongoTemplate.insert(new Keeper("fedor", "vadim","dir12", new Date(),  null, true));
//        mongoTemplate.insert(new Keeper("sollyk", "sanek","dir13", new Date(),  null, true));
//        mongoTemplate.insert(new Keeper("fedor", "vadim","dir22", new Date(),  null, true));
//        mongoTemplate.insert(new Keeper("sollyk", "sanek","dir23", new Date(),  null, true));
//        mongoTemplate.insert(new Keeper("sollyk", "sanek","dir33", new Date(),  null, true));

        List<Keeper> keepers = mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class,"keeper");
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
