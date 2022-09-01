package main.appliances.creators;

import main.appliances.Appliance;
import main.appliances.Boiler;
import main.appliances.Stove;

import java.util.NoSuchElementException;

public final class GasApplianceCreator implements AppliancesCreator {
    private final TypeOfAppliance type = TypeOfAppliance.GAS;
    private final Functionality highFunc = Functionality.HIGH;
    private final Functionality medium = Functionality.MEDIUM;

    @Override
    public Appliance createAppliance(String appliance) {
        double minNonActiveConsumption = 0.01;
        double maxNonActiveConsumption = 0.05;
        double minActiveConsumption = 0.2;
        double maxActiveConsumption = 0.5;
        if ("Boiler".equals(appliance)) {
            return new Boiler(type, appliance, highFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("Stove".equals(appliance)) {
            return new Stove(type, appliance, medium.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else {
            throw new NoSuchElementException();
        }
    }

    private double getRandomConsumption(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}
