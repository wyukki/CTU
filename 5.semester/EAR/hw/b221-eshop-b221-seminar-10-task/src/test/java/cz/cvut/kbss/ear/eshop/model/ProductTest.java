package cz.cvut.kbss.ear.eshop.model;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    @Test
    public void addCategoryWorksWhenAddingCategoryForFirstTime() {
        final Product product = Generator.generateProduct();
        final Category cat = new Category();
        cat.setName("test");
        cat.setId(Generator.randomInt());
        product.addCategory(cat);

        assertEquals(1, product.getCategories().size());
    }

    @Test
    public void addCategoryWorksForProductWithExistingCategory() {
        final Product product = Generator.generateProduct();
        final Category catOne = new Category();
        catOne.setName("test");
        catOne.setId(Generator.randomInt());
        product.setCategories(new ArrayList<>(Collections.singletonList(catOne)));

        final Category catTwo = new Category();
        catTwo.setName("test two");
        catTwo.setId(Generator.randomInt());

        product.addCategory(catTwo);
        assertEquals(2, product.getCategories().size());
    }
}
