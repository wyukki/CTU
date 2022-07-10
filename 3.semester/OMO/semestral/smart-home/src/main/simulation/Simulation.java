package main.simulation;

import main.activityElements.*;
import main.appliances.*;
import main.building.*;
import main.building.houseBuilder.*;
import main.event.*;
import main.people.*;
import main.pets.Cat;
import main.pets.Dog;
import main.pets.Minipig;
import main.pets.Pet;
import main.reports.HouseConfiguration;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;

public final class Simulation {
    private House house;
    private final List<Room> rooms = new ArrayList<>();
    private Car car;
    private final List<Person> family = new ArrayList<>();
    private final List<Pet> pets = new ArrayList<>();
    private final List<SportsElements> sportsElements = new ArrayList<>();
    private final PeopleLifeActivity lifeHandler = new PeopleLifeActivityHandler();
    private final PeopleWithApplianceActivity applianceHandler = new PeopleWithApplianceActivityHandler();
    private final List<Appliance> appliances = new ArrayList<>();
    private DayTimer timer;

    /**
     * Starts simulation of the life in smart home.
     *
     * @throws FileNotFoundException
     */
    public void start() throws FileNotFoundException {
        chooseConfiguration();
        PrintStream stream = new PrintStream("report.txt");
        System.setOut(stream);
        createFamily();
        createCar();
        createPets();
        ApplianceEventHandler applianceEventHandler = new ApplianceEventHandler((Father) getPerson(Family.FATHER));
        appliances.forEach(appliance -> appliance.attach(applianceEventHandler));
        HouseConfiguration configuration = new HouseConfiguration(house, family, pets, sportsElements, car);
        configuration.createReport();
        scenario();
    }

