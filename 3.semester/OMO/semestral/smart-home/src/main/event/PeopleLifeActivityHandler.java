package main.event;

import main.activityElements.*;
import main.appliances.*;
import main.building.Room;
import main.people.*;
import main.pets.Pet;

import java.util.List;

public class PeopleLifeActivityHandler implements PeopleLifeActivity {

    @Override
    public void getUp(List<Person> family) {
        if (family == null) {
            return;
        }
        for (Person person : family) {
            if (person.isAsleep()) {
                person.setAsleep(false);
            }
        }
        System.out.println("Family got up.");
    }

    @Override
    public void goToSleep(List<Person> family) {
        if (family == null) {
            return;
        }
        for (Person person : family) {
            if (!person.isAsleep()) {
                person.setAsleep(true);
            }
        }
        System.out.println("Family went to sleep");
    }

    @Override
    public void eat(Person person) {
        if (person == null) {
            return;
        }
        if (person.getLocation() != null &&
                person.getLocation().getName().equals("Kitchen")) {
            System.out.println(person.getIdentification() + " ate prepared food.");
        } else {
            System.out.println(person.getIdentification() + " must go to the kitchen!");
        }
    }

    @Override
    public void doDishes(Person person) {
        if (person == null) {
            return;
        }
        if (person.getLocation() != null &&
                person.getLocation().getName().equals("Kitchen")) {
            System.out.println(person.getIdentification() + " did dishes.");
        }
    }

    @Override
    public void leaveHouseForWork(Person person, Car car) {
        if (person == null || car == null) {
            return;
        }
        if (person.getIdentification() == Family.FATHER
                || person.getIdentification() == Family.MOTHER
                && person.getLocation() != null) {
            person.setLocation(null);
            if (!car.isInUse()) {
                car.setUser(person);
                car.setInUse(true);
                System.out.println(person.getIdentification() + " went for a work and took the car.");
            } else {
                System.out.println("Car is already in use by " + car.getUser().getIdentification()
                        + ". " + person.getIdentification() + " must take a bus to get to work.");
            }
        }
    }

    @Override
    public void leaveHouseToSport(Person person, SportsElements sportsElement) {
        if (person == null || sportsElement == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY
                && person.getIdentification() != Family.NURSE
                && person.getLocation() != null) {
            person.setLocation(null);
            if (!sportsElement.isInUse()) {
                sportsElement.setInUse(true);
                sportsElement.setUser(person);
                System.out.println(person.getIdentification() + " took a " + sportsElement.getElementType()
                        + " and went outside.");
            } else {
                System.out.println("The sport element" + sportsElement.getElementType()
                        + " is already in use by " + sportsElement.getUser().getIdentification() + ".");
            }
        }
    }

    @Override
    public void changeRoom(Person person, Room room) {
        if (room == null || person == null) {
            return;
        }
        if (person.getLocation() == room) {
            System.out.println(person.getIdentification() + " is already in " + room.getName());
            return;
        }
        Room prevRoom = room;
        if (room.getPeople().isEmpty()) {
            SmartLightning smartLightning = (SmartLightning) room.getAppliance("Smart lightning");
            if (smartLightning != null) {
                smartLightning.getSmartLightningAPI().turnOnSmartLightning();
            }
        }
        person.setLocation(room);
        if (prevRoom.getPeople().isEmpty()) {
            SmartLightning smartLightning = (SmartLightning) prevRoom.getAppliance("Smart lightning");
            if (smartLightning != null) {
                smartLightning.getSmartLightningAPI().turnOffSmartLightning();
            }
        }
        System.out.println(person.getIdentification() + " went to " + room.getName() + ".");
    }

    @Override
    public void orderFood(Person person, Refrigerator refrigerator) {
        if (person == null || refrigerator == null) {
            return;
        }
        refrigerator.addFood();
        System.out.println(person.getIdentification() + " ordered some food in Wolt. Now " +
                "family has something to eat.");
    }

