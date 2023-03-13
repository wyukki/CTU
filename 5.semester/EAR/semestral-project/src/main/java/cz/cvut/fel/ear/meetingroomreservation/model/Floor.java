package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.List;

@Entity
public class Floor {
    @Id
    @GeneratedValue
    private Long fid;

    @Column(nullable = false)
    private Integer number;

    @JsonBackReference(value = "building-floor")
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OrderBy("capacity DESC")
    @JsonManagedReference(value = "floor-room")
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL)
    private List<Room> rooms;

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
