package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLE",
        discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "CUSTOMER")
@NamedQuery(name = "Customer.findByUsername", query = "SELECT c FROM Customer c WHERE c.username = :username")
public class Customer {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "ROLE")
    private String role;

    @JsonBackReference(value = "customer-building")
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @JsonManagedReference(value = "customer-reservation")
    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) reservations = new ArrayList<>();
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.removeIf(curr -> curr.getResid().equals(reservation.getResid()));
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void erasePassword() {
        this.password = null;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
