package main.appliances.API;

import main.appliances.Refrigerator;
import main.appliances.components.Food;

public final class RefrigeratorAPI {
    private final Refrigerator refrigerator;

    public RefrigeratorAPI(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
    }

    public void turnOffRefrigerator() {
        this.refrigerator.getContext().turnOff();
    }

    /**
     * Removes food from ArrayList if it is spoiled.
     */
    public void checkRefrigerator() {
        for (int i = 0; i < refrigerator.getFood().size(); ++i) {
            Food food = refrigerator.getFood().get(i);
            if (food.isSpoiled()) {
                refrigerator.getFood().remove(food);
            }
        }
    }

}
