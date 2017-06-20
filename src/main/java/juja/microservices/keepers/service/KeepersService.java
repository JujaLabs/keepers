package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeepersService {
    @Inject
    private KeepersRepository keepersRepository;
    //TODO Should be implemented internal service logic

    public Map<String, List<String>> getAllActiveKeepers() {
        return keepersRepository.getAllActiveKeepers();
    }
}
