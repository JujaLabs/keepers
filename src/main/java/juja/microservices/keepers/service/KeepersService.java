package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
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

    public List<String> getDirections(String uuid) {
        logger.debug(LocalDateTime.now() + " Invoke of KeepersService.getDirections()");

        List<String> result  = keepersRepository.getDirections(uuid);

        logger.info("Number of returned keepers directions is ", result.size());
        logger.debug(LocalDateTime.now() + "Request for active directions for keeper with uuid " + uuid +
                " returned: " + result.toString());
        return result;
    }
}
