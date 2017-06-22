package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author Vadim Dyachenko
 */

@Service
public class KeepersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersRepository keepersRepository;

    public String addKeeper(KeeperRequest keeperRequest){
        if(keepersRepository.findOneByUUId(keeperRequest.getFrom()) == null){
            logger.warn("User '{}' tried to add new 'Keeper' but he is not a Keeper", keeperRequest.getFrom());
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        String newKeeperId = keepersRepository.save(keeperRequest);
        logger.info("Added new 'Keeper' with uuid {}, from user '{}'", keeperRequest.getUuid(), keeperRequest.getFrom());
        return newKeeperId;
    }

    //TODO Should be implemented internal service logic
}
