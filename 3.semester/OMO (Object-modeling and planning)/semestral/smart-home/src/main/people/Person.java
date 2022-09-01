package main.people;

import main.building.Room;

public class Person {
    private final Family identification;
    private Room location;
    private boolean asleep;

    public Person(Family identification, Room room) {
        this.identification = identification;
        this.location = room;
        setAsleep(false);
    }

    public boolean isAsleep() {
        return asleep;
    }

    public void setAsleep(boolean asleep) {
        this.asleep = asleep;
    }

    public Family getIdentification() {
        return identification;
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        if (this.location != null) {
            for (int i = 0; i < this.location.getPeople().size(); ++i) {
                Person p = this.location.getPeople().get(i);
                if (p.getIdentification() == this.identification) {
                    this.location.getPeople().remove(p);
                    break;
                }
            }
        }
        this.location = location;
        if (this.location != null) {
            this.location.setPerson(this);
        }
    }
}
