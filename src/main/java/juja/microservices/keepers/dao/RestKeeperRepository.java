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
    private RestTemplate restTemplate;
    @Inject
    private MongoTemplate mongoTemplate;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Override
    public List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        String urlTemplate = urlBase + urlGetKeepers;
        List<Keeper> result = null;
        try {
            ResponseEntity<Keeper[]> response = this.restTemplate.getForEntity(urlTemplate, Keeper[].class);
            result = Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw new UserMicroserviceExchangeException("User microservice Exchange Error: " + ex.getMessage());
        }
        return result;
    }

    @Override
    public List<Keeper> getAllActiveKeepersTest() throws UserMicroserviceExchangeException {
        System.out.println("-------------------------------------------------------");
        System.out.println("count true = " + mongoTemplate.count(new Query( Criteria.where("isActive").is(true)),"keepers"));
        System.out.println("count false = " + mongoTemplate.count(new Query( Criteria.where("isActive").is(false)),"keepers"));
        System.out.println("-------------------------------------------------------");
//        mongoTemplate.insert(new Keeper("123", "17799289","dir2", new Date(),  new Date(),false),"keepers");
//        mongoTemplate.insert(new Keeper("234", "123","dir3", new Date(),  new Date(),false),"keepers");
//        mongoTemplate.insert(new Keeper("345", "234","dir4", new Date(),  new Date(),false),"keepers");
        
        List<Keeper> outList = new ArrayList<>();
        outList.addAll(mongoTemplate.find(new Query(Criteria.where("isActive").is(false)), Keeper.class, "keepers"));
        System.out.println("outList.size() = " + outList.size());
        if(outList.size() == 0) {
            Keeper keeper1 = new Keeper("test_id_1", "test_uuid_1", "test_from_1", "test_dir_1", new Date(), new Date(), true);
            Keeper keeper2 = new Keeper("test_id_2", "test_uuid_2", "test_from_2", "test_dir_2", new Date(), new Date(), false);
            Keeper keeper3 = new Keeper("test_id_3", "test_uuid_3", "test_from_3", "test_dir_3", new Date(), new Date(), true);
            Keeper keeper4 = new Keeper("test_id_4", "test_uuid_4", "test_from_4", "test_dir_4", new Date(), new Date(), false);
            Keeper keeper5 = new Keeper("test_id_5", "test_uuid_5", "test_from_5", "test_dir_5", new Date(), new Date(), true);
            outList.add(keeper1);
            outList.add(keeper2);
            outList.add(keeper3);
            outList.add(keeper4);
            outList.add(keeper5);
        }
        return outList;

//        return mongoTemplate.find(new Query(Criteria.where("isActive").is(false)), Keeper.class, "keepers");
    }

    @Override
    public Keeper getActiveKeepersTest() {
        return new Keeper("test_id_1","test_uuid_1","test_from_1","test_dir_1", new Date(), new Date(), true);
    }

    @Override
    public Keeper getInActiveKeepersTest() throws UserMicroserviceExchangeException {
        Keeper keeper1 = new Keeper("test_id_1","test_uuid_1","test_from_1","test_dir_1", new Date(), new Date(), true );
        List<Keeper> outList = mongoTemplate.find(new Query(Criteria.where("isActive").is(false)), Keeper.class, "keepers");

        if(outList != null && outList.size() > 0){
            return outList.get(0);
        }else{
            return keeper1;
        }
    }

}