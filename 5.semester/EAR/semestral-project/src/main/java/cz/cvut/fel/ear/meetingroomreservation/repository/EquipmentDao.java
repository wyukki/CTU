package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Equipment;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentDao extends BaseDao<Equipment> {
    public EquipmentDao() {
        super(Equipment.class);
    }
}
