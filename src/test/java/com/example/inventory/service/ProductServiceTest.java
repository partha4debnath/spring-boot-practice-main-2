package com.example.inventory.service;

import com.example.inventory.config.RedisConfig;
import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({RedisConfig.class, ProductService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
public class ProductServiceTest {
    @MockBean
    private ProductRepository mockProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;


    @Test
    void getAllProductReturnsAllProducts() {
        //arrange
        int pageNo = 0;
        int pageSize = 12;
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        Page<Product> page = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Specification querySpec = Mockito.mock(Specification.class);
        Mockito.when(mockProductRepository.findAll(querySpec, pageable)).thenReturn(page);
        //act
        Page allProductsWithSpecification = productService.getAllProductsWithSpecification(querySpec, pageable);
        //assert
        Mockito.verify(mockProductRepository, Mockito.times(1)).findAll(querySpec, pageable);
        assertEquals(1, allProductsWithSpecification.getTotalElements());
    }

    @Test
    void getAllProductReturnsAllProductsTwiceGetsTheProductsFromCacheSecondTime() {
        //arrange
        int pageNo = 0;
        int pageSize = 12;
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            products.add(new Product());
        }
        Page<Product> page = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Specification querySpec = Mockito.mock(Specification.class);
        Mockito.when(mockProductRepository.findAll(querySpec, pageable)).thenReturn(page);
        //act
        Page allProductsWithSpecificationFromDatabase = productService.getAllProductsWithSpecification(querySpec, pageable);
        Page allProductsWithSpecificationFromRedisCache = productService.getAllProductsWithSpecification(querySpec, pageable);
        //assert
        Mockito.verify(mockProductRepository, Mockito.times(1)).findAll(querySpec, pageable);
        assertEquals(12, allProductsWithSpecificationFromDatabase.getTotalElements());
        assertEquals(12, allProductsWithSpecificationFromRedisCache.getTotalElements());
    }
}
