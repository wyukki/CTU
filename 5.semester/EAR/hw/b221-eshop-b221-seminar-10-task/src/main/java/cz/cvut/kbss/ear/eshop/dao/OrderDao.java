package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends BaseDao<Order> {

    public OrderDao() {
        super(Order.class);
    }
}
