package cz.cvut.kbss.ear.eshop.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Product.findByCategory", query = "SELECT p from Product p WHERE :category MEMBER OF p.categories AND NOT p.removed")
})
public class Product extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double price;

    @ManyToMany
    @OrderBy("name")
    private List<Category> categories;

    private Boolean removed = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        Objects.requireNonNull(category);
        if (categories == null) {
            this.categories = new ArrayList<>();
        }
        categories.add(category);
    }

    public void removeCategory(Category category) {
        Objects.requireNonNull(category);
        if (categories == null) {
            return;
        }
        categories.removeIf(c -> Objects.equals(c.getId(), category.getId()));
    }

    public Boolean isRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", categories=" + categories +
                "}";
    }
}
