package main.pets.states;

public class AtHome implements PetState {
    @Override
    public void walk(PetContext context) {
        context.setState(new Walking());
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
