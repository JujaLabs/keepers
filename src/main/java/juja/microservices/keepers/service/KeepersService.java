package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author Vadim Dyachenko
 *         Dmitriy Roy
 */
@Service
public class KeepersService {
    @Inject
    private KeepersRepository keepersRepository;

    public Map<String, List<String>> getAllActiveKeepers() {
        return keepersRepository.getAllActiveKeepers();
    }
}
