package main.activityElements;

import main.people.Person;

public final class Car {
    private final String color;
    private final String model;
    private boolean isInUse;
    private Person user;
    private static Car instance;

    private Car(String color, String model) {
        this.color = color;
        this.model = model;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public String getModel() {
        return model;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public Person getUser() {
        return user;
    }

    public static Car getInstance(String color, String model) {
        if (instance == null) {
            instance = new Car(color, model);
        }
        return instance;
    }
}
