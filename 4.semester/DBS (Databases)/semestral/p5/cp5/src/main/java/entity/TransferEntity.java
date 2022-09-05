package entity;

import javax.persistence.*;

@Entity
@Table(name = "transfer", schema = "public", catalog = "semenvol")
public class TransferEntity {
    private int artkey;
    private int amount;
    private BranchEntity branch;

    @Id
    @Column(name = "artkey", nullable = false)
    public int getArtkey() {
        return artkey;
    }

    public void setArtkey(int artkey) {
        this.artkey = artkey;
    }

    @Basic
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferEntity entity = (TransferEntity) o;

        if (artkey != entity.artkey) return false;
        if (amount != entity.amount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artkey;
        result = 31 * result + amount;
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

    @Override
    public String toString() {
        return "TransferEntity{" +
                "artkey=" + artkey +
                ", amount=" + amount +
                ", branch=" + branch +
                '}';
    }
}
