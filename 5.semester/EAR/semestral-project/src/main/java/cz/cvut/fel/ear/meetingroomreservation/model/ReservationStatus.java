package cz.cvut.fel.ear.meetingroomreservation.model;

public enum ReservationStatus {
    CANCELED("CANCELED"),
    ACTIVE("ACTIVE"),
    FINISHED("FINISHED");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
