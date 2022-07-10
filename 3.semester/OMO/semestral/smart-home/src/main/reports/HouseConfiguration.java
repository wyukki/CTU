package main.reports;

import main.activityElements.*;
import main.appliances.Appliance;
import main.building.*;
import main.people.Person;
import main.pets.Pet;

import java.util.List;

public final class HouseConfiguration {
    private final House house;
    private final List<Person> family;
    private final List<Pet> pets;
    private final List<SportsElements> sportsElements;
    private final Car car;

    public HouseConfiguration(House house, List<Person> family,
                              List<Pet> pets, List<SportsElements> sportsElements, Car car) {
        this.house = house;
        this.family = family;
        this.pets = pets;
        this.sportsElements = sportsElements;
        this.car = car;
    }

    /**
     * Creates report for house configuration in the beginning of simulation.
     */
    public void createReport() {
        System.out.println("----------------------------------------");
        System.out.println("House configuration: ");
        System.out.println("House: ");
        for (Floor floor : house.getFloors()) {
            System.out.println("\tFloor " + floor.getFloor() + " rooms: ");
            for (Room room : floor.getRooms()) {
                System.out.print("\t\t" + room.getName() + " appliances: ");
                for (Appliance appliance : room.getAppliances()) {
                    System.out.print(appliance.getName() + ", ");
                }
                if (room.getName().equals("Garage")) {
                    System.out.print("Sport elements: ");
                    for (SportsElements sportsElement : sportsElements) {
                        System.out.print(sportsElement.getElementType() + ", ");
                    }
                    System.out.print("Car: " + car.getModel() + " " + car.getColor() + ", ");
                }
                System.out.println("Windows: " + room.getWindows().size());
            }
        }
        System.out.println("In this house lives: ");
        for (Person person : this.family) {
            System.out.println("\t" + person.getIdentification());
        }
        for (Pet pet : pets) {
            System.out.println("\t" + pet.getNickname());
        }
        System.out.println("----------------------------------------");
    }
}
