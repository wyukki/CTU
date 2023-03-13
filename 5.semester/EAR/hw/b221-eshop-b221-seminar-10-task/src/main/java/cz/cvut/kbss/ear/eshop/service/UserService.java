package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.UserDao;
import cz.cvut.kbss.ear.eshop.exception.CartAccessException;
import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.User;
import cz.cvut.kbss.ear.eshop.security.SecurityUtils;
import cz.cvut.kbss.ear.eshop.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    private final UserDao dao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getRole() == null) {
            user.setRole(Constants.DEFAULT_ROLE);
        }
        createCart(user);
        dao.persist(user);
    }

    private void createCart(User user) {
        // Admin cannot shop under their administrator account
        if (!user.isAdmin()) {
            user.setCart(new Cart());
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    /**
     * Gets the currently logged-in user's cart.
     *
     * @return Current user's cart
     */
    public Cart getCurrentUserCart() {
        final User currentUser = SecurityUtils.getCurrentUser();
        assert currentUser != null;
        if (currentUser.isAdmin()) {
            throw new CartAccessException("Admin cannot shop, so it does not have a cart either.");
        }
        return currentUser.getCart();
    }
}
