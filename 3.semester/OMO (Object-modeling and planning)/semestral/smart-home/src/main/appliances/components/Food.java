package main.appliances.components;

public final class Food {

    private final String foodType;
    private boolean isSpoiled;

    public Food(String foodType) {
        this.foodType = foodType;
    }

    public boolean isSpoiled() {
        return isSpoiled;
    }

    public String getFoodType() {
        return foodType;
    }
}
