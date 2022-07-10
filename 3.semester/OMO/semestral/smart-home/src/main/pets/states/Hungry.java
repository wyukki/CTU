package main.pets.states;

public class Hungry implements PetState {
    @Override
    public void walk(PetContext context) {
        return;
    }

    @Override
    public void feed(PetContext context) {
        context.setState(new Fed());
    }

    @Override
    public void returnHome(PetContext context) {
        return;
    }
}
