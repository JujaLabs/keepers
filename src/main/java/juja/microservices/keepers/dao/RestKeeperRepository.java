package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class RestKeeperRepository implements KeeperRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Override
    public List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        String urlTemplate = urlBase + urlGetKeepers;
//        System.out.println("-------------------------------------------------------");
//        System.out.println("count true = " + mongoTemplate.count(new Query( Criteria.where("isActive").is(true)),"keepers"));
//        System.out.println("count false = " + mongoTemplate.count(new Query( Criteria.where("isActive").is(false)), Keeper.class));
//        System.out.println("-------------------------------------------------------");
//        mongoTemplate.insert(new Keeper("dima", "den","dir2", new Date(),  new Date(),true),"keepers");
//        mongoTemplate.insert(new Keeper("fedor", "vadim","dir3", new Date(),  new Date(),true),"keepers");
//        mongoTemplate.insert(new Keeper("sollyk", "sanek","dir4", new Date(),  new Date(),true),"keepers");

        return mongoTemplate.find(new Query(Criteria.where("isActive").is(true)), Keeper.class,"keepers");
    }

}