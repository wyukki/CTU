package cz.cvut.fel.ear.meetingroomreservation.exception;

public class EmptyReservationListException extends RuntimeException {
    public EmptyReservationListException(String message) {
        super(message);
    }

    public EmptyReservationListException(String message, Throwable cause) {
        super(message, cause);
    }
}
