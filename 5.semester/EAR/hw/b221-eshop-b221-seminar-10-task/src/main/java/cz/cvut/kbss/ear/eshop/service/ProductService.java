package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.ProductDao;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductDao dao;

    @Autowired
    public ProductService(ProductDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> findAll(Category category) {
        return dao.findAll(category);
    }

    @Transactional(readOnly = true)
    public Product find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Product product) {
        dao.persist(product);
    }

    @Transactional
    public void update(Product product) {
        dao.update(product);
    }

    /**
     * Removes product, which consists of setting its {@code removed} attribute to {@code true}.
     *
     * @param product Product to remove
     */
    @Transactional
    public void remove(Product product) {
        Objects.requireNonNull(product);
        product.setRemoved(true);
        dao.update(product);
    }
}
