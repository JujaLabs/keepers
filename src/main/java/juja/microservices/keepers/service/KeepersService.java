package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.ActiveKeeperDTO;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
    private KeepersRepository repository;

    public List<String> getDirections(String uuid) {
        logger.debug("Requesting the active directions to repository");
        List<Keeper> directions = repository.getDirections(uuid);
        logger.debug("Found {} active directions for uuid '{}': {}", directions.size(), uuid, directions.toString());

        List<String> result = new ArrayList<>();
        for (Keeper keeper : directions) {
            result.add(keeper.getDirection());
        }

        logger.info("Request for active directions for keeper returned {}", result.toString());
        return result;
    }

    public List<String> deactivateKeeper(KeeperRequest keeperRequest) {
        String uuid = keeperRequest.getUuid();
        String from = keeperRequest.getFrom();
        if (repository.findOneActive(from) == null) {
            String message = String.format("Request 'deactivate keeper' rejected. User '%s' tried to deactivate keeper, but he is not an active keeper", from);
            throw new KeeperAccessException(message);
        }

        String direction = keeperRequest.getDirection();
        Keeper keeper = repository.findOneByUUIdAndDirectionIsActive(uuid, direction);
        if (keeper == null) {
            String message = String.format("Keeper with uuid '%s' and direction '%s' is't exist or not active", uuid, direction);
            throw new KeeperNonexistentException(message);
        }

        keeper.setDismissDate(LocalDateTime.now());
        logger.debug("Trying to update record in repository. Deactivate date: {}", keeper.getDismissDate());
        String id = repository.save(keeper);
        logger.info("Keeper deactivated successfully. Id: '{}', uuid: '{}', from user: '{}'", id, uuid, from);

        return Collections.singletonList(id);
    }

    public List<String> addKeeper(KeeperRequest keeperRequest) {
        String from = keeperRequest.getFrom();
        if (repository.findOneActive(from) == null) {
            String message = String.format("Request 'add keeper' rejected. User '%s' tried to add new keeper, but he is not an active keeper", from);
            throw new KeeperAccessException(message);
        }

        String uuid = keeperRequest.getUuid();
        String direction = keeperRequest.getDirection();
        if (repository.findOneByUUIdAndDirectionIsActive(uuid, direction) != null) {
            String message = String.format("Keeper with uuid '%s' already keeps direction '%s' and he is active", uuid, direction);
            throw new KeeperDirectionActiveException(message);
        }

        Date startDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper(from, uuid, direction, startDate);
        logger.debug("Trying to add new keeper to repository. {}", keeper.toString());
        String id = repository.save(keeper);
        logger.info("New keeper added successfully. Id: '{}', uuid: '{}', from user: '{}'", id, uuid, from);

        return Collections.singletonList(id);
    }

    public List<ActiveKeeperDTO> getActiveKeepers() {
        logger.debug("Requesting the active keepers to repository");
        List<Keeper> keepers = repository.getActiveKeepers();
        List<ActiveKeeperDTO> result = new ArrayList<>();
        if (keepers != null) {
            logger.debug("Received keepers: {}", keepers.toString());

            Map<String, ActiveKeeperDTO> activeKeepers = new HashMap<>();
            for (Keeper keeper : keepers) {
                String uuid = keeper.getUuid();
                String direction = keeper.getDirection();

                if (activeKeepers.containsKey(uuid)) {
                    activeKeepers.get(uuid).addDirection(direction);
                } else {
                    activeKeepers.put(uuid, new ActiveKeeperDTO(uuid, Collections.singletonList(direction)));
                }
            }

            activeKeepers.forEach((String, ActiveKeeperDTO) -> result.add(ActiveKeeperDTO));
        }
        logger.info("Returned active keepers {}", result.toString());
        return result;
    }
}
