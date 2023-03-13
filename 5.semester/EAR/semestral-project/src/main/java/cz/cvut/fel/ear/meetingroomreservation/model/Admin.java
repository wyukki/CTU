package cz.cvut.fel.ear.meetingroomreservation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ROLE_ADMIN")
public class Admin extends Customer {

    @Column(name = "admin_email")
    private String adminEmail;

    @JsonManagedReference(value = "admin-room_alt")
    @OneToMany(mappedBy = "admin")
    private List<RoomAlteration> roomAlterations;

    public void addAlteration(RoomAlteration alteration) {
        Utils.checkParametersNotNull(alteration);
        if (roomAlterations == null) roomAlterations = new ArrayList<>();
        roomAlterations.add(alteration);
    }

    public void removeAlteration(RoomAlteration alteration) {
        Utils.checkParametersNotNull(alteration);
        if (roomAlterations == null) return;
        roomAlterations.removeIf(alt -> alt.getRaid().equals(alteration.getRaid()));
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public List<RoomAlteration> getRoomAlterations() {
        return roomAlterations;
    }

    public void setRoomAlterations(List<RoomAlteration> roomAlterations) {
        this.roomAlterations = roomAlterations;
    }
}
