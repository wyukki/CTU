package entity;

import javax.persistence.*;

@Entity
@Table(name = "doctor"/*, schema = "public", catalog = "semenvol"*/)
public class DoctorEntity extends PersonEntity {
    //    private int pass;
    private int workerid;

//    @Id
//    @Column(name = "pass", nullable = false)
//    public int getPass() {
//        return pass;
//    }

//    public void setPass(int pass) {
//        this.pass = pass;
//    }

    @Basic
    @Column(name = "workerid", nullable = false)
    public int getWorkerid() {
        return workerid;
    }

    public void setWorkerid(int workerid) {
        this.workerid = workerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorEntity that = (DoctorEntity) o;

//        if (pass != that.pass) return false;
        if (workerid != that.workerid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workerid;
        result = 31 * result + workerid;
        return result;
    }

    @Override
    public String toString() {
        return "DoctorEntity{" +
                "workerid=" + workerid +
                '}';
    }
}
