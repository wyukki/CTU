package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ProductDao extends BaseDao<Product> {

    public ProductDao() {
        super(Product.class);
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p WHERE NOT p.removed", Product.class).getResultList();
    }

    public List<Product> findAll(Category category) {
        Objects.requireNonNull(category);
        return em.createNamedQuery("Product.findByCategory", Product.class).setParameter("category", category)
                 .getResultList();
    }
}
