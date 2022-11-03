package com.example.inventory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Setter
@Getter
public class Product implements Serializable {
    @Id
    @Column(columnDefinition = "UUID",name = "product_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "userId", strategy = "uuid2")
    private UUID productID;
    private String name;
    private double price;
    private int rating;

    public Product(String name, double price, int rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }
}
