package main.sensor;

public interface ApplianceObservable {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers();
}
