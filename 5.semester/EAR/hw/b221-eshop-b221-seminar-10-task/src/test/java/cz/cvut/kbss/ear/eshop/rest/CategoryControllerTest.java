package cz.cvut.kbss.ear.eshop.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Category;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.rest.handler.ErrorInfo;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest extends BaseControllerTestRunner {

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
    public void getAllReturnsCategoriesReadByCategoryService() throws Exception {
        final List<Category> categories = IntStream.range(0, 5).mapToObj(i -> {
            final Category cat = new Category();
            cat.setName("Category" + i);
            cat.setId(Generator.randomInt());
            return cat;
        }).collect(Collectors.toList());
        when(categoryServiceMock.findAll()).thenReturn(categories);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories")).andReturn();
        final List<Category> result = readValue(mvcResult, new TypeReference<List<Category>>() {
        });
        assertEquals(categories.size(), result.size());
        verify(categoryServiceMock).findAll();
    }

    @Test
    public void createCategoryCreatesCategoryUsingService() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
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
                .perform(post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals("/rest/categories/" + toCreate.getId(), mvcResult);
    }

    @Test
    public void getByIdReturnsMatchingCategory() throws Exception {
        final Category category = new Category();
        category.setId(Generator.randomInt());
        category.setName("category");
        when(categoryServiceMock.find(category.getId())).thenReturn(category);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + category.getId())).andReturn();

        final Category result = readValue(mvcResult, Category.class);
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownCategoryId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Category identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void getProductsByCategoryReturnsProductsForCategory() throws Exception {
        final List<Product> products = Arrays.asList(Generator.generateProduct(), Generator.generateProduct());
        when(productServiceMock.findAll(any())).thenReturn(products);
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + category.getId() + "/products")).andReturn();
        final List<Product> result = readValue(mvcResult, new TypeReference<List<Product>>() {
        });
        assertNotNull(result);
        assertEquals(products.size(), result.size());
        verify(categoryServiceMock).find(category.getId());
        verify(productServiceMock).findAll(category);
    }

    @Test
    public void getProductsByCategoryThrowsNotFoundForUnknownCategoryId() throws Exception {
        final int id = 123;
        mockMvc.perform(get("/rest/categories/" + id + "/products")).andExpect(status().isNotFound());
        verify(categoryServiceMock).find(id);
        verify(productServiceMock, never()).findAll(any());
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
    public void addProductToCategoryThrowsNotFoundForUnknownCategory() throws Exception {
        final Product product = Generator.generateProduct();
        product.setId(Generator.randomInt());
        final int categoryId = 123;
        mockMvc.perform(post("/rest/categories/" + categoryId + "/products").content(toJson(product)).contentType(
                MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
        verify(categoryServiceMock).find(categoryId);
        verify(categoryServiceMock, never()).addProduct(any(), any());
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
