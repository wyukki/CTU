package cz.cvut.kbss.ear.eshop.rest;

import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.service.CategoryService;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerModifyTest extends BaseControllerTestRunner {

    @Mock
    private CategoryService categoryServiceMock;

    @Mock
    private ProductService productServiceMock;

    @InjectMocks
    private CategoryController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void createCategoryCreatesCategoryUsingService() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(post("/rest/categories").content(toJson(toCreate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        final ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryServiceMock).persist(captor.capture());
        assertEquals(toCreate.getName(), captor.getValue().getName());
    }

    @Test
    public void createCategoryReturnsResponseWithLocationHeader() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");
        toCreate.setId(Generator.randomInt());

        final MvcResult mvcResult = mockMvc
                .perform(post("/rest/categories").content(toJson(toCreate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals("/rest/categories/" + toCreate.getId(), mvcResult);
    }

    @Test
    public void addProductToCategoryAddsProductToSpecifiedCategory() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final Product product = Generator.generateProduct();
        product.setId(Generator.randomInt());
        mockMvc.perform(post("/rest/categories/" + category.getId() + "/products").content(toJson(product)).contentType(
                MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());
        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(categoryServiceMock).addProduct(eq(category), captor.capture());
        assertEquals(product.getId(), captor.getValue().getId());
    }

    @Test
    public void removeProductRemovesProductFromCategory() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final Product product = Generator.generateProduct();
        product.setId(Generator.randomInt());
        product.addCategory(category);
        when(productServiceMock.find(any())).thenReturn(product);
        mockMvc.perform(delete("/rest/categories/" + category.getId() + "/products/" + product.getId()))
                .andExpect(status().isNoContent());
        verify(categoryServiceMock).removeProduct(category, product);
    }

    @Test
    public void removeProductThrowsNotFoundForUnknownProductId() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final int unknownId = 123;
        mockMvc.perform(delete("/rest/categories/" + category.getId() + "/products/" + unknownId))
                .andExpect(status().isNotFound());
        verify(categoryServiceMock).find(category.getId());
        verify(categoryServiceMock, never()).removeProduct(any(), any());
    }
}
