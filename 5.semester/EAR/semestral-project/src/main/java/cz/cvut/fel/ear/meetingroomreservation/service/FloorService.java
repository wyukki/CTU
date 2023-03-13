package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.repository.FloorDao;
import cz.cvut.fel.ear.meetingroomreservation.model.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class FloorService {

    private final FloorDao floorDao;

    @Autowired
    public FloorService(FloorDao floorDao) {
        this.floorDao = floorDao;
    }

    @Transactional(readOnly = true)
    public List<Floor> findAllFloors() {
        return floorDao.findAll();
    }

    @Transactional(readOnly = true)
    public Floor findFloorById(Long id) {
        return floorDao.find(id);
    }

    @Transactional
    public void persist(Floor floor) {
        Objects.requireNonNull(floor);
        floorDao.persist(floor);
    }

    @Transactional
    public void update(Floor floor) {
        Objects.requireNonNull(floor);
        floorDao.update(floor);
    }
}
