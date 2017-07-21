package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import juja.microservices.keepers.exception.KeeperNonexistentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 * @author Konstantin Sergey
 * @author Oleksii Petrokhalko
 */
@Service
public class KeepersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersRepository keepersRepository;

    public List<String> getDirections(String uuid) {
        logger.debug("Received get directions by uuid request. Requested uuid: {}", uuid);

        List<Keeper> directions = keepersRepository.getDirections(uuid);

        List<String> result = new ArrayList<>();
        for (Keeper keeper : directions) {
            result.add(keeper.getDirection());
        }
        logger.info("Number of returned keeper directions is {}", result.size());
        logger.debug("Request for active directions for keeper returned {}", result.toString());
        return result;
    }

    public List<String> deactivateKeeper(KeeperRequest keeperRequest) {
        String uuid = keeperRequest.getUuid();
        String direction = keeperRequest.getDirection();
        String from = keeperRequest.getFrom();
        logger.debug("Service.deactivate after in, parameters: {}", keeperRequest.toString());
        if (keepersRepository.findOneActive(from) == null) {
            logger.warn("Keeper '{}' tried to deactivate 'keeper' '{}' but '{}' not active", from, uuid, from);
            throw new KeeperAccessException("Only active keeper could deactivate another keeper");
        }
        Keeper keeper = keepersRepository.findOneByUUIdAndDirectionIsActive(uuid, direction);
        if (keeper == null) {
            logger.warn("Keeper with uuid '{}' and direction '{}' is't exist or not active", uuid, direction);
            throw new KeeperNonexistentException("Keeper with uuid " + uuid + " and direction " + direction
                    + " is't exist or not active");
        }
        keeper.setDismissDate(LocalDateTime.now());
        List<String> ids = Collections.singletonList(keepersRepository.save(keeper));
        logger.info("'Keeper' deactivated , with uuid '{}', from user '{}'", uuid, from);
        logger.debug("Keeper {} was deactivated ", uuid);
        return ids;
    }

    public String addKeeper(KeeperRequest keeperRequest) {
        String uuid = keeperRequest.getUuid();
        String direction = keeperRequest.getDirection();
        String from = keeperRequest.getFrom();
        logger.debug("Service.addKeeper after in, parameters: {}", keeperRequest.toString());
        if (keepersRepository.findOneActive(from) == null) {
            logger.warn("User '{}' tried to add new 'Keeper' but he is not a Keeper", from);
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        if (keepersRepository.findOneByUUIdAndDirectionIsActive(uuid, direction) != null) {
            logger.warn("Keeper with uuid '{}' already keeps direction '{}' and he is active", uuid, direction);
            throw new KeeperDirectionActiveException("Keeper with uuid " + uuid + " already keeps direction "
                    + direction + " and he is active");
        }
        Date startDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper(keeperRequest.getFrom(),
                keeperRequest.getUuid(),
                keeperRequest.getDirection(),
                startDate);
        String newKeeperId = keepersRepository.save(keeper);
        logger.info("Added new 'Keeper' with DBId'{}', with uuid {}, from user '{}'",
                newKeeperId, uuid, from);
        logger.debug("Service.addKeeper before out, parameters: {}", newKeeperId);
        return newKeeperId;
    }

    public Map<String, List<String>> getActiveKeepers() {
        return keepersRepository.getActiveKeepers();
    }
}