    @Override
    public void returnHomeFromWork(Person person, Car car, Room garage, Room livingRoom) {
        if (person == null || car == null || garage == null || livingRoom == null) {
            return;
        }
        if ((person.getIdentification() == Family.FATHER || person.getIdentification() == Family.MOTHER)
                && person.getLocation() == null) {
            if (car.isInUse()) {
                person.setLocation(garage);
                car.setInUse(false);
                car.setUser(null);
                if (person.getIdentification() == Family.FATHER) {
                    System.out.println(person.getIdentification() + " returned home from work on a car. " +
                            "He is in " + garage.getName() + " now.");
                } else {
                    System.out.println(person.getIdentification() + " returned home from work on a car. " +
                            "She is in " + garage.getName() + " now.");
                }
            } else {
                person.setLocation(garage);
                if (person.getIdentification() == Family.FATHER) {
                    System.out.println(person.getIdentification() + " returned home from work on bus. " +
                            "He is in " + livingRoom.getName() + " now.");
                } else {
                    System.out.println(person.getIdentification() + " returned home from work with a bus. " +
                            "She is in " + livingRoom.getName() + " now.");
                }
            }
        }
    }

    @Override
    public void returnHomeFromSport(Person person, SportsElements sportsElement, Room garage) {
        if (person == null || sportsElement == null || garage == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY && person.getIdentification() != Family.NURSE
                && person.getLocation() == null && sportsElement.isInUse()) {
            person.setLocation(garage);
            sportsElement.setInUse(false);
            sportsElement.setUser(null);
            System.out.println(person.getIdentification() + " returned home from sport. "
                    + sportsElement.getElementType() + " can be used by someone else now!");
        }
    }

    @Override
    public void nurseComesHome(Nurse nurse, Room kitchen) {
        if (nurse == null || kitchen == null) {
            return;
        }
        if (nurse.getLocation() == null) {
            nurse.setLocation(kitchen);
            System.out.println("Nurse came home. She's in the kitchen.");
        }
    }

    @Override
    public void feedPet(Person person, Pet pet) {
        if (person == null || pet == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY
                && person.getLocation() != null
                && pet.getLocation() != null
                && person.getLocation().getName().equals(pet.getLocation().getName())) {
            pet.feed();
            System.out.println(person.getIdentification() + " fed " + pet.getNickname() + ".");
        }
    }

    @Override
    public void walkWithPet(Person person, Pet pet, Room livingRoom) {
        if (person == null || pet == null || livingRoom == null) {
            return;
        }
        if (person.getIdentification() != Family.BABY
                && person.getLocation() != null
                && pet.getLocation() != null
                && person.getLocation().getName().equals(pet.getLocation().getName())) {
            pet.setLocation(null);
            person.setLocation(null);
            pet.walk();
            System.out.println(person.getIdentification() + " walked with a " + pet.getNickname() + ".");
            pet.returnHome();
            pet.setLocation(livingRoom);
        }
    }

    @Override
    public void feedBaby(Person person, Baby baby) {
        if (person == null || baby == null) {
            return;
        }
        if (person.getIdentification() == Family.MOTHER
                || person.getIdentification() == Family.NURSE
                && person.getLocation() != null
                && baby.getLocation() != null
                && person.getLocation().getName().equals(baby.getLocation().getName())) {
            System.out.println(person.getIdentification() + " fed baby.");
        }
    }

    @Override
    public void takeCareOfBaby(Person person, Baby baby) {
        if (person == null || baby == null) {
            return;
        }
        if (person.getIdentification() == Family.MOTHER
                || person.getIdentification() == Family.NURSE
                && person.getLocation() != null
                && baby.getLocation() != null
                && person.getLocation().getName().equals(baby.getLocation().getName())) {
            System.out.println(person.getIdentification() + " took care of baby.");
        }
    }
}
