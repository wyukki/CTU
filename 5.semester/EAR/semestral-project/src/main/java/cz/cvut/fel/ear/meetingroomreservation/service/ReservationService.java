package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.exception.CancellationAfterTerm;
import cz.cvut.fel.ear.meetingroomreservation.exception.NotFoundException;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.model.ReservationStatus;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import cz.cvut.fel.ear.meetingroomreservation.repository.CustomerDao;
import cz.cvut.fel.ear.meetingroomreservation.repository.ReservationDao;
import cz.cvut.fel.ear.meetingroomreservation.model.Reservation;
import cz.cvut.fel.ear.meetingroomreservation.repository.RoomDao;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final CustomerDao customerDao;
    private final RoomDao roomDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao, CustomerDao customerDao, RoomDao roomDao) {
        this.reservationDao = reservationDao;
        this.customerDao = customerDao;
        this.roomDao = roomDao;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllReservations() {
        return reservationDao.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation findReservationById(Long id) {
        return reservationDao.find(id);
    }

    @Transactional
    public void persist(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservationDao.persist(reservation);
    }

    @Transactional
    public void update(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservationDao.update(reservation);
    }

    @Transactional
    public void cancelReservation(Customer current, Reservation reservation) throws CancellationAfterTerm {
        Utils.checkParametersNotNull(reservation);
        final Customer customer = reservation.getCustomer();
        final Room room = reservation.getRoom();
        if (current.getRole().equals("ROLE_WORKER")) {
            if (current.getUid().equals(customer.getUid())) {
                checkReservationDate(reservation);
            } else {
                throw new NotFoundException("You cannot cancel someone else's reservation");
            }
        }
        customer.removeReservation(reservation);
        room.removeReservation(reservation);
        customerDao.update(customer);
        roomDao.update(room);
        reservation.setStatus(ReservationStatus.CANCELED);
        reservationDao.remove(reservation);
    }

    private void checkReservationDate(Reservation reservation) {
        final LocalDateTime date = reservation.getFrom();
        final int currDate = LocalDate.now().getDayOfMonth();
        final int DAYS_CANCELLATION_TERM = 1;
        if (date.getDayOfMonth() - currDate <= DAYS_CANCELLATION_TERM) {
            throw new CancellationAfterTerm("You cannot cancel you're reservation after cancellation term!");
        }
    }

    @Transactional
    public void setFinishedReservationsAsFinished() {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Reservation> reservations = reservationDao.findAll();
        reservations.forEach(reservation -> {
            LocalDateTime to = reservation.getTo();
            if (to.getDayOfMonth() == localDateTime.getDayOfMonth()
                    && to.getHour() < localDateTime.getHour()) {
                reservation.setStatus(ReservationStatus.FINISHED);
                reservationDao.update(reservation);
            }
        });
    }
}
