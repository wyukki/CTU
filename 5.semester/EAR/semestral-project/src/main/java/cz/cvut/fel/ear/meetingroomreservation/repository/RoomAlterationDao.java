package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.RoomAlteration;
import org.springframework.stereotype.Repository;

@Repository
public class RoomAlterationDao extends BaseDao<RoomAlteration> {

    public RoomAlterationDao() {
        super(RoomAlteration.class);
    }
}
