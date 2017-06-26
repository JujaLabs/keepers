package juja.microservices.keepers.exception;

/**
 * @author Dmitriy Lyashenko
 */

public class KeeperDirectionActiveException extends RuntimeException {
    public KeeperDirectionActiveException(String message) {
        super(message);
    }
}
