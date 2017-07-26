package juja.microservices.common.matchers;

import juja.microservices.keepers.entity.Keeper;
import org.mockito.ArgumentMatcher;

/**
 * @author Oleksii Petrokhalko.
 */
public class KeeperMatcher extends ArgumentMatcher<Keeper> {
    private String id;
    private String uuid;
    private String from;
    private String direction;
    private boolean status;

    public KeeperMatcher(String id, String uuid, String from, String direction, boolean status) {
        this.id = id;
        this.uuid = uuid;
        this.from = from;
        this.direction = direction;
        this.status = status;
    }

    @Override
    public boolean matches(Object argument) {
        Keeper keeper = (Keeper) argument;
        return id.equals(keeper.getId()) && uuid.equals(keeper.getUuid()) && from.equals(keeper.getFrom())
                && direction.equals(keeper.getDirection()) && status == keeper.isActive();
    }
}
