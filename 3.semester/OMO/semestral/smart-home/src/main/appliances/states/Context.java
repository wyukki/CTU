package main.appliances.states;


public class Context {
    private State state;
    private String stateName;

    public Context() {
        state = new TurnedOff();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void turnOn() {
        if (state instanceof Broken) {
            return;
        }
        state.turnOn(this);
        state.setStateName("turned on");
    }

    public void turnOff() {
        if (state instanceof Broken) {
            return;
        }
        state.turnOff(this);
        state.setStateName("turned off");
    }

    public void setBroken() {
        state.setBroken(this);
        state.setStateName("broken");
    }

    public State getState() {
        return this.state;
    }
}
