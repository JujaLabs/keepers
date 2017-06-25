package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Roy
 */

@Service
public class KeepersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersRepository keepersRepository;

    public String addKeeper(KeeperRequest keeperRequest){
        logger.debug("Service.addKeeper after in " + LocalDateTime.now() + " in parameters: " + keeperRequest.toString());
        if(keepersRepository.findOneByUUId(keeperRequest.getFrom()) == null){
            logger.warn("User '{}' tried to add new 'Keeper' but he is not a Keeper", keeperRequest.getFrom());
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        String newKeeperId = keepersRepository.save(keeperRequest);
        logger.info("Added new 'Keeper' with DBId'{}', with uuid {}, from user '{}'",
                newKeeperId, keeperRequest.getUuid(), keeperRequest.getFrom());
        logger.debug("Service.addKeeper before out " + LocalDateTime.now() + " out parameters: " + newKeeperId);
        return newKeeperId;
    }

    public Map<String, List<String>> getAllActiveKeepers() {
        return keepersRepository.getAllActiveKeepers();
    }

    //TODO Should be implemented internal service logic
}
