package cz.cvut.kbss.ear.eshop.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ORDER")
public class OrderItem extends Item {

    public OrderItem() {
    }

    public OrderItem(Item other) {
        setAmount(other.getAmount());
        setProduct(other.getProduct());
    }
}
