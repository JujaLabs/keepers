package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.KeeperDirectionRequest;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeepersService {

    @Inject
    private KeepersRepository keepersRepository;

    public List<KeeperDirectionRequest> getDirections(String uuid) {
        List<KeeperDirectionRequest> directions = keepersRepository.getDirections(uuid);

        if (directions.isEmpty()) {
            return new ArrayList<>();
        } else {
            return keepersRepository.getDirections(uuid);
        }
    }
    //TODO Should be implemented internal service logic
}
