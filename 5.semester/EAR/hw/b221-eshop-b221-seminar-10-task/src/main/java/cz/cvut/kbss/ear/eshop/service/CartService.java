package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.CartDao;
import cz.cvut.kbss.ear.eshop.dao.ProductDao;
import cz.cvut.kbss.ear.eshop.exception.InsufficientAmountException;
import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.CartItem;
import cz.cvut.kbss.ear.eshop.model.Item;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CartService {

    private final CartDao dao;

    private final ProductDao productDao;

    @Autowired
    public CartService(CartDao dao, ProductDao productDao) {
        this.dao = dao;
        this.productDao = productDao;
    }

    /**
     * Adds the specified item into the specified cart.
     * <p>
     * Note that the item probably does not exist in the database, but thanks to the model cascading strategy, it will
     * be persisted automatically.
     * <p>
     * If an item with the same product already exists in the cart, its amount is updated and no new instance is
     * persisted.
     *
     * @param cart  Target cart
     * @param toAdd Item to add
     */
    @Transactional
    public void addItem(Cart cart, CartItem toAdd) {
        Objects.requireNonNull(cart);
        Objects.requireNonNull(toAdd);
        cart.addItem(toAdd);
        updateProductAmountOnItemCreation(toAdd);
        dao.update(cart);
    }

    private void updateProductAmountOnItemCreation(Item item) {
        final Product product = item.getProduct();
        if (product.getAmount() < item.getAmount()) {
            throw new InsufficientAmountException(
                    "The amount of product " + product + " is insufficient to create cart item.");
        }
        product.setAmount(product.getAmount() - item.getAmount());
        productDao.update(product);
    }

    /**
     * Removes the specified item from the cart.
     * <p>
     * If the amount to remove is less than the amount of the existing item, it is just updated and no instance is
     * removed from the actual cart.
     *
     * @param cart     Target cart
     * @param toRemove Item to remove
     */
    @Transactional
    public void removeItem(Cart cart, Item toRemove) {
        Objects.requireNonNull(cart);
        Objects.requireNonNull(toRemove);
        cart.removeItem(toRemove);
        updateProductAmountOnItemRemoval(toRemove);
        dao.update(cart);
    }

    private void updateProductAmountOnItemRemoval(Item item) {
        final Product product = item.getProduct();
        product.setAmount(product.getAmount() + item.getAmount());
        productDao.update(product);
    }
}
