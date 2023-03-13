package cz.cvut.fel.ear.meetingroomreservation.controller;

import cz.cvut.fel.ear.meetingroomreservation.dto.BuildingDTO;
import cz.cvut.fel.ear.meetingroomreservation.exception.NotFoundException;
import cz.cvut.fel.ear.meetingroomreservation.exception.ValidationException;
import cz.cvut.fel.ear.meetingroomreservation.mapper.BuildingMapper;
import cz.cvut.fel.ear.meetingroomreservation.model.Building;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.controller.util.RestUtil;
import cz.cvut.fel.ear.meetingroomreservation.service.BuildingService;
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
@RequestMapping("/rest/buildings")
public class BuildingController {

    private static final Logger LOG = LoggerFactory.getLogger(BuildingController.class);

    private final BuildingService buildingService;

    private final BuildingMapper buildingMapper;

    @Autowired
    public BuildingController(BuildingService buildingService, BuildingMapper buildingMapper) {
        this.buildingService = buildingService;
        this.buildingMapper = buildingMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BuildingDTO> getBuildings() {
        List<Building> buildings = buildingService.findAllBuildings();
        return buildings.stream().map(buildingMapper::buildingToBuildingDTO).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BuildingDTO getBuildingById(@PathVariable Long id) {
        final Building building = buildingService.findBuildingById(id);
        if (building == null) {
            throw NotFoundException.create("Building", id);
        }
        return buildingMapper.buildingToBuildingDTO(building);
    }

    //ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewBuilding(@RequestBody Building building) {
        buildingService.persist(building);
        LOG.debug("Added building {} with id {}.", building.getName(), building.getBid());
        final HttpHeaders headers = RestUtil.createLocationHeaderFromCurrentUri("/{id}", building.getBid());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    //    ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        final Building original = getBuilding(id);
        if (!original.getBid().equals(building.getBid())) {
            throw new ValidationException("Floor id in the data does not match the one in the request URL");
        }
        buildingService.update(building);
        LOG.debug("Updated building {}.", building.getBid());
    }

    // ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/workers", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addWorkerToBuilding(@PathVariable Long id, @RequestBody Customer customer) {
        final Building building = getBuilding(id);
        buildingService.addCustomer(building, customer);
    }

    // ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}/workers", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWorkerFromBuilding(@PathVariable Long id, @RequestBody Customer customer) {
        final Building building = getBuilding(id);
        buildingService.removeCustomer(building, customer);
        LOG.debug("Building with id {} has been deleted", id);
    }

    private Building getBuilding(Long id) {
        final Building building = buildingService.findBuildingById(id);
        if (building == null) {
            throw NotFoundException.create("Building", id);
        }
        return building;
    }
}
