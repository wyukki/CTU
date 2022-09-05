package entity;

import javax.persistence.*;

@Entity
@Table(name = "blood", schema = "public", catalog = "semenvol")
public class BloodEntity {
    private int bloodid;
    private short day;
    private short month;
    private short year;
    private boolean rh;
    private short bloodgroup;

    @Id
    @Column(name = "bloodid", nullable = false)
    public int getBloodid() {
        return bloodid;
    }

    public void setBloodid(int bloodid) {
        this.bloodid = bloodid;
    }

    @Basic
    @Column(name = "day", nullable = false)
    public short getDay() {
        return day;
    }

    public void setDay(short day) {
        this.day = day;
    }

    @Basic
    @Column(name = "month", nullable = false)
    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    @Basic
    @Column(name = "year", nullable = false)
    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    @Basic
    @Column(name = "rh", nullable = false)
    public boolean isRh() {
        return rh;
    }

    public void setRh(boolean rh) {
        this.rh = rh;
    }

    @Basic
    @Column(name = "bloodgroup", nullable = false)
    public short getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(short bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BloodEntity that = (BloodEntity) o;

        if (bloodid != that.bloodid) return false;
        if (day != that.day) return false;
        if (month != that.month) return false;
        if (year != that.year) return false;
        if (rh != that.rh) return false;
        if (bloodgroup != that.bloodgroup) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bloodid;
        result = 31 * result + (int) day;
        result = 31 * result + (int) month;
        result = 31 * result + (int) year;
        result = 31 * result + (rh ? 1 : 0);
        result = 31 * result + (int) bloodgroup;
        return result;
    }

    @Override
    public String toString() {
        return "BloodEntity{" +
                "bloodid=" + bloodid +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", rh=" + rh +
                ", bloodgroup=" + bloodgroup +
                '}';
    }
}
