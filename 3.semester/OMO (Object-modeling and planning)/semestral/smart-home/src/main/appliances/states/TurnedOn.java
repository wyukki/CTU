package main.appliances.states;

public final class TurnedOn extends State {

    @Override
    public void turnOff(Context context) {
        context.setState(new TurnedOff());
    }

    @Override
    public void turnOn(Context context) {
        context.setState(new TurnedOn());
    }

    @Override
    public void setBroken(Context context) {
        context.setState(new Broken());
    }
}
