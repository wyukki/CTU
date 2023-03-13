package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.ear.meetingroomreservation.exception.EmptyBuildingException;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@NamedQueries({
        @NamedQuery(name = "Building.findByName", query = "SELECT b FROM Building b WHERE b.name = :name"),
        @NamedQuery(name = "Building.findByAddress", query = "SELECT b FROM Building b WHERE b.address = :address")
})
public class Building {
    @Id
    @GeneratedValue
    private Long bid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OrderBy("number")
    @JsonManagedReference(value = "building-floor")
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Floor> floors;

    @JsonManagedReference(value = "customer-building")
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Customer> workers;

    public void addCustomerToBuilding(Customer customer) {
        Utils.checkParametersNotNull(customer);
        if (workers == null) this.workers = new ArrayList<>();

        final Optional<Customer> isInBuilding = workers.stream().filter(c -> c.getUid().
                equals(customer.getUid())).findFirst();
        if (isInBuilding.isEmpty()) {
            workers.add(customer);
        }
    }

    public void removeCustomerFromBuilding(Customer customer) {
        Utils.checkParametersNotNull(customer);
        if (workers == null || workers.isEmpty())
            throw new EmptyBuildingException("You cannot delete an customer, because this building is empty");
        workers.removeIf(c -> c.getUid().equals(customer.getUid()));
    }

    public void addFloor(Floor floor) {
        Utils.checkParametersNotNull(floor);
        if (floors == null) floors = new ArrayList<>();
        floors.add(floor);
    }

    public void removeFloor(Floor floor) {
        if (floors == null) return;
        floors.removeIf(f -> f.getFid().equals(floor.getFid()));
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public List<Customer> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Customer> workers) {
        this.workers = workers;
    }
}