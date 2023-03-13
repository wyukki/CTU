package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao extends BaseDao<Cart> {

    public CartDao() {
        super(Cart.class);
    }
}
