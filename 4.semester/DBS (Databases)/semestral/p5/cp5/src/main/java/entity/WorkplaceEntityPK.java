package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class WorkplaceEntityPK implements Serializable {
    private int branchnumber;
    private int doctorpass;

    @Column(name = "branchnumber", nullable = false)
    @Id
    public int getBranchnumber() {
        return branchnumber;
    }

    public void setBranchnumber(int branchnumber) {
        this.branchnumber = branchnumber;
    }

    @Column(name = "doctorpass", nullable = false)
    @Id
    public int getDoctorpass() {
        return doctorpass;
    }

    public void setDoctorpass(int doctorpass) {
        this.doctorpass = doctorpass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkplaceEntityPK that = (WorkplaceEntityPK) o;

        if (branchnumber != that.branchnumber) return false;
        if (doctorpass != that.doctorpass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = branchnumber;
        result = 31 * result + doctorpass;
        return result;
    }
}
