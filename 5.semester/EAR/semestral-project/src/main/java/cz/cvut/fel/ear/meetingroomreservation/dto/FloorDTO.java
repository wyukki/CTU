package cz.cvut.fel.ear.meetingroomreservation.dto;

import cz.cvut.fel.ear.meetingroomreservation.model.Room;

import java.util.List;

public class FloorDTO {
    private Integer number;
    private List<RoomDTO> rooms;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }
}
