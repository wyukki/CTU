package main.activityElements;

import main.people.Person;

public class SportsElements {
    private boolean isInUse;
    private Person user;
    private final SportsElementType elementType;

    public SportsElements(SportsElementType elementType) {
        this.elementType = elementType;
    }

    public SportsElementType getElementType() {
        return elementType;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }
}
