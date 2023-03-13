package cz.cvut.kbss.ear.eshop.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CART")
public class CartItem extends Item {
}
