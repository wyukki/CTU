package entity;

import javax.persistence.*;

@Entity
@Table(name = "donor"/*, schema = "public", catalog = "semenvol"*/)
public class DonorEntity extends PersonEntity {
//    private int pass;
    private String medcard;

//    @Id
//    @Column(name = "pass", nullable = false)
//    public int getPass() {
//        return pass;
//    }
//
//    public void setPass(int pass) {
//        this.pass = pass;
//    }

    @Basic
    @Column(name = "medcard", nullable = false, length = 12)
    public String getMedcard() {
        return medcard;
    }

    public void setMedcard(String medcard) {
        this.medcard = medcard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonorEntity that = (DonorEntity) o;

//        if (pass != that.pass) return false;
        if (medcard != null ? !medcard.equals(that.medcard) : that.medcard != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = medcard.hashCode();
        result = 31 * result + (medcard != null ? medcard.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DonorEntity{" +
                "medcard='" + medcard + '\'' +
                '}';
    }
}
