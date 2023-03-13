package cz.cvut.fel.ear.meetingroomreservation.model;

public enum Priority {
    PRIOR("PRIORITY_PRIOR"),
    NON_PRIOR("PRIORITY_NON");

    private final String name;

    Priority(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
