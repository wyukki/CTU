package main.appliances.creators;

import main.appliances.*;

import java.util.NoSuchElementException;

public final class ElectricityApplianceCreator implements AppliancesCreator {
    private final TypeOfAppliance type = TypeOfAppliance.ELECTRICITY;
    private final Functionality highFunc = Functionality.HIGH;
    private final Functionality mediumFunc = Functionality.MEDIUM;
    private final Functionality lowFunc = Functionality.LOW;

    @Override
    public Appliance createAppliance(String appliance) {
        double minNonActiveConsumption = 0.01;
        double maxNonActiveConsumption = 0.05;
        double minActiveConsumption = 0.2;
        double maxActiveConsumption = 0.5;
        if ("Refrigerator".equals(appliance)) {
            return new Refrigerator(type, appliance, highFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("TV".equals(appliance)) {
            return new TV(type, appliance, mediumFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("Smart lightning".equals(appliance)) {
            return new SmartLightning(type, appliance, highFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("Gaming console".equals(appliance)) {
            return new GamingConsole(type, appliance, lowFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("AC".equals(appliance)) {
            return new AirConditioner(type, appliance, highFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("Microwave".equals(appliance)) {
            return new Microwave(type, appliance, mediumFunc.getFunctionality(),
                    getRandomConsumption(minNonActiveConsumption, maxNonActiveConsumption),
                    getRandomConsumption(minActiveConsumption, maxActiveConsumption));
        } else if ("Washing machine".equals(appliance)) {
            return new WashingMachine(type, appliance, highFunc.getFunctionality(),
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
