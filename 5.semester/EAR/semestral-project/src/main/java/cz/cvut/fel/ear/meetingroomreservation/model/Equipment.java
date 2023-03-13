package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Equipment {
    @Id
    @GeneratedValue
    private Long eid;

    @Column(nullable = false)
    private String name;

    private String description;

    @JsonBackReference(value = "room-equip")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonBackReference(value = "equip-room_alt")
    @ManyToOne
    @JoinColumn(name = "room_alt_id")
    private RoomAlteration alteration;

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public RoomAlteration getAlteration() {
        return alteration;
    }

    public void setAlteration(RoomAlteration alteration) {
        this.alteration = alteration;
    }
}
