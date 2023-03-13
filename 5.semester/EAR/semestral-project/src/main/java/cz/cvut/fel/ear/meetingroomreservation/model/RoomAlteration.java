package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_alt")
public class RoomAlteration {
    @Id
    @GeneratedValue
    private Long raid;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private String comment;

    @JsonBackReference(value = "admin-room_alt")
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @JsonBackReference(value = "room-room_alt")
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @JsonManagedReference(value = "equip-room_alt")
    @OneToMany(mappedBy = "alteration")
    private List<Equipment> newEquipment;

    public Long getRaid() {
        return raid;
    }

    public void setRaid(Long raid) {
        this.raid = raid;
    }

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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Equipment> getNewEquipment() {
        return newEquipment;
    }

    public void setNewEquipment(List<Equipment> newEquipment) {
        this.newEquipment = newEquipment;
    }
}
