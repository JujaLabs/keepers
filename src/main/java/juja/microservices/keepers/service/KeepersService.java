package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeepersService {
    @Inject
    private KeepersRepository keepersRepository;
    //TODO Should be implemented internal service logic
}
