package juja.microservices.keepers.exception;

/**
 * @author Vadim Dyachenko
 */
public class KeepersException extends RuntimeException {
    public KeepersException(String message) {
        super(message);
    }
}
