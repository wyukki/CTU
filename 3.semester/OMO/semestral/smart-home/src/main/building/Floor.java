package main.building;

import java.util.ArrayList;
import java.util.List;

public final class Floor {
    private final int floor;
    private final List<Room> rooms = new ArrayList<>();

    public Floor(int floor) {
        this.floor = floor;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public int getFloor() {
        return floor;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        StringBuilder roomsOnFloor = new StringBuilder();
        for (Room room : rooms) {
            roomsOnFloor.append(room.getName());
            roomsOnFloor.append(' ');
        }
        return "Floor " + floor + " has rooms " + roomsOnFloor;
    }
}
