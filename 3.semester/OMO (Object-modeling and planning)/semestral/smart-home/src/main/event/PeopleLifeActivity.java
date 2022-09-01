package main.event;

import main.activityElements.Car;
import main.activityElements.SportsElements;
import main.appliances.Refrigerator;
import main.building.Room;
import main.people.Baby;
import main.people.Nurse;
import main.people.Person;
import main.pets.Pet;

import java.util.List;

public interface PeopleLifeActivity {
    /**
     * Wakes up entire family.
     *
     * @param family list of people, who lives in the house
     */
    void getUp(List<Person> family);

    /**
     * Entire family goes to sleep.
     *
     * @param family list of people, who lives in the house
     */
    void goToSleep(List<Person> family);

    /**
     * Person eats prepared food.
     *
     * @param person concrete person in the house
     */
    void eat(Person person);

    /**
     * Person does dishes.
     *
     * @param person concrete person in the house
     */
    void doDishes(Person person);

    /**
     * Father or mother leaves a house, one of them can use a car, other goes by bus.
     *
     * @param person father or mother
     * @param car    Car in the house
     */
    void leaveHouseForWork(Person person, Car car);

    /**
     * Person leaves a house and takes one of the sport elements.
     *
     * @param person        concrete person in the house
     * @param sportsElement skis, skateboard or bicycle
     */
    void leaveHouseToSport(Person person, SportsElements sportsElement);

    /**
     * Person changes his location in the house.
     *
     * @param person concrete person in the house
     * @param room   new location
     */
    void changeRoom(Person person, Room room);

    /**
     * Father or mother returns from work, one of them parks car in garage.
     *
     * @param person     father or mother
     * @param car        car in the house
     * @param garage     garage
     * @param livingRoom living room
     */
    void returnHomeFromWork(Person person, Car car, Room garage, Room livingRoom);

    /**
     * Person returns home from sport and leaves sport element in garage.
     *
     * @param person        concrete person in the house
     * @param sportsElement sport element, that was used by person
     * @param garage        garage
     */
    void returnHomeFromSport(Person person, SportsElements sportsElement, Room garage);

    /**
     * Person orders food, food in refrigerator fulfills.
     *
     * @param person       concrete person in the house
     * @param refrigerator refrigerator
     */
    void orderFood(Person person, Refrigerator refrigerator);

    /**
     * Nurse comes home every weekday.
     *
     * @param nurse   nurse
     * @param kitchen kitchen
     */
    void nurseComesHome(Nurse nurse, Room kitchen);

    /**
     * Person feeds pet.
     *
     * @param person concrete person in the house
     * @param pet    concrete pet in the house
     */
    void feedPet(Person person, Pet pet);

    /**
     * Person goes for a walk with a pet
     *
     * @param person     concrete person
     * @param pet        concrete pet
     * @param livingRoom living room
     */
    void walkWithPet(Person person, Pet pet, Room livingRoom);

    /**
     * Mother or nurse feeds baby.
     *
     * @param person mother or nurse
     * @param baby   baby
     */
    void feedBaby(Person person, Baby baby);

    /**
     * Mother or nurse takes care of baby.
     *
     * @param person mother or nurse
     * @param baby   baby
     */
    void takeCareOfBaby(Person person, Baby baby);
}
