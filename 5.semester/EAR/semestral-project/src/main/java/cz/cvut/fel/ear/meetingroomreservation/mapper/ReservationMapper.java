package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.CustomerDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.ReservationDTO;
import cz.cvut.fel.ear.meetingroomreservation.dto.RoomDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.model.Reservation;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "entity.customer", target = "customer")
    @Mapping(source = "entity.from", target = "from")
    @Mapping(source = "entity.to", target = "to")
    @Mapping(source = "entity.room", target = "room")
    ReservationDTO entityToDTO(Reservation entity);

    @Mapping(source = "dto.customer", target = "customer")
    @Mapping(source = "dto.from", target = "from")
    @Mapping(source = "dto.to", target = "to")
    @Mapping(source = "dto.room", target = "room")
    Reservation dtoToEntity(ReservationDTO dto);

    @Mapping(source = "entity.uid", target = "id")
    @Mapping(source = "entity.role", target = "role")
    @Mapping(source = "entity.username", target = "username")
    CustomerDTO customerToCustomerDTO(Customer entity);

    @Mapping(source = "entity.rid", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.priority", target = "priority")
    RoomDTO entityToDTO(Room entity);
}
