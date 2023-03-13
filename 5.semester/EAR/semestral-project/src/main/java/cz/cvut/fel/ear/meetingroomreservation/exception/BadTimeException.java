package cz.cvut.fel.ear.meetingroomreservation.exception;

public class BadTimeException extends RuntimeException{
    public BadTimeException(String message) {
        super(message);
    }

    public BadTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
