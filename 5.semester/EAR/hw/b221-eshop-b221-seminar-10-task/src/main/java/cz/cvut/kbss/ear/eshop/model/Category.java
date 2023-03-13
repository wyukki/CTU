package cz.cvut.kbss.ear.eshop.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents product category.
 * <p>
 * There can be categories currently without products, but every product must belong at least to one category.
 */
@Entity
public class Category extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                "}";
    }
}
