package cz.cvut.kbss.ear.eshop.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE")
public abstract class Item extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Product product;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Item{" +
                "amount=" + amount +
                ", product=" + product +
                "}";
    }
}
