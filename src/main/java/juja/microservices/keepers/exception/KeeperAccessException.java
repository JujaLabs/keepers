package juja.microservices.keepers.exception;

/**
 * @author Dmitriy Lyashenko
 */

public class KeeperAccessException extends KeepersException {
    public KeeperAccessException(String message) {
        super(message);
    }
}
