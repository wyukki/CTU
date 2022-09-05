package entity;

import javax.persistence.*;

@Entity
@Table(name = "branch", schema = "public", catalog = "semenvol")
@IdClass(BranchEntityPK.class)
public class BranchEntity {
    private int branchnumber;
    private int cid;
    private String ceo;
    private int bankid;
    private int bankcapacity;
    private int bankusedplace;
    private String hospital;
    private String city;
    private String street;
    private String zip;

    @Id
    @Column(name = "branchnumber", nullable = false)
    public int getBranchnumber() {
        return branchnumber;
    }

    public void setBranchnumber(int branchnumber) {
        this.branchnumber = branchnumber;
    }

    @Id
    @Column(name = "cid", nullable = false)
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Id
    @Column(name = "ceo", nullable = false, length = 100)
    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    @Basic
    @Column(name = "bankid", nullable = false)
    public int getBankid() {
        return bankid;
    }

    public void setBankid(int bankid) {
        this.bankid = bankid;
    }

    @Basic
    @Column(name = "bankcapacity", nullable = false)
    public int getBankcapacity() {
        return bankcapacity;
    }

    public void setBankcapacity(int bankcapacity) {
        this.bankcapacity = bankcapacity;
    }

    @Basic
    @Column(name = "bankusedplace", nullable = false)
    public int getBankusedplace() {
        return bankusedplace;
    }

    public void setBankusedplace(int bankusedplace) {
        this.bankusedplace = bankusedplace;
    }

    @Basic
    @Column(name = "hospital", nullable = false, length = 100)
    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    @Basic
    @Column(name = "city", nullable = false, length = 100)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "street", nullable = false, length = 100)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "zip", nullable = false, length = 20)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BranchEntity that = (BranchEntity) o;

        if (branchnumber != that.branchnumber) return false;
        if (cid != that.cid) return false;
        if (bankid != that.bankid) return false;
        if (bankcapacity != that.bankcapacity) return false;
        if (bankusedplace != that.bankusedplace) return false;
        if (ceo != null ? !ceo.equals(that.ceo) : that.ceo != null) return false;
        if (hospital != null ? !hospital.equals(that.hospital) : that.hospital != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (zip != null ? !zip.equals(that.zip) : that.zip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = branchnumber;
        result = 31 * result + cid;
        result = 31 * result + (ceo != null ? ceo.hashCode() : 0);
        result = 31 * result + bankid;
        result = 31 * result + bankcapacity;
        result = 31 * result + bankusedplace;
        result = 31 * result + (hospital != null ? hospital.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BranchEntity{" +
                "branchnumber=" + branchnumber +
                ", cid=" + cid +
                ", ceo='" + ceo + '\'' +
                ", bankid=" + bankid +
                ", bankcapacity=" + bankcapacity +
                ", bankusedplace=" + bankusedplace +
                ", hospital='" + hospital + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