    /**
     * Asks user to choose either winter or summer house configuration.
     */
    private void chooseConfiguration() {
        System.out.println("Enter number for house configuration:");
        System.out.println("1. Winter house");
        System.out.println("2. Summer house");
        int configuration = 0;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                configuration = sc.nextInt();
                if (configuration == 1 || configuration == 2) {
                    break;
                } else {
                    System.err.println("Enter 1 or 2!");
                }
            } catch (InputMismatchException exception) {
                System.err.println("Enter 1 or 2!");
            }
        }
        if (configuration == 1) {
            createWinterHouse();
        } else {
            createSummerHouse();
        }
    }

    private void createSummerHouse() {
        timer = new DayTimer("Summer");
        HouseDirector director = new HouseDirector();
        this.house = director.createSummerHouse(new Builder());
        createSportElementsForSummer();
        for (Floor floor : house.getFloors()) {
            rooms.addAll(floor.getRooms());
        }
        for (Room room : rooms) {
            appliances.addAll(room.getAppliances());
        }
    }

    private void createWinterHouse() {
        timer = new DayTimer("Winter");
        HouseDirector director = new HouseDirector();
        this.house = director.createWinterHouse(new Builder());
        createSportElementsForWinter();
        for (Floor floor : house.getFloors()) {
            rooms.addAll(floor.getRooms());
        }
        for (Room room : rooms) {
            appliances.addAll(room.getAppliances());
        }
    }

    private void createFamily() {
        Person father = new Father(Family.FATHER, getRoom("Parents bedroom"));
        Person mother = new Mother(Family.MOTHER, getRoom("Parents bedroom"));
        Person brother = new ElderBrother(Family.ELDER_BROTHER, getRoom("Children bedroom"));
        Person sister = new ElderSister(Family.ELDER_SISTER, getRoom("Children bedroom"));
        Person baby = new Baby(Family.BABY, getRoom("Parents bedroom"));
        Person nurse = new Nurse(Family.NURSE, null);
        family.add(father);
        family.add(mother);
        family.add(brother);
        family.add(sister);
        family.add(baby);
        family.add(nurse);
    }

    private void createPets() {
        Pet dog = new Dog("Richie", getRoom("Living room"));
        Pet cat = new Cat("Garfield", getRoom("Living room"));
        Pet minipig = new Minipig("Jamie", getRoom("Living room"));
        pets.add(dog);
        pets.add(cat);
        pets.add(minipig);
    }

    private void createCar() {
        this.car = Car.getInstance("Black", "Porsche 911");
    }

    private void createSportElementsForWinter() {
        SportsElements bicycle = new Bicycle();
        SportsElements skis1 = new Skis();
        SportsElements skis2 = new Skis();
        sportsElements.add(bicycle);
        sportsElements.add(skis1);
        sportsElements.add(skis2);
    }

    private void createSportElementsForSummer() {
        SportsElements bicycle1 = new Bicycle();
        SportsElements bicycle2 = new Bicycle();
        SportsElements skateboard = new Skateboard();
        sportsElements.add(bicycle1);
        sportsElements.add(bicycle2);
        sportsElements.add(skateboard);
    }

    /**
     * Scenario for life in the house.
     * It will run for simulationLength day.
     */
    private void scenario() {
        int simulationLength = 7; //if you want to change simulation length, change value of this variable

        System.out.println("SUMMER SIMULATION");
        try {
            for (int day = 1; day <= simulationLength; ++day) {
                System.out.println("\nDay " + timer.getDayCounter() + ":");
                System.out.println("\nDaytime: " + timer.getDayTime());
                lifeHandler.getUp(family);
                applianceHandler.reactToDayTime(timer, rooms);
                if (day < 6) {
                    simulateMorningWeekday();
                    simulateAfternoonWeekday(day);
                    simulateEveningWeekday(day);
                    simulateNight();
                } else {
                    simulateMorningWeekend();
                    simulateAfternoonWeekend(day);
                    simulateEveningWeekend();
                    simulateNight();
                    //consumption report in the end of every weekend
                    if (day % 7 == 0) {
                        generateConsumptionReport();
                    }
                }
            }
        } catch (NoSuchElementException exception) {
            System.err.println("Some appliance does not exist, or room doesn't have that appliance");
            exception.printStackTrace();
        }
    }

    private Person getPerson(Family member) {
        for (Person person : family) {
            if (person.getIdentification() == member) {
                return person;
            }
        }
        throw new NoSuchElementException();
    }

    private Room getRoom(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                return room;
            }
        }
        throw new NoSuchElementException();
    }

    private Pet getPet(String petType) {
        for (Pet pet : pets) {
            if (pet.getType().equals(petType)) {
                return pet;
            }
        }
        throw new NoSuchElementException();
    }

    private SportsElements getSportElement(SportsElementType type) {
        for (SportsElements sportsElement : sportsElements) {
            if (sportsElement.getElementType() == type) {
                return sportsElement;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Entity of scenario, simulates morning on weekdays.
     */
    private void simulateMorningWeekday() {
        applianceHandler.reactToHotWeather(timer, rooms);
        applianceHandler.reactToColdWeather(timer, rooms);
        lifeHandler.nurseComesHome((Nurse) getPerson(Family.NURSE), getRoom("Kitchen"));
        applianceHandler.turnOnLights(getPerson(Family.NURSE),
                (SmartLightning) getRoom("Kitchen").getAppliance("Smart lightning"));
        applianceHandler.cookMeal(getPerson(Family.NURSE),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"),
                (Stove) getRoom("Kitchen").getAppliance("Stove"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Kitchen"));
        lifeHandler.eat(getPerson(Family.FATHER));
        lifeHandler.eat(getPerson(Family.MOTHER));
        lifeHandler.doDishes(getPerson(Family.NURSE));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Parents bedroom"));
        lifeHandler.feedBaby(getPerson(Family.MOTHER), (Baby) getPerson(Family.BABY));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Garage"));
        lifeHandler.leaveHouseForWork(getPerson(Family.FATHER), car);
        lifeHandler.leaveHouseForWork(getPerson(Family.MOTHER), car);
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Kitchen"));
        lifeHandler.eat(getPerson(Family.ELDER_SISTER));
        lifeHandler.eat(getPerson(Family.ELDER_BROTHER));
        lifeHandler.doDishes(getPerson(Family.ELDER_BROTHER));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_BROTHER),
                getPet("Dog"), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_SISTER),
                getPet("Minipig"), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Parents bedroom"));
        lifeHandler.takeCareOfBaby(getPerson(Family.NURSE), (Baby) getPerson(Family.BABY));
        System.out.println();
        timer.incrementTime();
        appliances.forEach(Appliance::decrementFunctionality);
    }

    /**
     * Entity of scenario, simulates morning on weekends.
     */
    private void simulateMorningWeekend() {
        applianceHandler.reactToColdWeather(timer, rooms);
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_BROTHER),
                getPet("Dog"), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_SISTER),
                getPet("Minipig"), getRoom("Living room"));
        applianceHandler.cookMeal(getPerson(Family.NURSE),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"),
                (Stove) getRoom("Kitchen").getAppliance("Stove"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Kitchen"));
        lifeHandler.eat(getPerson(Family.FATHER));
        lifeHandler.eat(getPerson(Family.MOTHER));
        lifeHandler.eat(getPerson(Family.ELDER_BROTHER));
        lifeHandler.eat(getPerson(Family.ELDER_SISTER));
        lifeHandler.doDishes(getPerson(Family.MOTHER));
        lifeHandler.orderFood(getPerson(Family.FATHER),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"));
        timer.incrementTime();
    }

    /**
     * Entity of scenario, simulates afternoon on weekdays.
     *
     * @param day number of day
     */
    private void simulateAfternoonWeekday(int day) {
        applianceHandler.reactToHotWeather(timer, rooms);
        applianceHandler.reactToColdWeather(timer, rooms);
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Garage"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Garage"));
        if (timer.getSeason() == Season.WINTER) {
            lifeHandler.leaveHouseToSport(getPerson(Family.ELDER_BROTHER),
                    getSportElement(SportsElementType.SKIS));
        } else {
            lifeHandler.leaveHouseToSport(getPerson(Family.ELDER_BROTHER),
                    getSportElement(SportsElementType.SKATEBOARD));
        }
        lifeHandler.leaveHouseToSport(getPerson(Family.ELDER_SISTER),
                getSportElement(SportsElementType.BICYCLE));
        lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Kitchen"));
        applianceHandler.cookMeal(getPerson(Family.NURSE),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"),
                (Stove) getRoom("Kitchen").getAppliance("Stove"));
        lifeHandler.eat(getPerson(Family.NURSE));
        lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Parents bedroom"));
        lifeHandler.takeCareOfBaby(getPerson(Family.NURSE), (Baby) getPerson(Family.BABY));
        if (timer.getSeason() == Season.WINTER) {
            lifeHandler.returnHomeFromSport(getPerson(Family.ELDER_BROTHER),
                    getSportElement(SportsElementType.SKIS),
                    getRoom("Garage"));
        } else {
            lifeHandler.returnHomeFromSport(getPerson(Family.ELDER_BROTHER),
                    getSportElement(SportsElementType.SKATEBOARD),
                    getRoom("Garage"));
        }
        lifeHandler.returnHomeFromSport(getPerson(Family.ELDER_SISTER),
                getSportElement(SportsElementType.BICYCLE),
                getRoom("Garage"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Kitchen"));
        applianceHandler.warmUpFood(getPerson(Family.ELDER_BROTHER),
                (Microwave) getRoom("Kitchen").getAppliance("Microwave"),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"));
        lifeHandler.eat(getPerson(Family.ELDER_BROTHER));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Living room"));
        pets.forEach(pet -> lifeHandler.feedPet(getPerson(Family.ELDER_SISTER), pet));

        if (day % 2 == 1) {
            if (timer.getSeason() == Season.WINTER) {
                lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Bathroom"));
                applianceHandler.doLaundry(getPerson(Family.NURSE),
                        (WashingMachine) getRoom("Bathroom")
                                .getAppliance("Washing machine"));
            } else {
                lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Kitchen"));
                applianceHandler.doLaundry(getPerson(Family.NURSE),
                        (WashingMachine) getRoom("Kitchen")
                                .getAppliance("Washing machine"));
            }
        }
        lifeHandler.orderFood(getPerson(Family.NURSE),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"));

        System.out.println();
        timer.incrementTime();
        appliances.forEach(Appliance::decrementFunctionality);
    }

    /**
     * Entity of scenario, simulates afternoon on weekends.
     *
     * @param day number of day
     */
    private void simulateAfternoonWeekend(int day) {
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Living room"));
        applianceHandler.watchFootballOnTV((Father) getPerson(Family.FATHER),
                (TV) getRoom("Living room").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Living room"));
        pets.forEach(pet -> lifeHandler.feedPet(getPerson(Family.ELDER_SISTER), pet));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Children bedroom"));
        applianceHandler.playConsole(getPerson(Family.ELDER_BROTHER),
                (GamingConsole) getRoom("Children bedroom").getAppliance("Gaming console"),
                (TV) getRoom("Children bedroom").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Garage"));
        lifeHandler.leaveHouseToSport(getPerson(Family.ELDER_SISTER),
                getSportElement(SportsElementType.BICYCLE));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Parents bedroom"));
        lifeHandler.feedBaby(getPerson(Family.MOTHER), (Baby) getPerson(Family.BABY));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Garage"));
        lifeHandler.leaveHouseToSport(getPerson(Family.FATHER), getSportElement(SportsElementType.BICYCLE));
        if (day % 7 == 6) {
            if (timer.getSeason() == Season.WINTER) {
                lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Bathroom"));
                applianceHandler.doLaundry(getPerson(Family.MOTHER),
                        (WashingMachine) getRoom("Bathroom")
                                .getAppliance("Washing machine"));
            } else {
                lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Kitchen"));
                applianceHandler.doLaundry(getPerson(Family.MOTHER),
                        (WashingMachine) getRoom("Kitchen")
                                .getAppliance("Washing machine"));
            }
        }
        timer.incrementTime();
    }

    /**
     * Entity of scenario, simulates evening on weekdays.
     *
     * @param day number of day
     */
    private void simulateEveningWeekday(int day) {
        applianceHandler.reactToDayTime(timer, rooms);
        applianceHandler.reactToHotWeather(timer, rooms);
        applianceHandler.reactToColdWeather(timer, rooms);
        lifeHandler.changeRoom(getPerson(Family.NURSE), getRoom("Kitchen"));
        applianceHandler.cookMeal(getPerson(Family.NURSE),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"),
                (Stove) getRoom("Kitchen").getAppliance("Stove"));
        lifeHandler.returnHomeFromWork(getPerson(Family.FATHER), car,
                getRoom("Garage"), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Living room"));
        lifeHandler.returnHomeFromWork(getPerson(Family.MOTHER), car,
                getRoom("Parents bedroom"), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.BABY), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Kitchen"));
        lifeHandler.eat(getPerson(Family.FATHER));
        lifeHandler.eat(getPerson(Family.MOTHER));
        lifeHandler.eat(getPerson(Family.ELDER_BROTHER));
        lifeHandler.eat(getPerson(Family.ELDER_SISTER));
        lifeHandler.eat(getPerson(Family.NURSE));
        lifeHandler.eat(getPerson(Family.FATHER));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.FATHER),
                getPet("Dog"), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_BROTHER),
                getPet("Minipig"), getRoom("Living room"));
        lifeHandler.feedBaby(getPerson(Family.MOTHER), (Baby) getPerson(Family.BABY));
        lifeHandler.takeCareOfBaby(getPerson(Family.MOTHER), (Baby) getPerson(Family.BABY));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Living room"));
        applianceHandler.watchFootballOnTV((Father) getPerson(Family.FATHER),
                (TV) getRoom("Living room").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Parents bedroom"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Living room"));
        applianceHandler.watchTVSeries(getPerson(Family.ELDER_SISTER),
                (TV) getRoom("Living room").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Parents bedroom"));
        lifeHandler.changeRoom(getPerson(Family.BABY), getRoom("Parents bedroom"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Children bedroom"));
        applianceHandler.playConsole(getPerson(Family.ELDER_BROTHER),
                (GamingConsole) getRoom("Children bedroom").getAppliance("Gaming console"),
                (TV) getRoom("Children bedroom").getAppliance("TV"));

        appliances.forEach(Appliance::decrementFunctionality);
        //father checks functionality of all appliances every second day,
        // if something is broken, he reads manual and repairs it
        if (day % 2 == 1) {
            repairAllBrokenAppliancesAtHome();
        }
        timer.incrementTime();
    }

    /**
     * Entity of scenario, simulates evening on weekends.
     */
    private void simulateEveningWeekend() {
        lifeHandler.returnHomeFromSport(getPerson(Family.ELDER_SISTER),
                getSportElement(SportsElementType.BICYCLE),
                getRoom("Garage"));
        lifeHandler.returnHomeFromSport(getPerson(Family.FATHER),
                getSportElement(SportsElementType.BICYCLE),
                getRoom("Garage"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Living room"));
        applianceHandler.watchTVSeries(getPerson(Family.ELDER_SISTER),
                (TV) getRoom("Living room").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Living room"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.ELDER_BROTHER),
                getPet("Dog"), getRoom("Living room"));
        lifeHandler.walkWithPet(getPerson(Family.FATHER),
                getPet("Minipig"), getRoom("Living room"));
        applianceHandler.warmUpFood(getPerson(Family.MOTHER),
                (Microwave) getRoom("Kitchen").getAppliance("Microwave"),
                (Refrigerator) getRoom("Kitchen").getAppliance("Refrigerator"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Kitchen"));
        lifeHandler.changeRoom(getPerson(Family.FATHER), getRoom("Kitchen"));
        lifeHandler.eat(getPerson(Family.FATHER));
        lifeHandler.eat(getPerson(Family.MOTHER));
        lifeHandler.eat(getPerson(Family.ELDER_BROTHER));
        lifeHandler.eat(getPerson(Family.ELDER_SISTER));
        lifeHandler.doDishes(getPerson(Family.ELDER_BROTHER));
        repairAllBrokenAppliancesAtHome();
        lifeHandler.changeRoom(getPerson(Family.ELDER_SISTER), getRoom("Children bedroom"));
        applianceHandler.playConsole(getPerson(Family.ELDER_SISTER),
                (GamingConsole) getRoom("Children bedroom").getAppliance("Gaming console"),
                (TV) getRoom("Children bedroom").getAppliance("TV"));
        lifeHandler.changeRoom(getPerson(Family.ELDER_BROTHER), getRoom("Children bedroom"));
        lifeHandler.changeRoom(getPerson(Family.MOTHER), getRoom("Parents bedroom"));
        lifeHandler.feedBaby(getPerson(Family.MOTHER), (Baby) getPerson(Family.BABY));
        applianceHandler.watchFilm(getPerson(Family.MOTHER),
                (TV) getRoom("Parents bedroom").getAppliance("TV"));
        timer.incrementTime();
    }

    /**
     * Entity of scenario, simulates night.
     */
    private void simulateNight() {
        applianceHandler.reactToHotWeather(timer, rooms);
        applianceHandler.reactToColdWeather(timer, rooms);
        applianceHandler.reactToDayTime(timer, rooms);
        applianceHandler.turnOffLights(getPerson(Family.FATHER),
                (SmartLightning) getRoom("Parents bedroom").getAppliance("Smart lightning"));
        lifeHandler.goToSleep(family);
        timer.incrementTime();
    }

    /**
     * Father repairs all broken appliances at home.
     */
    private void repairAllBrokenAppliancesAtHome() {
        for (Room room : rooms) {
            Father father = (Father) getPerson(Family.FATHER);
            for (Appliance appliance : father.getBrokenAppliances()) {
                applianceHandler.checkAppliances((Father) getPerson(Family.FATHER), appliance, room);
            }
        }
    }

    /**
     * In the end of simulation generates consumption report.
     */
    private void generateConsumptionReport() {
        System.out.println("---------------------------------------------------------");
        System.out.println("APPLIANCES CONSUMPTION REPORT");
        DecimalFormat df = new DecimalFormat("#0.00");
        for (Room room : rooms) {
            for (Appliance appliance : room.getAppliances()) {
                System.out.println(appliance.getName() + " in " + room.getName()
                        + " consumed " + df.format(appliance.getConsumption()) + appliance.getUnitOfMeasurement());
            }
        }
        System.out.println("----------------------------------------------------------");
    }
}
