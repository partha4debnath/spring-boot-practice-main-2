package com.example.inventory.controller;

import com.example.inventory.model.Product;
import com.example.inventory.service.ProductService;
import com.example.inventory.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProductBySpecification(ProductSpecification specification,
                                                                   @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                                                   @RequestParam(required = false, name = "size", defaultValue = "2000") int size,
                                                                   @RequestParam(required = false, name = "sort", defaultValue = "") String sort,
                                                                   @RequestParam(required = false, name = "direction", defaultValue = "asc") String direction) {
        Pageable pageable = getPageable(page, size, sort, direction);
        Page<Product> allProductsWithSpecification = productService.getAllProductsWithSpecification(specification, pageable);
        return new ResponseEntity<Page<Product>>(allProductsWithSpecification, HttpStatus.OK);
    }

    private Pageable getPageable(int page, int size, String sort, String direction) {
        final String desc = "desc";
        Pageable pageable;
        pageable = Objects.equals(sort, "") ? PageRequest.of(page, size)
                : PageRequest.of(page, size, Objects.equals(direction, desc) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        return pageable;
    }

}
