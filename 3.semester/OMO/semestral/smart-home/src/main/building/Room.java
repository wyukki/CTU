package main.building;

import main.appliances.Appliance;
import main.people.Person;
import main.roomElements.Blinds;
import main.roomElements.Window;

import java.util.ArrayList;
import java.util.List;

public final class Room {
    private final String name;
    private final List<Window> windows = new ArrayList<>();
    private final List<Blinds> blinds = new ArrayList<>();
    private final List<Appliance> appliances = new ArrayList<>();
    private final List<Person> people = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public void addWindow(Window window) {
        this.windows.add(window);
    }

    public void addBlinds(Blinds blinds) {
        this.blinds.add(blinds);
    }

    public void addAppliance(Appliance appliance) {
        this.appliances.add(appliance);
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public String getName() {
        return name;
    }

    public List<Person> getPeople() {
        return people;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public void setPerson(Person person) {
        this.people.add(person);
    }

    public void openWindow() {
        for (Window window : windows) {
            if (!window.isOpen()) {
                window.setOpen(true);
                break;
            }
        }
    }

    public void closeWindow() {
        for (Window window : windows) {
            if (window.isOpen()) {
                window.setOpen(false);
            }
        }
    }

    public void openBlinds() {
        for (Blinds blind : blinds) {
            if (!blind.isOpen()) {
                blind.setOpen(true);
            }
        }
    }

    public void closeBlinds() {
        for (Blinds blind : blinds) {
            if (blind.isOpen()) {
                blind.setOpen(false);
            }
        }
    }

    public Appliance getAppliance(String applianceName) {
        for (Appliance appliance : appliances) {
            if (appliance.getName().equals(applianceName)) {
                return appliance;
            }
        }
        return null;
    }
}
