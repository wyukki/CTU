package main.pets.states;

public interface PetState {
    /**
     * Sets pet's state depending on current state.
     * @param context
     */
    void walk(PetContext context);

    /**
     * Sets pet's state depending on current state.
     * @param context
     */
    void feed(PetContext context);

    /**
     * Sets pet's state depending on current state.
     * @param context
     */
    void returnHome(PetContext context);
}
