package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeepersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersRepository keepersRepository;

    public List getDirections(String uuid) {
        List result  = keepersRepository.getDirections(uuid);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        logger.info("Request for active directions for keeper with uuid " + uuid);
        return result;
    }
}
