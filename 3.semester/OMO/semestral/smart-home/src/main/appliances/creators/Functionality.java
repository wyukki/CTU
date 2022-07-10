package main.appliances.creators;

public enum Functionality {
    HIGH(30), MEDIUM(20), LOW(10);

    private final int functionality;

    Functionality(int functionality) {
        this.functionality = functionality;
    }
    public int getFunctionality(){
        return functionality;
    }
}
