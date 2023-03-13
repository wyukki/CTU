package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Floor;
import org.springframework.stereotype.Repository;

@Repository
public class FloorDao extends BaseDao<Floor>{

    public FloorDao() {
        super(Floor.class);
    }
}
