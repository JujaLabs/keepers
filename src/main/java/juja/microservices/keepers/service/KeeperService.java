package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeeperRepository;
import juja.microservices.keepers.entity.Keeper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class KeeperService {

    @Inject
    private KeeperRepository keeperRepository;

    public List<Keeper> getAllActiveKeepers() {
        return keeperRepository.getAllActiveKeepers();
    }
}