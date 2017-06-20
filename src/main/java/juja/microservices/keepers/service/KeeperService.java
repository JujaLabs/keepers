package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeeperRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class KeeperService {

    @Inject
    private KeeperRepository keeperRepository;

    public Map<String, List<String>> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        return keeperRepository.getAllActiveKeepers();
    }

}