package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.ActiveKeeperDTO;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.KeeperAccessException;
import juja.microservices.keepers.exception.KeeperDirectionActiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 * @author Konstantin Sergey
 */
@Service
public class KeepersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersRepository keepersRepository;

    public List<String> getDirections(String uuid) {
        logger.debug("Received get directions by uuid request. Requested uuid: {}", uuid);

        List<Keeper> directions = keepersRepository.getDirections(uuid);

        List<String> result = new ArrayList<>();
        for (Keeper keeper : directions) {
            result.add(keeper.getDirection());
        }
        logger.info("Number of returned keeper directions is {}", result.size());
        logger.debug("Request for active directions for keeper returned {}", result.toString());
        return result;
    }

    public String addKeeper(KeeperRequest keeperRequest){
        logger.debug("Service.addKeeper after in, parameters: {}", keeperRequest.toString());
        if(keepersRepository.findOneByUUId(keeperRequest.getFrom()) == null){
            logger.warn("User '{}' tried to add new 'Keeper' but he is not a Keeper", keeperRequest.getFrom());
            throw new KeeperAccessException("Only the keeper can appoint another keeper");
        }

        if(keepersRepository.findOneByUUIdAndDirectionIsActive(keeperRequest.getUuid(), keeperRequest.getDirection()) != null){
            logger.warn("Keeper with uuid '{}' already keeps direction '{}' and he is active",
                    keeperRequest.getUuid(), keeperRequest.getDirection());
            throw new KeeperDirectionActiveException("Keeper with uuid " + keeperRequest.getUuid() + " already keeps direction "
                    + keeperRequest.getDirection() + " and he is active");
        }

        String newKeeperId = keepersRepository.save(keeperRequest);
        logger.info("Added new 'Keeper' with DBId'{}', with uuid {}, from user '{}'",
                newKeeperId, keeperRequest.getUuid(), keeperRequest.getFrom());
        logger.debug("Service.addKeeper before out, parameters: {}", newKeeperId);
        return newKeeperId;
    }

    public List<ActiveKeeperDTO> getActiveKeepers() {
        logger.debug("Service.getActiveKeepers after in, without any parameters.");
        List<ActiveKeeperDTO> activeKeeperDTOList = new ArrayList<>();
        Map<String, ActiveKeeperDTO> activeKeeperDTOMap = new HashMap<>();
        logger.debug("Service.getActiveKeepers after repository invocation");
        List<Keeper> keepers = keepersRepository.getActiveKeepers();
        logger.debug("Get List<Keeper> : {}", keepers);
        for (Keeper keeper : keepers) {
            String keeperUuid = keeper.getUuid();

            if(activeKeeperDTOMap.containsKey(keeperUuid)) {
                activeKeeperDTOMap.get(keeperUuid).addDirection(keeper.getDirection());
            }else{
                activeKeeperDTOMap.put(keeperUuid, new ActiveKeeperDTO(keeperUuid, Arrays.asList(keeper.getDirection())));
            }
        }

        activeKeeperDTOMap.forEach((String, ActiveKeeperDTO) -> activeKeeperDTOList.add(ActiveKeeperDTO));
        logger.debug("Create Map<String, ActiveKeeperDTO>. It has {} elements.", activeKeeperDTOMap.size());
        logger.info("Service.getActiveKeepers before out with result data - list of ActiveKeepersDTO: {}", activeKeeperDTOList);
        return activeKeeperDTOList;
    }
}
