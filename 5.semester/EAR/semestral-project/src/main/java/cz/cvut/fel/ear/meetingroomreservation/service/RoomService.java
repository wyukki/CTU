package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.exception.LowPriorityException;
import cz.cvut.fel.ear.meetingroomreservation.exception.PriorityRoomReservationException;
import cz.cvut.fel.ear.meetingroomreservation.exception.ToManyReservations;
import cz.cvut.fel.ear.meetingroomreservation.model.*;
import cz.cvut.fel.ear.meetingroomreservation.repository.*;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class RoomService {

    private final RoomDao roomDao;
    private final RoomAlterationDao alterationDao;
    private final EquipmentDao equipmentDao;
    private final CustomerDao customerDao;
    private final ReservationDao reservationDao;
    private final AdminDao adminDao;


    @Autowired
    public RoomService(RoomDao roomDao, RoomAlterationDao alterationDao, EquipmentDao equipmentDao, CustomerDao customerDao,
                       ReservationDao reservationDao, AdminDao adminDao) {
        this.roomDao = roomDao;
        this.alterationDao = alterationDao;
        this.equipmentDao = equipmentDao;
        this.customerDao = customerDao;
        this.reservationDao = reservationDao;
        this.adminDao = adminDao;
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRooms() {
        return roomDao.findAll();
    }

    @Transactional(readOnly = true)
    public Room findRoomById(Long id) {
        return roomDao.find(id);
    }

    @Transactional
    public void persist(Room room) {
        Objects.requireNonNull(room);
        roomDao.persist(room);
    }

    @Transactional
    public void update(Room room) {
        Objects.requireNonNull(room);
        roomDao.update(room);
    }

    @Transactional
    public void createReservationOnNonPriorityRoom(Room room, Reservation reservation)
            throws ToManyReservations {
        Utils.checkParametersNotNull(reservation, room);
        if (room.getPriority().equals(Priority.PRIOR)) {
            throw new PriorityRoomReservationException("This is not a non-priority room");
        }
        addReservation(room, reservation);
    }

    @Transactional
    public void createReservationOnPriorityRoom(Room room, Reservation reservation) {
        Utils.checkParametersNotNull(reservation, room);
        final Worker current = (Worker) reservation.getCustomer();
        Floor floor = room.getFloor();
        if (current.getPriority().equals(Priority.PRIOR)) {
            addReservation(room, reservation);
            return;
        }
        if (room.getPriority().equals(Priority.NON_PRIOR) || !floor.getRooms().contains(room)) {
            throw new LowPriorityException("This is not a priority room or this room does not exist on this floor");
        } else {
            for (Room r : floor.getRooms()) {
                if (r.getPriority().equals(Priority.NON_PRIOR) && !isReserved(r, reservation)) {
                    throw new PriorityRoomReservationException("You cannot reserve priority room, " +
                            "because there are non-priority rooms available on this floor");
                }
            }
            addReservation(room, reservation);
        }
    }

    private void addReservation(Room room, Reservation reservation) {
        if (!isReserved(room, reservation) && reservation.getFrom().isBefore(reservation.getTo())
                && reservation.getFrom().isAfter(LocalDateTime.now())) {
            final Customer customer = reservation.getCustomer();
            customer.addReservation(reservation);
            room.addReservation(reservation);
            customerDao.update(customer);
            roomDao.update(room);
            reservationDao.persist(reservation);
        } else {
            throw new ToManyReservations("You cannot create this reservation on this time!");
        }
    }

    private boolean isReserved(Room room, Reservation reservation) {
        if (room.getReservations() == null) {
            return false;
        }
        for (Reservation existReservation : room.getReservations()) {
            if (!(reservation.getTo().isBefore(existReservation.getFrom()) ||
                    reservation.getFrom().isAfter(existReservation.getTo()))) {
                return true; // reservation is impossible
            }
        }
        return false; // reservation is possible
    }

    // Create weekly reservation only on current month
    @Transactional
    public void createWeeklyReservation(Room room, Customer customer) {
        Utils.checkParametersNotNull(room);
        final LocalDateTime currDate = LocalDateTime.now();
        final int daysCount = currDate.getMonth().maxLength();

        //GTH:
        // - create not only weekly reservations.
        // - user can specify exact from and to dates.
        // - system can create reservation in advance up to year

        for (int i = currDate.getDayOfMonth(); i <= daysCount; i += 7) {
            LocalDateTime dateFrom = LocalDateTime.of(currDate.getYear(), currDate.getMonth(),
                    i, currDate.getHour(), currDate.getMinute());

            LocalDateTime dateTo = LocalDateTime.of(currDate.getYear(), currDate.getMonth(),
                    i, currDate.getHour(), currDate.getMinute());

            Reservation newReservation = setReservation(customer, room,
                    dateFrom, dateTo);
            try {
                if (!isReserved(room, newReservation)) {
                    customer.addReservation(newReservation);
                    room.addReservation(newReservation);
                    customerDao.update(customer);
                    roomDao.update(room);
                    newReservation.setStatus(ReservationStatus.ACTIVE);
                    reservationDao.persist(newReservation);
                }
            } catch (ToManyReservations exception) {
                System.err.println("Cannot create reservation on " + i + " of " + currDate.getMonth().name());
            }
        }
    }

    private Reservation setReservation(Customer customer, Room room, LocalDateTime dateFrom, LocalDateTime dateTo) {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setFrom(dateFrom);
        reservation.setTo(dateTo);
        return reservation;
    }

    @Transactional
    public void setRoomIsPrior(Room room) {
        Utils.checkParametersNotNull(room);
        room.setPriority(Priority.PRIOR);
        roomDao.update(room);
    }

    @Transactional
    public void setRoomIsNotPrior(Room room) {
        Utils.checkParametersNotNull(room);
        room.setPriority(Priority.NON_PRIOR);
        roomDao.update(room);
    }

    private RoomAlteration createAlteration(Admin admin, Room room) {
        RoomAlteration roomAlteration = new RoomAlteration();
        roomAlteration.setAdmin(admin);
        roomAlteration.setRoom(room);
        roomAlteration.setDate(LocalDateTime.now());
        return roomAlteration;
    }

    @Transactional
    public void addEquipment(Admin admin, Room room, Equipment equipment) {
        Utils.checkParametersNotNull(admin, room, equipment);
        RoomAlteration roomAlteration = createAlteration(admin, room);
        equipment.setAlteration(roomAlteration);
        room.addEquipmentToRoom(equipment);
        admin.addAlteration(roomAlteration);
        roomAlteration.setComment("Added " + equipment.getName() + " with ID: " + equipment.getEid());
        roomAlteration.setNewEquipment(List.of(equipment));
        admin.addAlteration(roomAlteration);
        room.addAlteration(roomAlteration);

        roomDao.update(room);
        equipmentDao.persist(equipment);
        adminDao.update(admin);
        alterationDao.persist(roomAlteration);
    }

    @Transactional
    public void removeEquipment(Admin admin, Room room, Equipment equipment) {
        Utils.checkParametersNotNull(room, equipment);
        room.removeEquipmentFromRoom(equipment);
        RoomAlteration roomAlteration = createAlteration(admin, room);
        roomAlteration.setComment("Removed " + equipment.getName() + " with ID: " + equipment.getEid());
        alterationDao.persist(roomAlteration);

        admin.removeAlteration(roomAlteration);
        room.removeAlteration(roomAlteration);

        roomDao.update(room);
        adminDao.update(admin);
        equipmentDao.remove(equipment);
    }
}