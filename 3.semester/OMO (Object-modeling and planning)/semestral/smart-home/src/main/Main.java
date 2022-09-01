package main;

import main.simulation.Simulation;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        try {
            simulation.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
