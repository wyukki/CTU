package cz.cvut.fel.ear.meetingroomreservation.mapper;

import cz.cvut.fel.ear.meetingroomreservation.dto.CustomerDTO;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "dto.id", target = "uid")
    @Mapping(source = "dto.role", target = "role")
    @Mapping(source = "dto.username", target = "username")
    Customer customerDTOtoCustomer(CustomerDTO dto);

    @Mapping(source = "entity.uid", target = "id")
    @Mapping(source = "entity.role", target = "role")
    @Mapping(source = "entity.username", target = "username")
    CustomerDTO customerToCustomerDTO(Customer entity);
}
