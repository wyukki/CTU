package entity;

import javax.persistence.*;

@Entity
@Table(name = "person"/*, schema = "public", catalog = "semenvol"*/)
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity {
    private int pass;
    private String name;
    private String phone;
//    private DoctorEntity doctorByPass;
//    private DonorEntity donorByPass;

    @Id
    @Column(name = "pass", nullable = false)
    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 100)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEntity that = (PersonEntity) o;

        if (pass != that.pass) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pass;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

//    @OneToOne(mappedBy = "personByPass")
//    public DoctorEntity getDoctorByPass() {
//        return doctorByPass;
//    }
//
//    public void setDoctorByPass(DoctorEntity doctorByPass) {
//        this.doctorByPass = doctorByPass;
//    }
//
//    @OneToOne(mappedBy = "personByPass")
//    public DonorEntity getDonorByPass() {
//        return donorByPass;
//    }
//
//    public void setDonorByPass(DonorEntity donorByPass) {
//        this.donorByPass = donorByPass;
//    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "pass=" + pass +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
