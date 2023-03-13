package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao extends BaseDao<Reservation> {
    public ReservationDao() {
        super(Reservation.class);
    }
}
