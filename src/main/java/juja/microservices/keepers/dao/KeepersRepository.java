package juja.microservices.keepers.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author Vadim Dyachenko
 */

@Repository
public class KeepersRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    //TODO Should be implemented work with MongoDB
}
