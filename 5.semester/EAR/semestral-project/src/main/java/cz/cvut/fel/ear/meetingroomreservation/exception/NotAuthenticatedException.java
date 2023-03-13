package cz.cvut.fel.ear.meetingroomreservation.exception;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String message) {
        super(message);
    }

    public NotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
