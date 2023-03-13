package cz.cvut.fel.ear.meetingroomreservation.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
    private CustomerDTO customer;
    private LocalDateTime from;
    private LocalDateTime to;
    private RoomDTO room;

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }
}
