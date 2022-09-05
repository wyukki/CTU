package entity;

import javax.persistence.*;

@Entity
@Table(name = "donation", schema = "public", catalog = "semenvol")
public class DonationEntity {
    private int artkey;
    private DoctorEntity doctorByDoctorpass;
    private DonorEntity donorByDonorPass;

    @Id
    @Column(name = "artkey", nullable = false)
    public int getArtkey() {
        return artkey;
    }

    public void setArtkey(int artkey) {
        this.artkey = artkey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonationEntity that = (DonationEntity) o;

        if (artkey != that.artkey) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return artkey;
    }

    @ManyToOne
    @JoinColumn(name = "doctorpass", referencedColumnName = "pass", nullable = false, insertable = false, updatable = false)
    public DoctorEntity getDoctorByDoctorpass() {
        return doctorByDoctorpass;
    }

    public void setDoctorByDoctorpass(DoctorEntity doctorByDoctorpass) {
        this.doctorByDoctorpass = doctorByDoctorpass;
    }

    @ManyToOne
    @JoinColumn(name = "donorpass", referencedColumnName = "pass", nullable = false, insertable = false, updatable = false)
    public DonorEntity getDonorByDonorPass() {
        return donorByDonorPass;
    }

    public void setDonorByDonorPass(DonorEntity donorByDonorPass) {
        this.donorByDonorPass = donorByDonorPass;
    }

    @Override
    public String toString() {
        return "DonationEntity{" +
                "artkey=" + artkey +
                ", doctorByDoctorpass=" + doctorByDoctorpass +
                ", donorByDonorPass=" + donorByDonorPass +
                '}';
    }
}
