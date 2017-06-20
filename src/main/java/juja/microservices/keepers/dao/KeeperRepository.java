package juja.microservices.keepers.dao;

import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import java.util.*;

public interface KeeperRepository {
    Map<String, List<String>> getAllActiveKeepers() throws UserMicroserviceExchangeException;
}