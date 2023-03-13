package cz.cvut.fel.ear.meetingroomreservation.exception;

public class LowPriorityException extends RuntimeException {
    public LowPriorityException(String message) {
        super(message);
    }

    public LowPriorityException(String message, Throwable cause) {
        super(message, cause);
    }
}
