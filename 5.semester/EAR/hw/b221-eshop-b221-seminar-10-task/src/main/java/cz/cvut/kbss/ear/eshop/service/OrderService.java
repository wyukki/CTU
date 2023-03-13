package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.CartDao;
import cz.cvut.kbss.ear.eshop.dao.OrderDao;
import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.Order;
import cz.cvut.kbss.ear.eshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class OrderService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    private final OrderDao dao;

    private final CartDao cartDao;

    private final UserService userService;

    @Autowired
    public OrderService(OrderDao dao, CartDao cartDao, UserService userService) {
        this.dao = dao;
        this.cartDao = cartDao;
        this.userService = userService;
    }

    /**
     * Creates new order from the specified cart.
     *
     * @param cart Cart from which order is created
     * @return The new order
     */
    @Transactional
    public Order create(Cart cart) {
        Objects.requireNonNull(cart);
        final Order order = new Order(cart);
        if (cart.getOwner() == null) {
            order.setCustomer(generateCustomerAccount());
        }
        order.setCreated(LocalDateTime.now());
        dao.persist(order);
        clearCart(cart);
        return order;
    }

    private User generateCustomerAccount() {
        final User user = new User();
        user.setFirstName("Customer");
        user.setLastName("No" + System.currentTimeMillis());
        user.setUsername("customer-" + System.currentTimeMillis() + "@kbss.felk.cvut.cz");
        final StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        user.setPassword(sb.toString());
        userService.persist(user);
        return user;
    }

    private void clearCart(Cart cart) {
        cart.getItems().clear();
        cartDao.update(cart);
    }

    @Transactional(readOnly = true)
    public Order find(Integer id) {
        return dao.find(id);
    }

    @PostFilter("hasRole('ROLE_ADMIN') or filterObject.customer.username == principal.username")
    public List<Order> findAll(){
        return dao.findAll();
    }

}
