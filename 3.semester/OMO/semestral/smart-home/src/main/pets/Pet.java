package main.pets;

import main.building.Room;
import main.pets.states.PetContext;

public class Pet {
    private final String nickname;
    private final String type;
    private Room location;
    private PetContext context;

    public Pet(String nickname, Room location, String type) {
        this.nickname = nickname;
        this.location = location;
        this.context = new PetContext();
        this.type = type;
    }

    public void feed() {
        context.feed();
    }

    public void walk() {
        context.walk();
    }

    public void returnHome() {
        context.returnHome();
    }

    public PetContext getContext() {
        return context;
    }

    public void setContext(PetContext context) {
        this.context = context;
    }

    public String getNickname() {
        return type + " " + nickname;
    }

    public Room getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public void setLocation(Room location) {
        this.location = location;
    }
}
