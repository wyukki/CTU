package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Room {
    @Id
    @GeneratedValue
    private Long rid;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @JsonBackReference(value = "floor-room")
    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @OrderBy("name ASC")
    @JsonManagedReference(value = "room-equip")
    @OneToMany(mappedBy = "room")
    private List<Equipment> equipmentList;

    @JsonManagedReference(value = "room-room_alt")
    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RoomAlteration> roomAlterations;

    @JsonManagedReference(value = "reservation-room")
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if (reservations == null) reservations = new ArrayList<>();
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.removeIf(curr -> curr.getResid().equals(reservation.getResid()));
    }

    public void addEquipmentToRoom(Equipment equipment) {
        Utils.checkParametersNotNull(equipment);
        if (equipmentList == null) {
            this.equipmentList = new ArrayList<>();
        }
        final Optional<Equipment> isInRoom = equipmentList.stream().filter(e -> e.getEid().
                equals(equipment.getEid())).findFirst();
        if (isInRoom.isEmpty()) {
            equipmentList.add(equipment);
        }
    }


    public void removeEquipmentFromRoom(Equipment equipment) {
        Utils.checkParametersNotNull(equipment);
        if (equipmentList == null) {
            return;
        }
        equipmentList.removeIf(e -> e.getEid().equals(equipment.getEid()));
    }

    public void addAlteration(RoomAlteration alteration) {
        Utils.checkParametersNotNull(alteration);
        if (roomAlterations == null) roomAlterations = new ArrayList<>();
        roomAlterations.add(alteration);
    }

    public void removeAlteration(RoomAlteration alteration) {
        Utils.checkParametersNotNull(alteration);
        if (roomAlterations == null) return;
        roomAlterations.removeIf(alt -> alt.getRaid().equals(alteration.getRaid()));
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<RoomAlteration> getRoomAlterations() {
        return roomAlterations;
    }

    public void setRoomAlterations(List<RoomAlteration> roomAlterations) {
        this.roomAlterations = roomAlterations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
