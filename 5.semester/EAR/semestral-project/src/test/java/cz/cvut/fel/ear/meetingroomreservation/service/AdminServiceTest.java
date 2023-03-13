package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.environment.Generator;
import cz.cvut.fel.ear.meetingroomreservation.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdminServiceTest {
    @PersistenceContext
    private EntityManager em;
    private Building building;

//    @Test
//    public void addEquipmentAddsEquipmentToSpecificRoom() {
//        Admin admin = new Admin();
//        admin.setUsername("testUsername");
//        admin.setPassword("testPassword");
//        em.persist(admin);
//        building = Generator.generateBuilding();
//        Equipment equipment = new Equipment();
//        equipment.setName("testName");
//        equipment.setDescription("testDescription");
//        em.persist(equipment);
//        Room roomWithEquipment = initRoomsWithEquipment();
//        List<Equipment> equipmentList = roomWithEquipment.getEquipmentList();
//        assertNotNull(equipmentList);
//        sut.addEquipment(admin, roomWithEquipment, equipment);
//
//        final Equipment result = em.find(Equipment.class, equipment.getEid());
//        assertEquals(equipment.getRoom(), result.getRoom());
//
//    }
//
//    private Room initRoomsWithEquipment() {
//        Room room = new Room();
//        room.setName("testName");
//        room.setDescription("testDescription");
//        em.persist(room);
//        List<Equipment> equipments = new ArrayList<>();
//        for (int i = 0; i < 5; ++i) {
//            final Equipment equipment = Generator.generateEquipment(room);
//            em.persist(equipment);
//            equipments.add(equipment);
//        }
//        room.setEquipmentList(equipments);
//        return room;
//    }
//
//    @Test
//    public void removeEquipmentTest(){
//        Admin admin = new Admin();
//        admin.setUsername("testUsername");
//        admin.setPassword("testPassword");
//        em.persist(admin);
//        Room room = new Room();
//        Equipment equipment1 = new Equipment();
//        equipment1.setName("testName1");
//        equipment1.setDescription("testDescription1");
//        Equipment equipment2 = new Equipment();
//        equipment2.setName("testName2");
//        equipment2.setDescription("testDescription2");
//        em.persist(equipment1);
//        em.persist(equipment2);
//        room.addEquipmentToRoom(equipment1);
//        room.addEquipmentToRoom(equipment2);
//        List<Equipment> equipmentList = room.getEquipmentList();
//
//        assertEquals( 2, equipmentList.size());
//        room.removeEquipmentFromRoom(equipment1);
//        assertEquals( 1, equipmentList.size());
//    }
//
//    @Test
//    public void setWorkerPriorTest(){
//        Admin admin = new Admin();
//        admin.setUsername("testUsername");
//        admin.setPassword("testPassword");
//        em.persist(admin);
//
//        Worker worker1 = new Worker();
//        Worker worker2 = new Worker();
//
//        worker1.setPriority(Priority.PRIOR);
//        worker2.setPriority(Priority.NON_PRIOR);
//        em.persist(worker1);
//        em.persist(worker2);
//
//        assertEquals(worker1.getPriority(), Priority.PRIOR);
//        assertEquals(worker2.getPriority(), Priority.NON_PRIOR);
//    }
//
//
//    @Test
//    public void setRoomPriorTest(){
//        Admin admin = new Admin();
//        admin.setUsername("testUsername");
//        admin.setPassword("testPassword");
//        em.persist(admin);
//
//        Room room1 = new Room();
//        Room room2 = new Room();
//
//        room1.setPriority(Priority.PRIOR);
//        room2.setPriority(Priority.NON_PRIOR);
//        em.persist(room1);
//        em.persist(room2);
//
//        assertEquals(room1.getPriority(), Priority.PRIOR);
//        assertEquals(room2.getPriority(), Priority.NON_PRIOR);
//    }
//
//
//    @Test
//    public void cancelUserReservation(){
//        Admin admin = new Admin();
//        admin.setUsername("testUsername");
//        admin.setPassword("testPassword");
//        em.persist(admin);
//
//        Customer customer = new Customer();
//        customer.setUsername("testCustomerUsername");
//        customer.setPassword("testCustomerPassword");
//
//        Reservation reservation = new Reservation();
//        Room room = new Room();
//        reservation.setCustomer(customer);
//        reservation.setRoom(room);
//        reservation.setResid((long) 1);
//        Reservation reservation1 = new Reservation();
//        reservation1.setRoom(room);
//        reservation1.setCustomer(customer);
//        reservation1.setResid((long) 2);
//        List<Reservation> reservationList = new ArrayList<>();
//        reservationList.add(reservation);
//        reservationList.add(reservation1);
//
//        customer.setReservations(reservationList);
//
//        assertEquals(reservationList.size(), customer.getReservations().size());
//
//        customer.removeReservation(reservation1);
//        reservationList.remove(reservation1);
//
//        assertEquals(reservationList.size(), customer.getReservations().size());
//
//    }
}
