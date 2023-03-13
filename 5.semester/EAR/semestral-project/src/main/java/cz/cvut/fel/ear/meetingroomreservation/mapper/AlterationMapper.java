package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.AdminDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.AlterationDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.RoomDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Admin;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import cz.cvut.fel.ear.meetingroomreservation.model.RoomAlteration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlterationMapper {
    @Mapping(source = "entity.date", target = "date")
    @Mapping(source = "entity.comment", target = "comment")
    @Mapping(source = "entity.admin", target = "admin")
    @Mapping(source = "entity.room", target = "room")
    AlterationDTO entityToDTO(RoomAlteration entity);

    @Mapping(source = "dto.date", target = "date")
    @Mapping(source = "dto.comment", target = "comment")
    @Mapping(source = "dto.admin", target = "admin")
    @Mapping(source = "dto.room", target = "room")
    RoomAlteration dtoToEntity(AlterationDTO dto);

    @Mapping(source = "admin.username", target = "username")
    AdminDTO adminToDTO(Admin admin);

    @Mapping(source = "room.rid", target = "id")
    @Mapping(source = "room.name", target = "name")
    RoomDTO roomToDTO(Room room);
}
