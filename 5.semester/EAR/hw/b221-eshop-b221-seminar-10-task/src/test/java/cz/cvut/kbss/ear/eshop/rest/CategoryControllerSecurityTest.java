package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.config.SecurityConfig;
import cz.cvut.kbss.ear.eshop.environment.Environment;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.TestConfiguration;
import cz.cvut.kbss.ear.eshop.environment.TestSecurityConfig;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.model.Role;
import cz.cvut.kbss.ear.eshop.model.User;
import cz.cvut.kbss.ear.eshop.service.CategoryService;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {TestSecurityConfig.class, CategoryControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class CategoryControllerSecurityTest extends BaseControllerTestRunner {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    private User user;

    @BeforeEach
    public void setUp() {
        this.objectMapper = Environment.getObjectMapper();
        this.user = Generator.generateUser();
    }

    @AfterEach
    public void tearDown() {
        Environment.clearSecurityContext();
        Mockito.reset(categoryService, productService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private CategoryService categoryService;
        @MockBean
        private ProductService productService;

        @Bean
        public CategoryController categoryController() {
            return new CategoryController(categoryService, productService);
        }
    }

    @WithAnonymousUser
    @Test
    public void addCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                        post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).persist(any());
    }

    @WithMockUser
    @Test
    public void addCategoryThrowsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                        post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).persist(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                        post("/rest/categories").content(toJson(toCreate))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @WithAnonymousUser
    @Test
    public void addProductToCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Product p = Generator.generateProduct();
        final int catId = 1;
        mockMvc.perform(post("/rest/categories/" + catId + "/products").content(toJson(p))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).addProduct(any(), any());
    }

    @WithMockUser
    @Test
    public void addProductToCategoryIsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final Product p = Generator.generateProduct();
        final int catId = 1;
        mockMvc.perform(post("/rest/categories/" + catId + "/products").content(toJson(p))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).addProduct(any(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addProductToCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Product p = Generator.generateProduct();
        p.setId(2);
        final Category c = new Category();
        final int catId = 1;
        when(categoryService.find(catId)).thenReturn(c);
        mockMvc.perform(post("/rest/categories/" + catId + "/products").content(toJson(p))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(categoryService).addProduct(eq(c), captor.capture());
        assertEquals(p.getId(), captor.getValue().getId());
    }

    @WithAnonymousUser
    @Test
    public void removeProductFromCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final int productId = 2;
        final int catId = 1;
        mockMvc.perform(delete("/rest/categories/" + catId + "/products/" + productId))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).removeProduct(any(), any());
    }

    @WithMockUser
    @Test
    public void removeProductFromCategoryIsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final int productId = 2;
        final int catId = 1;
        mockMvc.perform(delete("/rest/categories/" + catId + "/products/" + productId))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).removeProduct(any(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void removeProductFromCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Product p = Generator.generateProduct();
        final int productId = 2;
        final Category c = new Category();
        final int catId = 1;
        when(categoryService.find(catId)).thenReturn(c);
        when(productService.find(productId)).thenReturn(p);
        mockMvc.perform(delete("/rest/categories/" + catId + "/products/" + productId))
                .andExpect(status().isNoContent());
        verify(categoryService).removeProduct(c, p);
    }
}
