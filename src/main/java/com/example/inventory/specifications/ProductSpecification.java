package com.example.inventory.specifications;

import com.example.inventory.model.Product;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Or({
        @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class, defaultVal = ""),
})
public interface ProductSpecification extends Specification<Product> {
}
