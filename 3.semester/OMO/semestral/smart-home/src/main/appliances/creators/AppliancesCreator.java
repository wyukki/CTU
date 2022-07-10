package main.appliances.creators;

import main.appliances.Appliance;

import java.util.Random;

public interface AppliancesCreator {
    Random random = new Random();

    /**
     * Creates appliance of a specific type.
     *
     * @param appliance to be created
     * @return created appliance
     */
    Appliance createAppliance(String appliance);
}
