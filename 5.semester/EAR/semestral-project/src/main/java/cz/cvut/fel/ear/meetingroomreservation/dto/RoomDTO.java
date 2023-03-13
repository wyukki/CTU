package cz.cvut.fel.ear.meetingroomreservation.dto;

import cz.cvut.fel.ear.meetingroomreservation.model.Floor;
import cz.cvut.fel.ear.meetingroomreservation.model.Priority;

import java.util.List;

public class RoomDTO {
    private Long id;
    private Integer capacity;
    private String name;
    private String description;
    private Priority priority;
    private List<EquipmentDTO> equipments;

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

    public List<EquipmentDTO> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentDTO> equipments) {
        this.equipments = equipments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
