package juja.microservices.common.creators;

import juja.microservices.keepers.entity.Keeper;

import java.time.LocalDateTime;

/**
 * @author Oleksii Petrokhalko.
 */
public class KeeperCreator implements Creator<Keeper> {
    private Keeper keeper;

    public KeeperCreator() {
        this.keeper = new Keeper();
    }

    public KeeperCreator withId(String id) {
        keeper.setId(id);
        return this;
    }

    public KeeperCreator withUuid(String uuid) {
        keeper.setUuid(uuid);
        return this;
    }

    public KeeperCreator withFrom(String from) {
        keeper.setFrom(from);
        return this;
    }

    public KeeperCreator withDirection(String direction) {
        keeper.setDirection(direction);
        return this;
    }

    public KeeperCreator isActive(boolean isActive) {
        keeper.setActive(isActive);
        return this;
    }

    public KeeperCreator withDismissDate(LocalDateTime dismissDate) {
        keeper.setDismissDate(dismissDate);
        return this;
    }

    @Override
    public Keeper create() {
        return keeper;
    }
}
