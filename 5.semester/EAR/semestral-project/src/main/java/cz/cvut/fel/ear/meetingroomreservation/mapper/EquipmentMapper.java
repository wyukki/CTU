package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.EquipmentDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.description", target = "description")
    EquipmentDTO equipmentToDTO(Equipment entity);

    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.description", target = "description")
    Equipment dtoToEntity(EquipmentDTO dto);
}
