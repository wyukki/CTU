package cz.cvut.fel.ear.meetingroomreservation.exception;

public class EmptyBuildingException extends RuntimeException {
    public EmptyBuildingException(String message) {
        super(message);
    }

    public EmptyBuildingException(String message, Throwable cause) {
        super(message, cause);
    }
}
