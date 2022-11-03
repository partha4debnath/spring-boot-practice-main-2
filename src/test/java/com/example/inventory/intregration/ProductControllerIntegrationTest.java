package com.example.inventory.intregration;

import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.util.PostgresTestContainer;
import com.example.inventory.util.TestRedisConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestRedisConfiguration.class)
@AutoConfigureMockMvc()
public class ProductControllerIntegrationTest extends PostgresTestContainer {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;

    @Test
    void getAllProductsGetAllProducts() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(100)));
    }

    @Test
    void getProductsByNameGetsAProductWithNameLikeGivenName() throws Exception {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedName = RandomStringUtils.random(length, useLetters, useNumbers);
        productRepository.save(new Product(generatedName,1.2,1));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        requestBuilder.param("name",generatedName);

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value(generatedName))

        ;
    }
}
