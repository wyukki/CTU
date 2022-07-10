package main.event;

import main.appliances.Appliance;
import main.people.Father;
import main.sensor.Observer;

public final class ApplianceEventHandler implements Observer {

    private final Father father;

    public ApplianceEventHandler(Father father) {
        this.father = father;
    }

    @Override
    public void update(Appliance appliance) {
        if (appliance.getContext().getState().getStateName().equals("broken")) {
            System.out.println(appliance.getName() + " is broken. Father was notified.");
            father.addBrokenAppliance(appliance);
        }
    }
}
