package main.appliances.states;

public final class TurnedOff extends State {

    @Override
    public void turnOff(Context context) {
        return;
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
