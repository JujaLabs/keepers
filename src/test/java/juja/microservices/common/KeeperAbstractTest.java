package juja.microservices.common;

import juja.microservices.common.creators.KeeperCreator;

/**
 * @author Oleksii Petrokhalko.
 */
public abstract class KeeperAbstractTest {

    protected KeeperCreator createKeeper() {
        return new KeeperCreator();
    }
}
