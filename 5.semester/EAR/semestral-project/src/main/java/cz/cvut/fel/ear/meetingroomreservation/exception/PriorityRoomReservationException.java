package cz.cvut.fel.ear.meetingroomreservation.exception;

public class PriorityRoomReservationException extends RuntimeException {
    public PriorityRoomReservationException(String message) {
        super(message);
    }

    public PriorityRoomReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
