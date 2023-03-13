package cz.cvut.fel.ear.meetingroomreservation.controller;

import cz.cvut.fel.ear.meetingroomreservation.dto.EquipmentDTO;
import cz.cvut.fel.ear.meetingroomreservation.exception.NotFoundException;
import cz.cvut.fel.ear.meetingroomreservation.exception.ValidationException;
import cz.cvut.fel.ear.meetingroomreservation.mapper.EquipmentMapper;
import cz.cvut.fel.ear.meetingroomreservation.model.Admin;
import cz.cvut.fel.ear.meetingroomreservation.model.Equipment;
import cz.cvut.fel.ear.meetingroomreservation.controller.util.RestUtil;
import cz.cvut.fel.ear.meetingroomreservation.model.Room;
import cz.cvut.fel.ear.meetingroomreservation.security.SecurityUtils;
import cz.cvut.fel.ear.meetingroomreservation.service.EquipmentService;
import cz.cvut.fel.ear.meetingroomreservation.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/equipment")
public class EquipmentController {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

    private final EquipmentService equipmentService;
    private final RoomService roomService;
    private final EquipmentMapper mapper;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, RoomService roomService, EquipmentMapper mapper) {
        this.equipmentService = equipmentService;
        this.roomService = roomService;
        this.mapper = mapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getEquipment() {
        List<Equipment> equipment = equipmentService.findAllEquipments();
        return equipment.stream().map(mapper::equipmentToDTO).collect(Collectors.toList());
    }

    //ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEquipment(@RequestBody Equipment equipment) {
        equipmentService.persist(equipment);
        LOG.debug("Created equipment {}.", equipment.getEid());
        final HttpHeaders headers = RestUtil.createLocationHeaderFromCurrentUri("/{id}", equipment.getEid());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public EquipmentDTO getEquipmentById(@PathVariable Long id) {
        final Equipment equipment = equipmentService.findEquipmentById(id);
        if (equipment == null) {
            throw NotFoundException.create("Equipment", id);
        }
        return mapper.equipmentToDTO(equipment);
    }

    //ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        final Equipment original = equipmentService.findEquipmentById(id);
        if (original == null) {
            throw NotFoundException.create("Equipment", id);
        }
        if (!original.getEid().equals(equipment.getEid())) {
            throw new ValidationException("Equipment id in data does not match the one in the request URL");
        }
        equipmentService.update(equipment);
        LOG.debug("Updated equipment {}.", equipment.getEid());
    }

    //ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEquipment(@PathVariable Long id) {
        final Equipment toRemove = equipmentService.findEquipmentById(id);
        if (toRemove == null) {
            throw NotFoundException.create("Equipment", id);
        }
        final Room room = toRemove.getRoom();
        final Admin current = (Admin) SecurityUtils.getCurrentCustomer();
        roomService.removeEquipment(current, room, toRemove);
        LOG.debug("Removed equipment {}.", id);
    }
}
