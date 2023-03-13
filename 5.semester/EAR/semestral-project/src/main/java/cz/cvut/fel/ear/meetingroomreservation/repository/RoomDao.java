package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao extends BaseDao<Room> {

    public RoomDao() {
        super(Room.class);
    }
}
