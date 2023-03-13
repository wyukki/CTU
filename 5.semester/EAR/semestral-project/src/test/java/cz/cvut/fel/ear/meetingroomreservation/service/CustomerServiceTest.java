package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.environment.Generator;
import cz.cvut.fel.ear.meetingroomreservation.exception.PriorityRoomReservationException;
import cz.cvut.fel.ear.meetingroomreservation.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@Transactional
public class CustomerServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CustomerService customerService;

    private Building building;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int indexOfPriorityRoom;

    private List<Room> roomsOnFirstFloor;

    @BeforeEach
    public void setUp() {
        building = Generator.generateBuilding();
        indexOfPriorityRoom = building.getFloors().get(0).getRooms().size() - 1;
        roomsOnFirstFloor = building.getFloors().get(0).getRooms();
        roomsOnFirstFloor.get(indexOfPriorityRoom).setPriority(Priority.PRIOR);
    }

//    @Test
//    public void createReservationOnNonPriorityRoom_reservationCreated() {
//        Customer customer = new Customer();
//        customer.setUsername("user");
//        customer.setPassword("user123");
//        em.persist(customer);
//
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 16:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//
//        customerService.createReservationOnNonPriorityRoom(customer, roomsOnFirstFloor.get(0),
//                from, to);
//        Assertions.assertEquals(1, roomsOnFirstFloor.get(0).getReservations().size());
//    }
//
//    @Test
//    public void createReservationOnNonPriorityRoom_wrongTimeSpan() {
//        Customer customer = new Customer();
//        customer.setUsername("user");
//        customer.setPassword("user123");
//        em.persist(customer);
//
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 12:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//        Exception exception = Assertions.assertThrows(RuntimeException.class, () ->
//                customerService.createReservationOnNonPriorityRoom(customer, roomsOnFirstFloor.get(0), from, to)
//        );
//
//        Assertions.assertTrue(exception.getMessage().contains("Wrong time span set"));
//    }
//
//    @Test
//    public void createReservationOnNonPriorityRoom_reservationOnThisTimeAlreadyExist() {
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//
//        Customer cust = new Customer();
//        cust.setUsername("test");
//        cust.setPassword("test123");
//        em.persist(cust);
//
//        Reservation alreadyExist = new Reservation();
//        alreadyExist.setCustomer(cust);
//        alreadyExist.setRoom(building.getFloors().get(0).getRooms().get(0));
//        alreadyExist.setFrom(from);
//        alreadyExist.setTo(to);
//        em.persist(alreadyExist);
//        customerService.createReservationOnNonPriorityRoom(cust, roomsOnFirstFloor.get(0),
//                from, to);
//
//        Customer customer = new Customer();
//        customer.setUsername("user");
//        customer.setPassword("user123");
//        em.persist(customer);
//
//        Exception exception = Assertions.assertThrows(RuntimeException.class, () ->
//                customerService.createReservationOnNonPriorityRoom(customer,
//                        roomsOnFirstFloor.get(0), from, to)
//        );
//        Assertions.assertTrue(exception.getMessage().contains("You cannot create this reservation on this time!"));
//    }
//
//    @Test
//    public void createReservationOnNonPriorityRoom_isNotNonPriorityRoom() {
////        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.now();
//        LocalDateTime to = LocalDateTime.now().plusHours(1);
//
//        Customer customer = new Customer();
//        customer.setUsername("test");
//        customer.setPassword("test123");
//        em.persist(customer);
//
//        Exception exception = Assertions.assertThrows(PriorityRoomReservationException.class, () ->
//                customerService.createReservationOnNonPriorityRoom(customer,
//                        roomsOnFirstFloor.get(indexOfPriorityRoom), from, to));
//        Assertions.assertTrue(exception.getMessage().contains("This is not a non-priority room"));
//    }
//
//    @Test
//    public void createReservationOnNonPriorityRoom_reservationCreated_roomAlreadyHasReservations() {
//        Customer customer = new Customer();
//        customer.setUsername("user");
//        customer.setPassword("user123");
//        em.persist(customer);
//
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//        customerService.createReservationOnNonPriorityRoom(customer,
//                roomsOnFirstFloor.get(0), from, to);
//
//        Customer customer1 = new Customer();
//        customer1.setUsername("second");
//        customer1.setPassword("secondttt");
//        em.persist(customer1);
//
//        String strNewFrom = "2022-12-15 15:30";
//        String strNewTo = "2022-12-15 16:00";
//        LocalDateTime newFrom = LocalDateTime.parse(strNewFrom, formatter);
//        LocalDateTime newTo = LocalDateTime.parse(strNewTo, formatter);
//
//        customerService.createReservationOnNonPriorityRoom(customer1,
//                roomsOnFirstFloor.get(0), newFrom, newTo);
//
//        Assertions.assertEquals(2, roomsOnFirstFloor.get(0).getReservations().size());
//    }
//
//    @Test
//    public void createReservationOnPriorityRoom_reservationNotCreated_workerNonPriority() {
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//
//        Worker worker = new Worker();
//        worker.setUsername("worker");
//        worker.setPassword("pass");
//        worker.setPriority(Priority.NON_PRIOR);
//        em.persist(worker);
//
//        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> customerService.createReservationOnPriorityRoom(worker,
//                roomsOnFirstFloor.get(indexOfPriorityRoom), from, to));
//        Assertions.assertTrue(exception.getMessage().contains("You cannot reserve priority room, " +
//                "because there are non-priority rooms available on this floor"));
//    }
//
//    @Test
//    public void createReservationOnPriorityRoom_reservationCreated_workerPriority() {
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//
//        Worker worker = new Worker();
//        worker.setUsername("worker");
//        worker.setPassword("pass");
//        worker.setPriority(Priority.PRIOR);
//        em.persist(worker);
//
//        customerService.createReservationOnPriorityRoom(worker,
//                roomsOnFirstFloor.get(indexOfPriorityRoom), from, to);
//        Assertions.assertEquals(1, roomsOnFirstFloor.get(indexOfPriorityRoom)
//                .getReservations().size());
//    }
//
//    @Test
//    public void createReservationOnPriorityRoom_reservationCreated_allNonPriorityReserved() {
//        String strFrom = "2022-12-15 14:30";
//        String strTo = "2022-12-15 15:00";
//        LocalDateTime from = LocalDateTime.parse(strFrom, formatter);
//        LocalDateTime to = LocalDateTime.parse(strTo, formatter);
//
//        Worker worker = new Worker();
//        worker.setUsername("worker");
//        worker.setPassword("pass");
//        worker.setPriority(Priority.NON_PRIOR);
//        em.persist(worker);
//
//        Worker worker1 = new Worker();
//        worker1.setUsername("worker1");
//        worker1.setPassword("pass2");
//        worker1.setPriority(Priority.NON_PRIOR);
//        em.persist(worker1);
//
//        Worker worker2 = new Worker();
//        worker2.setUsername("worker2");
//        worker2.setPassword("pass2");
//        worker2.setPriority(Priority.NON_PRIOR);
//        em.persist(worker2);
//
//        Worker worker3 = new Worker();
//        worker3.setUsername("worker");
//        worker3.setPassword("pass");
//        worker3.setPriority(Priority.NON_PRIOR);
//        em.persist(worker3);
//
//        for (int i = 0; i < roomsOnFirstFloor.size(); ++i) {
//            if (i == indexOfPriorityRoom) {
//                continue;
//            }
//            customerService.createReservationOnNonPriorityRoom(worker,
//                    roomsOnFirstFloor.get(i), from, to);
//        }
//
//        customerService.createReservationOnPriorityRoom(worker3,
//                roomsOnFirstFloor.get(indexOfPriorityRoom), from, to);
//
//        Assertions.assertEquals(1, roomsOnFirstFloor.get(indexOfPriorityRoom).getReservations().size());
//    }
}
