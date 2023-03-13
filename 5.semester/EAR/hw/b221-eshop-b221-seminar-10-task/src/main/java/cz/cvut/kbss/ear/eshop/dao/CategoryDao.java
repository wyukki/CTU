package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends BaseDao<Category> {

    public CategoryDao() {
        super(Category.class);
    }
}
