package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.exception.EmptyBuildingException;
import cz.cvut.fel.ear.meetingroomreservation.repository.BuildingDao;
import cz.cvut.fel.ear.meetingroomreservation.repository.CustomerDao;
import cz.cvut.fel.ear.meetingroomreservation.model.Building;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BuildingService {

    private final BuildingDao buildingDao;

    private final CustomerDao customerDao;

    @Autowired
    public BuildingService(BuildingDao buildingDao, CustomerDao customerDao) {
        this.buildingDao = buildingDao;
        this.customerDao = customerDao;
    }

    @Transactional(readOnly = true)
    public List<Building> findAllBuildings() {
        return buildingDao.findAll();
    }

    @Transactional(readOnly = true)
    public Building findBuildingById(Long id) {
        return buildingDao.find(id);
    }

    @Transactional
    public Building findByBuildingName(String name) {
        Utils.checkParametersNotNull(name);
        return buildingDao.findByName(name);
    }

    @Transactional
    public Building findByBuildingAddress(String address) {
        Utils.checkParametersNotNull(address);
        return buildingDao.findByAddress(address);
    }

    @Transactional
    public void persist(Building building) {
        Objects.requireNonNull(building);
        buildingDao.persist(building);
    }

    @Transactional
    public void update(Building building) {
        Objects.requireNonNull(building);
        buildingDao.update(building);
    }


    /**
     * Adds the customer to the building
     * @param building Target building
     * @param customer Customer to add
     */
    @Transactional
    public void addCustomer(Building building, Customer customer) {
        Objects.requireNonNull(building);
        Objects.requireNonNull(customer);
        building.addCustomerToBuilding(customer);
        customer.setBuilding(building);
        buildingDao.update(building);
        customerDao.update(customer);
    }

    /**
     * Removes the customer from the building
     * <p>
     * After removal sets the {@code building} value to {@code null} for the removed customer
     * @param building Target building
     * @param customer Customer to remove
     */
    @Transactional
    public void removeCustomer(Building building, Customer customer) throws EmptyBuildingException {
        Objects.requireNonNull(building);
        Objects.requireNonNull(customer);
        building.removeCustomerFromBuilding(customer);
        customer.setBuilding(null);
        buildingDao.update(building);
        customerDao.update(customer);
    }
}
