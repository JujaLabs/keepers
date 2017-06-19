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
        List<KeeperDirectionRequest> result  = keepersRepository.getDirections(uuid);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        return result;
    }
    //TODO Should be implemented internal service logic
}
