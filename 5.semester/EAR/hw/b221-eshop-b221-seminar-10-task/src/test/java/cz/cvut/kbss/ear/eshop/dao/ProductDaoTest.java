package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.EShopApplication;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.TestConfiguration;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.service.SystemInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

// DataJpaTest does not load all the application beans, it starts only persistence-related stuff
@DataJpaTest
// Exclude SystemInitializer from the startup, we don't want the admin account here
@ComponentScan(basePackageClasses = EShopApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class ProductDaoTest {

    // TestEntityManager provides additional test-related methods (it is Spring-specific)
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductDao sut;

    @Test
    public void findAllByCategoryReturnsProductsInSpecifiedCategory() {
        final Category cat = generateCategory("testCategory");

        final List<Product> products = generateProducts(cat);
        final List<Product> result = sut.findAll(cat);
        assertEquals(products.size(), result.size());
        products.sort(Comparator.comparing(Product::getName));
        result.sort(Comparator.comparing(Product::getName));
        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i).getId(), result.get(i).getId());
        }
    }

    private Category generateCategory(String name) {
        final Category cat = new Category();
        cat.setName(name);
        em.persist(cat);
        return cat;
    }

    private List<Product> generateProducts(Category category) {
        final List<Product> inCategory = new ArrayList<>();
        final Category other = generateCategory("otherCategory");
        for (int i = 0; i < 10; i++) {
            final Product p = Generator.generateProduct();
            p.addCategory(other);
            if (Generator.randomBoolean()) {
                p.addCategory(category);
                inCategory.add(p);
            }
            em.persist(p);
        }
        return inCategory;
    }

    @Test
    public void findAllReturnsOnlyNonRemovedProducts() {
        final Category cat = generateCategory("testCategory");
        final List<Product> products = IntStream.range(0, 10).mapToObj(i -> {
            final Product p = Generator.generateProduct();
            p.addCategory(cat);
            p.setRemoved(Generator.randomBoolean());
            return p;
        }).collect(Collectors.toList());
        products.forEach(em::persist);

        final List<Product> result = sut.findAll();
        assertEquals(products.stream().filter(p -> !p.isRemoved()).count(), result.size());
        result.forEach(p -> assertFalse(p.isRemoved()));
    }

    @Test
    public void findAllByCategoryReturnsOnlyNonRemovedProducts() {
        final Category cat = generateCategory("testCategory");

        final List<Product> products = generateProducts(cat);
        products.forEach(p -> {
            p.setRemoved(Generator.randomBoolean());
            em.merge(p);
        });
        final List<Product> result = sut.findAll(cat);
        result.forEach(p -> {
            assertTrue(p.getCategories().stream().anyMatch(c -> c.getId().equals(cat.getId())));
            assertFalse(p.isRemoved());
        });
    }
}
