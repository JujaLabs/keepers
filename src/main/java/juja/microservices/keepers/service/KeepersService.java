package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.KeeperRequest;

import juja.microservices.keepers.exception.KeeperAccessException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author Vadim Dyachenko
 */

@Service
public class KeepersService {

    @Inject
    private KeepersRepository keepersRepository;

    public String addKeeper(KeeperRequest keeperRequest){
        if(keepersRepository.findOneByUUId(keeperRequest.getFrom()) == null){
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        return keepersRepository.save(keeperRequest);
    }

    //TODO Should be implemented internal service logic
}
