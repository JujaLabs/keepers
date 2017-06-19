package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface KeeperRepository {
    List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException;

    List<Keeper> getAllActiveKeepersTest() throws UserMicroserviceExchangeException;
    Keeper getActiveKeepersTest() throws UserMicroserviceExchangeException;
    Keeper getInActiveKeepersTest() throws UserMicroserviceExchangeException;
}