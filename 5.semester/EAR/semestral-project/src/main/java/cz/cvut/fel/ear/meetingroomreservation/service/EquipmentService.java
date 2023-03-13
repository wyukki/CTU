package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.repository.EquipmentDao;
import cz.cvut.fel.ear.meetingroomreservation.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class EquipmentService {

    private final EquipmentDao equipmentDao;

    @Autowired
    public EquipmentService(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    @Transactional(readOnly = true)
    public List<Equipment> findAllEquipments() {
        return equipmentDao.findAll();
    }

    @Transactional(readOnly = true)
    public Equipment findEquipmentById(Long id) {
        return equipmentDao.find(id);
    }

    @Transactional
    public void persist(Equipment equipment) {
        Objects.requireNonNull(equipment);
        equipmentDao.persist(equipment);
    }

    @Transactional
    public void update(Equipment equipment) {
        Objects.requireNonNull(equipment);
        equipmentDao.update(equipment);
    }

    @Transactional
    public void remove(Equipment equipment) {
        Objects.requireNonNull(equipment);
        equipmentDao.remove(equipment);
    }
}
