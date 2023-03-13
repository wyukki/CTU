package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.EquipmentDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.RoomDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Equipment;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(source = "entity.rid", target = "id")
    @Mapping(source = "entity.capacity", target = "capacity")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.description", target = "description")
    @Mapping(source = "entity.priority", target = "priority")
    @Mapping(source = "entity.equipmentList", target = "equipments")
    RoomDTO entityToDTO(Room entity);

    @Mapping(source = "dto.id", target = "rid")
    @Mapping(source = "dto.capacity", target = "capacity")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.description", target = "description")
    @Mapping(source = "dto.priority", target = "priority")
    Room dtoToEntity(RoomDTO dto);

    @Mapping(source = "equipment.name", target = "name")
    @Mapping(source = "equipment.description", target = "description")
    EquipmentDTO equipmentToDTO(Equipment equipment);
}
