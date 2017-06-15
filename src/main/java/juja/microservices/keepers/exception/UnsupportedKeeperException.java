package juja.microservices.keepers.exception;

/**
 * @author Dmitriy Lyashenko
 */
public class UnsupportedKeeperException extends KeepersException {
    public UnsupportedKeeperException(String message) {
        super(message);
    }
}
