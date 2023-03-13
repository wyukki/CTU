package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.exception.NotFoundException;
import cz.cvut.kbss.ear.eshop.exception.ValidationException;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.rest.util.RestUtils;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        productService.persist(product);
        LOG.debug("Created product {}.", product);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", product.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable Integer id) {
        final Product p = productService.find(id);
        if (p == null) {
            throw NotFoundException.create("Product", id);
        }
        return p;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        final Product original = getProduct(id);
        if (!original.getId().equals(product.getId())) {
            throw new ValidationException("Product identifier in the data does not match the one in the request URL.");
        }
        productService.update(product);
        LOG.debug("Updated product {}.", product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(@PathVariable Integer id) {
        final Product toRemove = productService.find(id);
        if (toRemove == null) {
            return;
        }
        productService.remove(toRemove);
        LOG.debug("Removed product {}.", toRemove);
    }
}
