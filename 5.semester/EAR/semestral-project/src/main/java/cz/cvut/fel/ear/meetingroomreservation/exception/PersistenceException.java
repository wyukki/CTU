package cz.cvut.fel.ear.meetingroomreservation.exception;


public class PersistenceException extends RuntimeException {
    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message) {
        super(message);
    }
}
