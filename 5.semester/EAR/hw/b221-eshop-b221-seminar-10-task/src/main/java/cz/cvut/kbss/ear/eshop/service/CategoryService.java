package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.CategoryDao;
import cz.cvut.kbss.ear.eshop.dao.ProductDao;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final CategoryDao dao;

    private final ProductDao productDao;

    @Autowired
    public CategoryService(CategoryDao dao, ProductDao productDao) {
        this.dao = dao;
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Category find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Category category) {
        Objects.requireNonNull(category);
        dao.persist(category);
    }

    /**
     * Adds the specified product to the specified category.
     *
     * @param category Target category
     * @param product  Product to add
     */
    @Transactional
    public void addProduct(Category category, Product product) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(product);
        product.addCategory(category);
        productDao.update(product);
    }

    /**
     * Removes the specified product from the specified category.
     *
     * @param category Category to remove product from
     * @param product  Product to remove
     */
    @Transactional
    public void removeProduct(Category category, Product product) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(product);
        product.removeCategory(category);
        productDao.update(product);
    }
}
