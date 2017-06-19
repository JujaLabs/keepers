package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeeperRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class KeeperService {

    @Inject
    private KeeperRepository keeperRepository;

    public List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        return keeperRepository.getAllActiveKeepers();
    }


    public List<Keeper> getAllActiveKeepersTest() throws UserMicroserviceExchangeException {
        return keeperRepository.getAllActiveKeepersTest();
    }
    public Keeper getActiveKeepersTest() throws UserMicroserviceExchangeException {
        return keeperRepository.getActiveKeepersTest();
    }
    public Keeper getInActiveKeepersTest() throws UserMicroserviceExchangeException {
        return keeperRepository.getInActiveKeepersTest();
    }
}