package cz.cvut.fel.ear.meetingroomreservation.exception;

public class CancellationAfterTerm extends RuntimeException {
    public CancellationAfterTerm(String message) {
        super(message);
    }

    public CancellationAfterTerm(String message, Throwable cause) {
        super(message, cause);
    }
}
