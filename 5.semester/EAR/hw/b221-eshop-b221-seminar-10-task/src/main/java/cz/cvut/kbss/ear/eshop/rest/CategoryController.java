package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.exception.NotFoundException;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.model.Role;
import cz.cvut.kbss.ear.eshop.rest.util.RestUtils;
import cz.cvut.kbss.ear.eshop.service.CategoryService;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService service;

    private final ProductService productService;

    @Autowired
    public CategoryController(CategoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return service.findAll();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCategory(@RequestBody Category category) {
        service.persist(category);
        LOG.debug("Created category {}.", category);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", category.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    // If the parameter name matches parameter in the mapping value, it is not necessary to explicitly provide it
    public Category getById(@PathVariable Integer id) {
        final Category category = service.find(id);
        if (category == null) {
            throw NotFoundException.create("Category", id);
        }
        return category;
    }

    @GetMapping(value = "/{id}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductsByCategory(@PathVariable Integer id) {
        return productService.findAll(getById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addProductToCategory(@PathVariable Integer id, @RequestBody Product product) {
        final Category category = getById(id);
        service.addProduct(category, product);
        LOG.debug("Product {} added into category {}.", product, category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{categoryId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductFromCategory(@PathVariable Integer categoryId,
                                          @PathVariable Integer productId) {
        final Category category = getById(categoryId);
        final Product toRemove = productService.find(productId);
        if (toRemove == null) {
            throw NotFoundException.create("Product", productId);
        }
        service.removeProduct(category, toRemove);
        LOG.debug("Product {} removed from category {}.", toRemove, category);
    }
}
