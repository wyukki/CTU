package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.FloorDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.RoomDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Floor;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FloorMapper {
    @Mapping(source = "entity.number", target = "number")
    @Mapping(source = "entity.rooms", target = "rooms")
    FloorDTO entityToDTO(Floor entity);

    @Mapping(source = "dto.number", target = "number")
    @Mapping(source = "dto.rooms", target = "rooms")
    Floor dtoToEntity(FloorDTO dto);

    @Mapping(source = "room.name", target = "name")
    @Mapping(source = "room.description", target = "description")
    RoomDTO roomToDTO(Room room);
}
