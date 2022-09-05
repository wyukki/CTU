package entity;

import javax.persistence.*;

@Entity
@Table(name = "invite", schema = "public", catalog = "semenvol")
@IdClass(InviteEntityPK.class)
public class InviteEntity {
    private int donorpass;
    private int invitedpass;
    private DonorEntity donorByDonorpass;
    private DonorEntity donorByInvitedpass;

    @Id
    @Column(name = "donorpass", nullable = false)
    public int getDonorpass() {
        return donorpass;
    }

    public void setDonorpass(int donorpass) {
        this.donorpass = donorpass;
    }

    @Id
    @Column(name = "invitedpass", nullable = false)
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

        InviteEntity that = (InviteEntity) o;

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

    @ManyToOne
    @JoinColumn(name = "donorpass", referencedColumnName = "pass", nullable = false, insertable = false, updatable = false)
    public DonorEntity getDonorByDonorpass() {
        return donorByDonorpass;
    }

    public void setDonorByDonorpass(DonorEntity donorByDonorpass) {
        this.donorByDonorpass = donorByDonorpass;
    }

    @ManyToOne
    @JoinColumn(name = "invitedpass", referencedColumnName = "pass", nullable = false, insertable = false, updatable = false)
    public DonorEntity getDonorByInvitedpass() {
        return donorByInvitedpass;
    }

    public void setDonorByInvitedpass(DonorEntity donorByInvitedpass) {
        this.donorByInvitedpass = donorByInvitedpass;
    }

    @Override
    public String toString() {
        return "InviteEntity{" +
                "donorpass=" + donorpass +
                ", invitedpass=" + invitedpass +
                ", donorByDonorpass=" + donorByDonorpass +
                ", donorByInvitedpass=" + donorByInvitedpass +
                '}';
    }
}
