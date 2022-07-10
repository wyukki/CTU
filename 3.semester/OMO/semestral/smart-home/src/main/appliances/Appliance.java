package main.appliances;

import main.appliances.creators.TypeOfAppliance;
import main.appliances.states.Context;
import main.sensor.ApplianceObservable;
import main.sensor.Observer;

import java.util.HashSet;
import java.util.Set;

public class Appliance implements ApplianceObservable {
    protected final TypeOfAppliance type;
    protected String name;
    protected Context context;
    private double consumption;
    private final double MIN_CONSUMPTION;
    private final double ACTIVE_CONSUMPTION;
    private final Set<Observer> observers = new HashSet<>();
    private String manual;
    private final int MAX_FUNCTIONALITY;
    private int functionality;

    public Appliance(TypeOfAppliance type, String name,
                     int functionality, double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        this.type = type;
        this.name = name;
        this.consumption = 0;
        this.MIN_CONSUMPTION = MIN_CONSUMPTION;
        this.ACTIVE_CONSUMPTION = ACTIVE_CONSUMPTION;
        this.functionality = functionality;
        this.MAX_FUNCTIONALITY = functionality;
        this.context = new Context();
    }

    public void turnOn() {
        context.turnOn();
        notifyObservers();
        consumption += ACTIVE_CONSUMPTION;
    }

    public void turnOff() {
        context.turnOff();
        notifyObservers();
        consumption += MIN_CONSUMPTION;
    }

    public void setBroken() {
        context.setBroken();
        notifyObservers();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(observer -> observer.update(this));
    }

    public String getName() {
        return name;
    }

    public String getManual() {
        if (manual == null) {
            manual = "Manual for " + name;
        }
        return manual;
    }

    public void decrementFunctionality() {
        this.functionality = functionality > 0 ? functionality - 1 : 0;
        if (this.functionality == 0) {
            this.setBroken();
        }
    }

    public void resetFunctionality() {
        this.functionality = MAX_FUNCTIONALITY;
    }

    public double getConsumption() {
        return consumption;
    }

    public String getUnitOfMeasurement() {
        return type == TypeOfAppliance.GAS ? " m^3" : " kWh";
    }
}
