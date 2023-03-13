package cz.cvut.kbss.ear.eshop.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.rest.handler.ErrorInfo;
import cz.cvut.kbss.ear.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest extends BaseControllerTestRunner {

    @Mock
    private ProductService productServiceMock;

    @InjectMocks
    private ProductController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void getAllReturnsAllProducts() throws Exception {
        final List<Product> products = IntStream.range(0, 5).mapToObj(i -> Generator.generateProduct()).collect(
                Collectors.toList());
        when(productServiceMock.findAll()).thenReturn(products);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/products")).andReturn();
        final List<Product> result = readValue(mvcResult, new TypeReference<List<Product>>() {
        });
        assertNotNull(result);
        assertEquals(products.size(), result.size());
        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i).getName(), result.get(i).getName());
            assertEquals(products.get(i).getAmount(), result.get(i).getAmount());
        }
    }

    @Test
    public void getByIdReturnsProductWithMatchingId() throws Exception {
        final Product product = Generator.generateProduct();
        product.setId(123);
        when(productServiceMock.find(product.getId())).thenReturn(product);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/products/" + product.getId())).andReturn();
        final Product result = readValue(mvcResult, Product.class);
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getAmount(), result.getAmount());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/products/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Product identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void removeRemovesProductUsingService() throws Exception {
        final Product product = Generator.generateProduct();
        product.setId(123);
        when(productServiceMock.find(product.getId())).thenReturn(product);
        mockMvc.perform(delete("/rest/products/" + product.getId())).andExpect(status().isNoContent());
        verify(productServiceMock).remove(product);
    }

    @Test
    public void removeDoesNothingWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(delete("/rest/products/123")).andExpect(status().isNoContent());
        verify(productServiceMock, never()).remove(any());
    }
}
