package cz.cvut.fel.ear.meetingroomreservation.exception;

public class ToManyReservations extends RuntimeException {
    public ToManyReservations(String message) {
        super(message);
    }

    public ToManyReservations(String message, Throwable cause) {
        super(message, cause);
    }
}
