package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.BuildingDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.FloorDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.RoomDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Building;
import cz.cvut.fel.ear.meetingroomreservation.model.Floor;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingMapper {
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.floors", target = "floors")
    @Mapping(source = "entity.address", target = "address")
    BuildingDTO buildingToBuildingDTO(Building entity);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.floors", target = "floors")
    @Mapping(source = "dto.address", target = "address")
    Building buildingDTOtoBuilding(BuildingDTO dto);

    @Mapping(source = "floor.number", target = "number")
    @Mapping(source = "floor.rooms", target = "rooms")
    FloorDTO floorToDTO(Floor floor);

    @Mapping(source = "room.capacity" , target = "capacity")
    @Mapping(source = "room.name" , target = "name")
    @Mapping(source = "room.description" , target = "description")
    @Mapping(source = "room.priority" , target = "priority")
    RoomDTO roomToDTO(Room room);
}
