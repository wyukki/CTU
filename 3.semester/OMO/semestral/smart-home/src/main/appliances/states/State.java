package main.appliances.states;

public abstract class State {
    private String stateName;

    public abstract void turnOff(Context context);

    public abstract void turnOn(Context context);

    public abstract void setBroken(Context context);

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
