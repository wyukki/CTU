package main.appliances.states;

public final class Broken extends State {

    @Override
    public void turnOff(Context context) {
        context.setState(new Broken());
    }

    @Override
    public void turnOn(Context context) {
        context.setState(new Broken());
    }

    @Override
    public void setBroken(Context context) {
        context.setState(new Broken());
    }

}
