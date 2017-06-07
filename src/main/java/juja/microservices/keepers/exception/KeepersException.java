package juja.microservices.keepers.exception;

/**
 * @author Danil Kuznetsov
 */
public class KeepersException extends RuntimeException {
    public KeepersException(String message) {
        super(message);
    }
}
