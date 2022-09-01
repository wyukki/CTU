package main.event;

import main.appliances.*;
import main.building.Room;
import main.people.Father;
import main.people.Person;

import java.util.List;

public interface PeopleWithApplianceActivity {

    /**
     * Opens/closes blinds, turns off/turns on smart lightning
     * in all rooms depending on daytime.
     *
     * @param timer represents daytime, weather and season
     * @param rooms all rooms in the house
     */
    void reactToDayTime(DayTimer timer, List<Room> rooms);

    /**
     * Closes/opens windows, turns on/turns off boiler and AC
     * depending on cold weather.
     *
     * @param timer represents daytime, weather and season
     * @param rooms all rooms in the house
     */
    void reactToColdWeather(DayTimer timer, List<Room> rooms);

    /**
     * Closes/opens windows, turns on/turns off boiler and AC
     * depending on hot weather.
     *
     * @param timer represents daytime, weather and season
     * @param rooms all rooms in the house
     */
    void reactToHotWeather(DayTimer timer, List<Room> rooms);

    /**
     * Father checks if any appliance is broken.
     *
     * @param father father
     * @param appliance specific appliance
     * @param room specific room
     */
    void checkAppliances(Father father, Appliance appliance, Room room);

    /**
     * Father reads manual for appliance and fixes it,
     * functionality of broken appliance is set to defined maximum.
     *
     * @param father father
     * @param appliance broken appliance that needs to be fixed
     */
    void repairAppliance(Father father, Appliance appliance);

    /**
     * Mother or Nurse takes products from fridge, cooks meal on stove.
     *
     * @param person mother or nurse
     * @param refrigerator fridge
     * @param stove stove
     */
    void cookMeal(Person person, Refrigerator refrigerator, Stove stove);

    /**
     * Every person except baby can take food from fridge and warm it up in the
     * microwave.
     *
     * @param person any person except baby
     * @param microwave microwave
     * @param refrigerator fridge
     */
    void warmUpFood(Person person, Microwave microwave, Refrigerator refrigerator);

    /**
     * Any person except baby manually turns on lights.
     *
     * @param person any person except baby
     * @param smartLightning smart lightning
     */
    void turnOnLights(Person person, SmartLightning smartLightning);

    /**
     * Any person except baby manually turns off lights.
     *
     * @param person any person except baby
     * @param smartLightning smart lightning
     */
    void turnOffLights(Person person, SmartLightning smartLightning);

    /**
     * Father watches football on TV in the living room.
     *
     * @param father father
     * @param tv TV
     */
    void watchFootballOnTV(Father father, TV tv);

    /**
     * Mother or Father watches TV series in the living room.
     *
     * @param person mother or father
     * @param TV TV
     */
    void watchFilm(Person person, TV TV);

    /**
     * Mother or elder sister watches TV series in the living room.
     *
     * @param person mother or sister
     * @param TV TV
     */
    void watchTVSeries(Person person, TV TV);

    /**
     * Elder Brother or Elder Sister turn on TV and play gaming console in children bedroom
     *
     * @param person elder brother or elder sister
     * @param gamingConsole gaming console
     * @param TV TV
     */
    void playConsole(Person person, GamingConsole gamingConsole, TV TV);

    /**
     * Mother or Nurse does laundry using washing machine.
     *
     * @param person mother or nurse
     * @param washingMachine washing machine
     */
    void doLaundry(Person person, WashingMachine washingMachine);
}
