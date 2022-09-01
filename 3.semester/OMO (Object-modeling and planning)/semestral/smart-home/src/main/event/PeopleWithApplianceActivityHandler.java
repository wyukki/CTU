package main.event;

import main.appliances.*;
import main.appliances.components.Food;
import main.appliances.states.Broken;
import main.appliances.states.TurnedOff;
import main.appliances.states.TurnedOn;
import main.building.Room;
import main.people.Family;
import main.people.Father;
import main.people.Person;

import java.util.List;

public class PeopleWithApplianceActivityHandler implements PeopleWithApplianceActivity {

    @Override
    public void reactToDayTime(DayTimer timer, List<Room> rooms) {
        if (timer == null || rooms == null) {
            return;
        }
        if (timer.getDayTime() == DayTime.MORNING || timer.getDayTime() == DayTime.AFTERNOON) {
            rooms.forEach(Room::openBlinds);
            System.out.println("Blinds opened in all rooms.");
            for (Room room : rooms) {
                SmartLightning smartLightning = (SmartLightning) room.getAppliance("Smart lightning");
                if (smartLightning != null) {
                    if (!(smartLightning.getContext().getState() instanceof Broken)) {
                        smartLightning.getSmartLightningAPI().turnOffSmartLightning();
                    } else {
                        System.out.println("Smart lightning is broken");
                    }
                }
            }
        } else if (timer.getDayTime() == DayTime.EVENING || timer.getDayTime() == DayTime.NIGHT) {
            rooms.forEach(Room::closeBlinds);
            System.out.println("Blinds closed in all rooms.");
            for (Room room : rooms) {
                SmartLightning smartLightning = (SmartLightning) room.getAppliance("Smart lightning");
                if (smartLightning != null) {
                    if (timer.getDayTime() == DayTime.EVENING) {
                        if (room.getPeople().isEmpty()) {
                            if (!(smartLightning.getContext().getState() instanceof Broken)) {
                                smartLightning.getSmartLightningAPI().turnOffSmartLightning();
                            } else {
                                System.out.println("Smart lightning is broken");
                            }
                        } else {
                            if (!(smartLightning.getContext().getState() instanceof Broken)) {
                                smartLightning.getSmartLightningAPI().turnOnSmartLightning();
                            } else {
                                System.out.println("Smart lightning is broken");
                            }
                        }
                    } else {
                        if (!(smartLightning.getContext().getState() instanceof Broken)) {
                            smartLightning.getSmartLightningAPI().turnOffSmartLightning();
                        } else {
                            System.out.println("Smart lightning is broken");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reactToColdWeather(DayTimer timer, List<Room> rooms) {
        if (timer == null || rooms == null) {
            return;
        }
        if (timer.getWeather() == Weather.COLD
                || timer.getWeather() == Weather.RAINY
                || timer.getWeather() == Weather.SNOWY) {
            System.out.println("It is cold outside.");
            rooms.forEach(Room::closeWindow);
            System.out.println("All windows are closed.");
            for (Room room : rooms) {
                Appliance AC = room.getAppliance("AC");
                if (AC != null && AC.getContext().getState() instanceof TurnedOn) {
                    System.out.println("AC in " + room.getName() + " turned off");
                    AC.turnOff();
                }
                Appliance boiler = room.getAppliance("Boiler");
                if (boiler != null) {
                    if (boiler.getContext().getState() instanceof Broken) {
                        System.out.println("Boiler is broken!");
                        return;
                    }
                    if (timer.getSeason() == Season.WINTER) {
                        boiler.turnOn();
                        System.out.println("Boiler in " + room.getName() + " turned on");
                    } else {
                        boiler.turnOff();
                        System.out.println("Boiler in " + room.getName() + " turned off");
                    }
                }
            }
        }
    }

    @Override
    public void reactToHotWeather(DayTimer timer, List<Room> rooms) {
        if (timer == null || rooms == null) {
            return;
        }
        if (timer.getWeather() == Weather.HOT) {
            System.out.println("It is hot outside.");
            if (timer.getDayTime() == DayTime.AFTERNOON) {
                rooms.forEach(Room::closeWindow);
                System.out.println("All windows are closed.");
                for (Room room : rooms) {
                    AirConditioner AC = (AirConditioner) room.getAppliance("AC");
                    if (AC != null && AC.getContext().getState() instanceof TurnedOff) {
                        AC.getAcAPI().turnOnAC();
                        System.out.println("Turned on AC in " + room.getName());
                    }
                    if (timer.getSeason() == Season.SUMMER) {
                        Boiler boiler = (Boiler) room.getAppliance("Boiler");
                        if (boiler != null && boiler.getContext().getState() instanceof TurnedOn) {
                            boiler.getBoilerAPI().turnOffBoiler();
                            System.out.println(".\nTurned off Boiler in " + room.getName() + ".");
                        }
                    }
                }
            } else {
                rooms.forEach(Room::openWindow);
                System.out.println("All windows are opened.");
                for (Room room : rooms) {
                    AirConditioner AC = (AirConditioner) room.getAppliance("AC");
                    if (AC != null && AC.getContext().getState() instanceof TurnedOn) {
                        AC.getAcAPI().turnOffAC();
                        System.out.print("Turned off AC in " + room.getName());
                    }
                    if (timer.getSeason() == Season.SUMMER) {
                        Boiler boiler = (Boiler) room.getAppliance("Boiler");
                        if (boiler != null && boiler.getContext().getState() instanceof TurnedOn) {
                            boiler.getBoilerAPI().turnOnBoiler();
                            System.out.print(".\nTurned off Boiler in " + room.getName() + ".");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkAppliances(Father father, Appliance appliance, Room room) {
        if (father == null || appliance == null || room == null) {
            return;
        }
        if (appliance.getContext().getState() instanceof Broken) {
            this.repairAppliance(father, appliance);
        }
    }

    @Override
    public void repairAppliance(Father father, Appliance appliance) {
        if (father == null || appliance == null) {
            return;
        }
        if (!(appliance.getContext().getState() instanceof Broken)) {
            return;
        }
        System.out.println("Father reads " + appliance.getManual() + ".");
        System.out.println("Father repaired " + appliance.getName() + "."
                + " It works now!");
        appliance.resetFunctionality();
        appliance.getContext().setState(new TurnedOn());
    }

    @Override
    public void cookMeal(Person person, Refrigerator refrigerator, Stove stove) {
        if (person == null || refrigerator == null || stove == null) {
            return;
        }
        if (person.getIdentification() == Family.NURSE || person.getIdentification() == Family.MOTHER) {
            if (person.getLocation() == null || !person.getLocation().getName().equals("Kitchen")) {
                return;
            }
            if (refrigerator.getContext().getState() instanceof Broken) {
                System.out.println("Refrigerator is broken! " + person.getIdentification() + " cannot cook meal!");
                return;
            }
            if (stove.getContext().getState() instanceof Broken) {
                System.out.println("Stove is broken! " + person.getIdentification() + " cannot cook meal!");
                return;
            }
            refrigerator.turnOn();
            refrigerator.getRefrigeratorAPI().checkRefrigerator();
            if (!refrigerator.getFood().isEmpty()) {
                Food food = refrigerator.getFood().remove(0); //change indexes
                stove.getStoveAPI().cookOnStove();
                stove.getStoveAPI().stopCookingOnStove();
                System.out.println(person.getIdentification().name() + " cooked " +
                        food.getFoodType() + " on stove.");
            } else {
                System.out.println("Refrigerator is empty, buy some food!");
            }
            refrigerator.getRefrigeratorAPI().turnOffRefrigerator();
        }
    }

    @Override
    public void warmUpFood(Person person, Microwave microwave, Refrigerator refrigerator) {
        if (person == null || microwave == null || refrigerator == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY) {
            if (person.getLocation() == null || !person.getLocation().getName().equals("Kitchen")) {
                return;
            }
            if (refrigerator.getContext().getState() instanceof Broken) {
                System.out.println("Refrigerator is broken! " + person.getIdentification() + " cannot warm up food!");
                return;
            }
            if (microwave.getContext().getState() instanceof Broken) {
                System.out.println("Microwave is broken! " + person.getIdentification() + " cannot warm up food!");
                return;
            }
            refrigerator.turnOn();
            refrigerator.getRefrigeratorAPI().checkRefrigerator();
            if (!refrigerator.getFood().isEmpty()) {
                Food food = refrigerator.getFood().remove(0); //change indexes
                microwave.getMicrowaveAPI().cookInMicrowave();
                microwave.getMicrowaveAPI().stopCookingInMicrowave();
                System.out.println(person.getIdentification().name() + " warmed up " +
                        food.getFoodType() + " in microwave.");
            } else {
                System.out.println("Refrigerator is empty, buy some food!");
            }
            refrigerator.getRefrigeratorAPI().turnOffRefrigerator();
        }
    }

    @Override
    public void turnOnLights(Person person, SmartLightning smartLightning) {
        if (person == null || smartLightning == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY) {
//            smartLightning.getSmartLightningAPI().turnOnSmartLightning();
            if (smartLightning.getContext().getState() instanceof Broken) {
                System.out.println("Smart lightning is broken!");
                return;
            }
            smartLightning.getSmartLightningAPI().turnOnSmartLightning();
            System.out.println(person.getIdentification().name() + " turned on lights.");
        }
    }

    @Override
    public void turnOffLights(Person person, SmartLightning smartLightning) {
        if (person == null || smartLightning == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY) {
//            smartLightning.getSmartLightningAPI().turnOffSmartLightning();
            if (smartLightning.getContext().getState() instanceof Broken) {
                System.out.println("Smart lightning is broken!");
                return;
            }
            smartLightning.getSmartLightningAPI().turnOffSmartLightning();
            System.out.println(person.getIdentification().name() + " turned off lights.");
        }
    }

    @Override
    public void watchFootballOnTV(Father father, TV TV) {
        if (father == null || TV == null
                || father.getLocation() == null || !father.getLocation().getName().equals("Living room")) {
            return;
        }
        if (TV.getContext().getState() instanceof Broken) {
            System.out.println("TV is broken. Father cannot watch football!");
            return;
        }
        TV.getTvAPI().turnOnTV();
        System.out.println(father.getIdentification() + " watched a football match on TV.");
        TV.getTvAPI().turnOffTV();
    }

    @Override
    public void watchTVSeries(Person person, TV TV) {
        if (person == null || TV == null
                || person.getLocation() == null || !person.getLocation().getName().equals("Living room")) {
            return;
        }
        if (person.getIdentification() == Family.ELDER_SISTER
                || person.getIdentification() == Family.MOTHER) {
            if (TV.getContext().getState() instanceof Broken) {
                System.out.println("TV is broken " + person.getIdentification() + " cannot watch TV series");
                return;
            }
            TV.getTvAPI().turnOnTV();
            System.out.println(person.getIdentification() + " watched a TV series.");
            TV.getTvAPI().turnOffTV();
        }
    }

    @Override
    public void playConsole(Person person, GamingConsole gamingConsole, TV TV) {
        if (person == null || gamingConsole == null || TV == null
                || person.getLocation() == null || !person.getLocation().getName().equals("Children bedroom")) {
            return;
        }
        if (person.getIdentification() == Family.ELDER_BROTHER
                || person.getIdentification() == Family.ELDER_SISTER) {
            if (TV.getContext().getState() instanceof Broken) {
                System.out.println("TV is broken! " + person.getIdentification() + " cannot play console!");
                return;
            }
            if (gamingConsole.getContext().getState() instanceof Broken) {
                System.out.println("Gaming console is broken! " + person.getIdentification() + " cannot play console");
                return;
            }
            TV.getTvAPI().turnOnTV();
            gamingConsole.getGamingConsoleAPI().playConsole();
            System.out.println(person.getIdentification() + " played "
                    + gamingConsole.getGamingConsoleAPI().getCD() + " on PlayStation5.");
            gamingConsole.getGamingConsoleAPI().stopPlaying();
            TV.getTvAPI().turnOffTV();
        }
    }

    @Override
    public void doLaundry(Person person, WashingMachine washingMachine) {
        if (person == null || washingMachine == null || person.getLocation() == null) {
            return;
        }
        if (person.getLocation() != null) {
            Room personLocation = person.getLocation();
            if (personLocation.getAppliance(washingMachine.getName()) != null) {
                if (person.getIdentification() == Family.NURSE
                        || person.getIdentification() == Family.MOTHER) {
                    if (washingMachine.getContext().getState() instanceof Broken) {
                        System.out.println("Washing machine is broken! " +
                                person.getIdentification() + " cannot do laundry");
                        return;
                    }
                    washingMachine.getWashingMachineAPI().doLaundry();
                    System.out.println("There was a lot of dirty clothes, so "
                            + person.getIdentification() + " did laundry.");
                    washingMachine.getWashingMachineAPI().finishLaundry();
                }
            }
        }
    }

    @Override
    public void watchFilm(Person person, TV TV) {
        if (person == null || TV == null || person.getLocation() == null
                || !person.getLocation().getName().equals("Parents bedroom")) {
            return;
        }
        if (person.getLocation() != null
                && person.getLocation().getName().equals("Parents bedroom")) {
            if (person.getIdentification() == Family.FATHER || person.getIdentification() == Family.MOTHER) {
                if (TV.getContext().getState() instanceof Broken) {
                    System.out.println("TV is broken! " + person.getIdentification() + " cannot watch film!");
                    return;
                }
                TV.getTvAPI().turnOnTV();
                System.out.println(person.getIdentification() + " watches a film.");
                TV.getTvAPI().turnOnTV();
            }
        }
    }

}
