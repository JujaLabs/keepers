package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeepersService {

    @Inject
    private KeepersRepository keepersRepository;

    public List getDirections(String uuid) {
        List result  = keepersRepository.getDirections(uuid);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        return result;
    }
}
