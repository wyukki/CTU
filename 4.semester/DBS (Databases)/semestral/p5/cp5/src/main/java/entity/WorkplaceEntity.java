package entity;

import javax.persistence.*;

@Entity
@Table(name = "workplace", schema = "public", catalog = "semenvol")
@IdClass(WorkplaceEntityPK.class)
public class WorkplaceEntity {
    private int branchnumber;
    private int doctorpass;
    private BranchEntity branch;
    private DoctorEntity doctorByDoctorpass;

    @Id
    @Column(name = "branchnumber", nullable = false)
    public int getBranchnumber() {
        return branchnumber;
    }

    public void setBranchnumber(int branchnumber) {
        this.branchnumber = branchnumber;
    }

    @Id
    @Column(name = "doctorpass", nullable = false)
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

        WorkplaceEntity that = (WorkplaceEntity) o;

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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "branchnumber", referencedColumnName = "branchnumber", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "cid", referencedColumnName = "cid", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "ceo", referencedColumnName = "ceo", nullable = false, insertable = false, updatable = false)})
    public BranchEntity getBranch() {
        return branch;
    }

    public void setBranch(BranchEntity branch) {
        this.branch = branch;
    }

    @ManyToOne
    @JoinColumn(name = "doctorpass", referencedColumnName = "pass", nullable = false, insertable = false, updatable = false)
    public DoctorEntity getDoctorByDoctorpass() {
        return doctorByDoctorpass;
    }

    public void setDoctorByDoctorpass(DoctorEntity doctorByDoctorpass) {
        this.doctorByDoctorpass = doctorByDoctorpass;
    }

    @Override
    public String toString() {
        return "WorkplaceEntity{" +
                "branchnumber=" + branchnumber +
                ", doctorpass=" + doctorpass +
                ", branch=" + branch +
                ", doctorByDoctorpass=" + doctorByDoctorpass +
                '}';
    }
}
