package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.exception.InsufficientAmountException;
import cz.cvut.kbss.ear.eshop.model.Cart;
import cz.cvut.kbss.ear.eshop.model.CartItem;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Using Spring here to startup the context, initialize in-memory database and rollback changes after each test.
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {

    @MockBean
    private SystemInitializer initializerMock;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CartService sut;

    private Cart cart;

    @BeforeEach
    public void setUp() {
        final User owner = Generator.generateUser();
        this.cart = new Cart();
        owner.setCart(cart);
        em.persist(owner);
    }

    @Test
    public void addItemAddsItemIntoCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(1);
        sut.addItem(cart, toAdd);

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(toAdd.getAmount(), result.getItems().get(0).getAmount());
        assertEquals(toAdd.getProduct(), result.getItems().get(0).getProduct());
    }

    @Test
    public void addItemUpdatesProductAmountBySubtractingAmountSpecifiedInItem() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = Generator.randomInt(10, 15);
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        final int toAddAmount = Generator.randomInt(productAmount);
        toAdd.setAmount(toAddAmount);
        sut.addItem(cart, toAdd);

        final Product result = em.find(Product.class, product.getId());
        assertEquals(productAmount - toAddAmount, result.getAmount().intValue());
    }

    @Test
    public void addItemUpdatesExistingItemIfItHasSameProduct() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        final int itemAmount = 1;
        item.setAmount(itemAmount);
        sut.addItem(cart, item);

        final CartItem newItem = new CartItem();
        newItem.setProduct(product);
        final int newItemAmount = 2;
        newItem.setAmount(newItemAmount);
        sut.addItem(cart, newItem);

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(itemAmount + newItemAmount, result.getItems().get(0).getAmount().intValue());
    }

    @Test
    public void addItemThrowsInsufficientAmountExceptionWhenNotEnoughProductExistsForItem() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(productAmount + 1);
        assertThrows(InsufficientAmountException.class, () -> sut.addItem(cart, toAdd));
    }

    @Test
    public void addItemThrowsInsufficientAmountExceptionWhenNotEnoughProductExistsForExistingItem() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem toAdd = new CartItem();
        toAdd.setProduct(product);
        toAdd.setAmount(productAmount - 1);
        sut.addItem(cart, toAdd);

        final CartItem tooMuch = new CartItem();
        tooMuch.setProduct(product);
        tooMuch.setAmount(productAmount);
        assertThrows(InsufficientAmountException.class, () -> sut.addItem(cart, tooMuch));
    }

    @Test
    public void removeItemRemovesItemFromCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        item.setAmount(1);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);
        sut.removeItem(cart, item);

        final Cart result = em.find(Cart.class, cart.getId());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void removeItemUpdatesProductAmount() {
        final Product product = new Product();
        product.setName("test product");
        final int productAmount = 5;
        product.setAmount(productAmount);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        final int itemAmount = 1;
        item.setAmount(itemAmount);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);
        sut.removeItem(cart, item);

        final Product result = em.find(Product.class, product.getId());
        assertEquals(productAmount + itemAmount, result.getAmount().intValue());
    }

    @Test
    public void removeItemUpdatesExistingItemAmountWhenToRemoveAmountIsSmallerThanAmountInCart() {
        final Product product = new Product();
        product.setName("test product");
        product.setAmount(5);
        em.persist(product);

        final CartItem item = new CartItem();
        item.setProduct(product);
        int origAmount = 4;
        item.setAmount(origAmount);
        cart.addItem(item);
        em.persist(item);
        em.merge(cart);

        final CartItem toRemove = new CartItem();
        toRemove.setProduct(product);
        final int toRemoteAmount = origAmount - 1;
        toRemove.setAmount(toRemoteAmount);
        sut.removeItem(cart, toRemove);

        final Cart result = em.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(origAmount - toRemoteAmount, result.getItems().get(0).getAmount().intValue());
    }
}
