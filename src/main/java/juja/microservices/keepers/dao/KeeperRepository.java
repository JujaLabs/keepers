package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;

import java.util.List;

public interface KeeperRepository {
    List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException;
}