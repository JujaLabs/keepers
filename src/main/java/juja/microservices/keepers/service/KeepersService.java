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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<String> deleteKeeper(KeeperRequest keeperRequest) {
        logger.debug("Service.deleteKeeper after in, parameters: {}", keeperRequest.toString());
        if (keepersRepository.findOneActive(keeperRequest.getFrom()) == null) {
            logger.warn("Keeper '{}' tried to delete 'Keeper' but he's not an active Keeper", keeperRequest.getFrom());
            throw new KeeperAccessException("Only active keeper could delete another keeper");
        }
        Keeper keeper = keepersRepository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection());
        if (keeper == null) {
            logger.warn("Keeper with uuid '{}' and direction '{}' is't exist or inactive",
                    keeperRequest.getUuid(), keeperRequest.getDirection());
            throw new KeeperNonexistentException("Keeper with uuid " + keeperRequest.getUuid() + " and direction "
                    + keeperRequest.getDirection() + " is't exist or inactive");
        }
        keeper.setDismissDate(LocalDateTime.now());
        List<String> ids = new ArrayList<>();
        ids.add(keepersRepository.delete(keeper));
        return ids;
    }

    public String addKeeper(KeeperRequest keeperRequest) {
        logger.debug("Service.addKeeper after in, parameters: {}", keeperRequest.toString());
        if (keepersRepository.findOneByUUId(keeperRequest.getFrom()) == null) {
            logger.warn("User '{}' tried to add new 'Keeper' but he is not a Keeper", keeperRequest.getFrom());
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        if (keepersRepository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()) != null) {
            logger.warn("Keeper with uuid '{}' already keeps direction '{}' and he is active",
                    keeperRequest.getUuid(), keeperRequest.getDirection());
            throw new KeeperDirectionActiveException("Keeper with uuid " + keeperRequest.getUuid() + " already keeps direction "
                    + keeperRequest.getDirection() + " and he is active");
        }

        String newKeeperId = keepersRepository.save(keeperRequest);
        logger.info("Added new 'Keeper' with DBId'{}', with uuid {}, from user '{}'",
                newKeeperId, keeperRequest.getUuid(), keeperRequest.getFrom());
        logger.debug("Service.addKeeper before out, parameters: {}", newKeeperId);
        return newKeeperId;
    }

    public Map<String, List<String>> getActiveKeepers() {
        return keepersRepository.getActiveKeepers();
    }
}
