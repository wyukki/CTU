package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.ProductDao;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * This test does not use Spring.
 * <p>
 * It showcases how services can be unit tested without being dependent on the application framework or database.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDaoMock;

    @InjectMocks
    private ProductService sut;

    @Test
    public void removeSetsRemovedStatusOnProduct() {
        final Product product = Generator.generateProduct();
        assertFalse(product.isRemoved());
        sut.remove(product);

        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDaoMock).update(captor.capture());
        assertTrue(captor.getValue().isRemoved());
    }
}
