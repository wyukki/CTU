package main.pets.states;

public class Walking implements PetState {
    @Override
    public void walk(PetContext context) {
        context.setState(new Hungry());
    }

    @Override
    public void feed(PetContext context) {
        context.setState(new AtHome());
    }

    @Override
    public void returnHome(PetContext context) {
        context.setState(new AtHome());
    }
}
