package main.sensor;

import main.appliances.Appliance;

public interface Observer {
    void update(Appliance appliance);
}
