package juja.microservices.keepers.exception;

/**
 * @author Oleksii Petrokhalko.
 */
public class KeeperNonexistentException extends KeepersException {
    public KeeperNonexistentException(String message) {
        super(message);
    }
}
