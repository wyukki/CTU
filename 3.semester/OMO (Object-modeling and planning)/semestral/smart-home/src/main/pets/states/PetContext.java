package main.pets.states;

public class PetContext {
    private PetState state;

    public PetContext() {
        state = new Awake();
    }

    public void setState(PetState state) {
        this.state = state;
    }

    public PetState getState() {
        return state;
    }

    public void feed() {
        state.feed(this);
    }

    public void walk() {
        state.walk(this);
    }

    public void returnHome() {
        state.returnHome(this);
    }
}
