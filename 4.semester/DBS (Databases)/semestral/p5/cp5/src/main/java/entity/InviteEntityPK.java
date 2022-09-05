package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class InviteEntityPK implements Serializable {
    private int donorpass;
    private int invitedpass;

    @Column(name = "donorpass", nullable = false)
    @Id
    public int getDonorpass() {
        return donorpass;
    }

    public void setDonorpass(int donorpass) {
        this.donorpass = donorpass;
    }

    @Column(name = "invitedpass", nullable = false)
    @Id
    public int getInvitedpass() {
        return invitedpass;
    }

    public void setInvitedpass(int invitedpass) {
        this.invitedpass = invitedpass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InviteEntityPK that = (InviteEntityPK) o;

        if (donorpass != that.donorpass) return false;
        if (invitedpass != that.invitedpass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = donorpass;
        result = 31 * result + invitedpass;
        return result;
    }
}
