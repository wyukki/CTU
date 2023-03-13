package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @MockBean
    private SystemInitializer initializerMock;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderService sut;

    @Test
    public void createCreatesOrderWithItemsFromSpecifiedCart() {
        final Cart cart = initCartWithItems();
        final List<CartItem> items = new ArrayList<>(cart.getItems());

        final Order order = sut.create(cart);
        assertNotNull(order);
        final Order result = em.find(Order.class, order.getId());
        assertEquals(items.size(), result.getItems().size());
        for (int i = 0; i < items.size(); i++) {
            final CartItem expected = items.get(i);
            final OrderItem actual = order.getItems().get(i);
            assertEquals(expected.getAmount(), actual.getAmount());
            assertEquals(expected.getProduct().getId(), actual.getProduct().getId());
        }
        assertEquals(cart.getOwner(), result.getCustomer());
    }

    private Cart initCartWithItems() {
        final Cart cart = new Cart();
        cart.setOwner(Generator.generateUser());
        em.persist(cart.getOwner());
        em.persist(cart);
        for (int i = 0; i < 5; i++) {
            final Product p = Generator.generateProduct();
            p.setAmount(5);
            em.persist(p);
            final CartItem item = new CartItem();
            item.setProduct(p);
            item.setAmount(i);
            cart.addItem(item);
            em.persist(item);
        }
        return cart;
    }

    @Test
    public void createSetsCreatedDateToActualDate() {
        final Cart cart = initCartWithItems();

        final Order order = sut.create(cart);
        assertNotNull(order.getCreated());
    }

    @Test
    public void createEmptiesCart() {
        final Cart cart = initCartWithItems();

        sut.create(cart);
        final Cart result = em.find(Cart.class, cart.getId());
        assertThat(result.getItems(), anyOf(nullValue(), emptyCollectionOf(CartItem.class)));
    }

    @Test
    public void createGeneratesUserWhenCartHasNoOwner() {
        final Cart cart = initCartWithItems();
        cart.setOwner(null);
        final Order result = sut.create(cart);

        assertNotNull(result.getCustomer());
        assertNotNull(em.find(User.class, result.getCustomer().getId()));
    }
}
