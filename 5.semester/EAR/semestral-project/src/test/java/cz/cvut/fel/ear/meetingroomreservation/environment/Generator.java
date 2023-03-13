package cz.cvut.fel.ear.meetingroomreservation.environment;

import cz.cvut.fel.ear.meetingroomreservation.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static int randomInt(int max) {
        return RAND.nextInt(max);
    }

    public static int randomInt(int min, int max) {
        assert min >= 0;
        assert min < max;

        int result;
        do {
            result = randomInt(max);
        } while (result < min);
        return result;
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static Building generateBuilding() {
        Building building = new Building();
        building.setAddress("address" + randomInt());
        building.setName("testCompany");
        int floorCount = randomInt(1, 5);
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < floorCount; ++i) {
            floors.add(generateFloor(building));
        }
        building.setFloors(floors);
        return building;
    }

    public static Floor generateFloor(Building building) {
        Floor floor = new Floor();
        floor.setBuilding(building);
        floor.setNumber(randomInt(0, 10));
        List<Room> roomList = new ArrayList<>();
        int roomCount = randomInt(1, 5);
        for (int i = 0; i < roomCount; ++i) {
            roomList.add(generateRoom(floor));
        }
        roomList.add(generatePriorityRoom(floor));
        floor.setRooms(roomList);
        return floor;
    }

    public static Room generateRoom(Floor floor) {
        Room room = new Room();
        room.setCapacity(randomInt());
        room.setDescription("description" + randomInt());
        room.setFloor(floor);
        room.setName("testRoom");
        room.setPriority(Priority.NON_PRIOR);
        return room;
    }

    public static Room generatePriorityRoom(Floor floor) {
        Room roomPrior = new Room();
        roomPrior.setCapacity(randomInt());
        roomPrior.setDescription("description" + randomInt());
        roomPrior.setFloor(floor);
        roomPrior.setName("testPriorityRoom");
        roomPrior.setPriority(Priority.PRIOR);
        return roomPrior;
    }

    public static Equipment generateEquipment(Room room) {
        Equipment equipment = new Equipment();
        equipment.setDescription("description" + randomInt());
        equipment.setName("testName" + randomInt());
        equipment.setRoom(room);
        return equipment;
    }
}
