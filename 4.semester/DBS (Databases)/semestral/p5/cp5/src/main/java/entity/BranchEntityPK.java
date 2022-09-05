package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class BranchEntityPK implements Serializable {
    private int branchnumber;
    private int cid;
    private String ceo;

    @Column(name = "branchnumber", nullable = false)
    @Id
    public int getBranchnumber() {
        return branchnumber;
    }

    public void setBranchnumber(int branchnumber) {
        this.branchnumber = branchnumber;
    }

    @Column(name = "cid", nullable = false)
    @Id
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Column(name = "ceo", nullable = false, length = 100)
    @Id
    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BranchEntityPK that = (BranchEntityPK) o;

        if (branchnumber != that.branchnumber) return false;
        if (cid != that.cid) return false;
        if (ceo != null ? !ceo.equals(that.ceo) : that.ceo != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "BranchEntityPK{" +
                "branchnumber=" + branchnumber +
                ", cid=" + cid +
                ", ceo='" + ceo + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = branchnumber;
        result = 31 * result + cid;
        result = 31 * result + (ceo != null ? ceo.hashCode() : 0);
        return result;
    }
}
