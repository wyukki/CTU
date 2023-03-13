package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.exception.EmptyBuildingException;
import cz.cvut.fel.ear.meetingroomreservation.model.Building;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class BuildingServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BuildingService buildingService;

    private Building building;

    @BeforeEach
    public void setUp() {
        building = new Building();
        building.setName("GameDev Office");
        building.setAddress("Groove St. 11");
        em.persist(building);
    }

    @Test
    public void addCustomerToBuilding_customerAddedToBuilding_oneCustomer() {
        Customer customer = new Customer();
        customer.setUsername("user");
        customer.setPassword("user123");
        em.persist(customer);

        buildingService.addCustomer(building, customer);

        Assertions.assertEquals(building, customer.getBuilding());
        Assertions.assertEquals(1, building.getWorkers().size());
    }

    @Test
    public void addCustomersToBuilding_customersAddedToBuilding_severalCustomers() {
        Customer customer1 = new Customer();
        customer1.setUsername("user");
        customer1.setPassword("user123");
        em.persist(customer1);

        Customer customer2 = new Customer();
        customer2.setUsername("newbie");
        customer2.setPassword("noeq12");
        em.persist(customer2);

        Customer customer3 = new Customer();
        customer3.setUsername("senior");
        customer3.setUsername("G342@#v_ase");
        em.persist(customer3);

        buildingService.addCustomer(building, customer1);
        buildingService.addCustomer(building, customer2);

        Assertions.assertEquals(building, customer1.getBuilding());
        Assertions.assertEquals(building, customer2.getBuilding());
        Assertions.assertNull(customer3.getBuilding());

        Assertions.assertEquals(2, building.getWorkers().size());
    }

    @Test
    public void addCustomerToBuilding_customerNotAdded_thisCustomerAlreadyInBuilding() {
        Customer customer1 = new Customer();
        customer1.setUsername("user");
        customer1.setPassword("user123");
        em.persist(customer1);

        Customer customer2 = new Customer();
        customer2.setUsername("newbie");
        customer2.setPassword("noeq12");
        em.persist(customer2);

        buildingService.addCustomer(building, customer1);
        buildingService.addCustomer(building, customer2);

        // Trying to add an existing customer
        buildingService.addCustomer(building, customer2);

        Assertions.assertEquals(2, building.getWorkers().size());
    }

    @Test
    public void removeCustomerFromBuilding_customerRemoved_oneCustomer() {
        Customer toRemove = new Customer();
        toRemove.setUsername("user_to_remove");
        toRemove.setPassword("removed_user123");
        em.persist(toRemove);

        buildingService.addCustomer(building,toRemove);
        Assertions.assertEquals(1, building.getWorkers().size());

        buildingService.removeCustomer(building, toRemove);
        Assertions.assertEquals(0, building.getWorkers().size());
    }

    @Test
    public void removeCustomersFromBuilding_customersRemovedFromBuilding_severalCustomers() {
        Customer customer1 = new Customer();
        customer1.setUsername("user");
        customer1.setPassword("user123");
        em.persist(customer1);

        Customer customer2 = new Customer();
        customer2.setUsername("newbie");
        customer2.setPassword("noeq12");
        em.persist(customer2);

        Customer customer3 = new Customer();
        customer3.setUsername("senior");
        customer3.setUsername("G342@#v_ase");
        em.persist(customer3);

        buildingService.addCustomer(building, customer1);
        buildingService.addCustomer(building, customer2);
        buildingService.addCustomer(building, customer3);
        Assertions.assertEquals(3, building.getWorkers().size());

        buildingService.removeCustomer(building, customer1);
        buildingService.removeCustomer(building, customer2);
        Assertions.assertEquals(1, building.getWorkers().size());
    }

    @Test
    public void removeCustomerFromBuilding_customerNotRemoved_customerDoesNotExist() {
        Customer toRemove = new Customer();
        toRemove.setUsername("user_to_remove");
        toRemove.setPassword("removed_user123");
        em.persist(toRemove);

        // Trying to remove a non-existent customer
        Exception exception = Assertions.assertThrows(EmptyBuildingException.class, () ->
                buildingService.removeCustomer(building, toRemove));

        Assertions.assertTrue(exception.getMessage().
                contains("You cannot delete an customer, because this building is empty"));
    }

    @Test
    public void findExistingBuildingByNameReturnsBuilding() {
        Building actual = buildingService.findByBuildingName(building.getName());
        Assertions.assertEquals(building, actual);
    }

    @Test
    public void findNonExistingBuildingByNameReturnsNull() {
        Building actual = buildingService.findByBuildingName("CVUT");
        Assertions.assertNull(actual);
    }

    @Test
    public void findExistingBuildingByAddressReturnsBuilding() {
        Building actual = buildingService.findByBuildingAddress(building.getAddress());
        Assertions.assertEquals(building, actual);
    }
    @Test
    public void findNonExistingBuildingByAddressReturnsNull() {
        Building actual = buildingService.findByBuildingAddress("Baker st.1");
        Assertions.assertNull(actual);
    }
}
