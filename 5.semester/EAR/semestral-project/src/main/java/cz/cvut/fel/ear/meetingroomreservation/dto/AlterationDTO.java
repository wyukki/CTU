package cz.cvut.fel.ear.meetingroomreservation.dto;

import java.time.LocalDateTime;

public class AlterationDTO {
    private LocalDateTime date;
    private String comment;
    private RoomDTO room;
    private AdminDTO admin;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }
}
