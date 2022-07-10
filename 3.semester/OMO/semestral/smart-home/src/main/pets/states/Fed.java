package main.pets.states;

public class Fed implements PetState {
    @Override
    public void walk(PetContext context) {
        context.setState(new Walking());
    }

    @Override
    public void feed(PetContext context) {
        return;
    }

    @Override
    public void returnHome(PetContext context) {
        return;
    }
}
