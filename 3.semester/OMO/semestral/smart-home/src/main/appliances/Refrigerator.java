package main.appliances;

import main.appliances.API.RefrigeratorAPI;
import main.appliances.components.Food;
import main.appliances.creators.TypeOfAppliance;

import java.util.ArrayList;
import java.util.List;

public final class Refrigerator extends Appliance {
    private final RefrigeratorAPI refrigeratorAPI = new RefrigeratorAPI(this);
    private final List<Food> food = new ArrayList<>();

    public Refrigerator(TypeOfAppliance type, String appliance, int functionality,
                        double MIN_CONSUMPTION, double ACTIVE_CONSUMPTION) {
        super(type, appliance, functionality, MIN_CONSUMPTION, ACTIVE_CONSUMPTION);
        addFood();
    }

    public RefrigeratorAPI getRefrigeratorAPI() {
        return refrigeratorAPI;
    }

    public void addFood() {
        food.add(new Food("Kebab"));
        food.add(new Food("Ramen"));
        food.add(new Food("Burger"));
        food.add(new Food("Beef"));
        food.add(new Food("Pizza"));
    }

    public List<Food> getFood() {
        return this.food;
    }
}
