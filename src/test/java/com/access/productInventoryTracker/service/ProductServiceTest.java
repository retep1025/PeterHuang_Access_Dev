package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import com.access.productInventoryTracker.repository.ProductRepository;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setupMockProducts() {
        List<Product> mockProducts = Arrays.asList(
            new Product(1L, "Laptop", new BigDecimal(1500.0), "Electronics", true),
            new Product(2L, "Smartphone", new BigDecimal(800.0), "Electronics", false),
            new Product(3L, "Coffee Maker", new BigDecimal(100.0), "Home Appliances", true),
            new Product(4L, "Blender", new BigDecimal(150.0), "Home Appliances", true),
            new Product(5L, "T-Shirt", new BigDecimal(30.0), "Apparel", true),
            new Product(6L, "Jeans", new BigDecimal(45.0), "Apparel", true),
            new Product(7L, "Desk Lamp", new BigDecimal(89.99), "Home Appliances", false),
            new Product(8L, "Wall Art", new BigDecimal(120.0), "Home Decor", true),
            new Product(9L, "Sneakers", new BigDecimal(75.0), "Apparel", true),
            new Product(10L, "Wristwatch", new BigDecimal(250.0), "Accessories", false),
            new Product(11L, "Backpack", new BigDecimal(60.0), "Accessories", true),
            new Product(12L, "Microwave Oven", new BigDecimal(99.0), "Home Appliances", false),
            new Product(13L, "Floor Rug", new BigDecimal(150.0), "Home Decor", true),
            new Product(14L, "Speaker", new BigDecimal(300.0), "Electronics", true),
            new Product(15L, "E-reader", new BigDecimal(200.0), "Electronics", false),
            new Product(16L, "Gaming Console", new BigDecimal(499.99), "Electronics", true),
            new Product(17L, "Office Chair", new BigDecimal(220.0), "Office Supplies", true),
            new Product(18L, "Pen Set", new BigDecimal(29.99), "Office Supplies", true),
            new Product(19L, "Mountain Bike", new BigDecimal(489.0), "Outdoor", true),
            new Product(20L, "Camping Tent", new BigDecimal(270.0), "Outdoor", false),
            new Product(21L, "Office Table", new BigDecimal(567.89), "Office Supplies", true)
        );

        productRepository.saveAll(mockProducts);

    }

    @Test
    public void testGetActiveProductsByFiltersProductName() {
        List<ProductDTO> products = productService.getActiveProductsByFilters(Optional.of("Off"),
                Optional.empty(), Optional.empty(), Optional.empty(), ProductService.Sort.categoryASC);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals("Home Appliances", products.get(0).getCategory());
        Assertions.assertEquals("Office Supplies", products.get(1).getCategory());
        Assertions.assertEquals("Office Supplies", products.get(2).getCategory());

        products = productService.getActiveProductsByFilters(Optional.of("off"),
                Optional.of("of"), Optional.empty(), Optional.empty(), ProductService.Sort.categoryDESC);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals("Home Appliances", products.get(2).getCategory());
        Assertions.assertEquals("Office Supplies", products.get(1).getCategory());
        Assertions.assertEquals("Office Supplies", products.get(0).getCategory());

        products = productService.getActiveProductsByFilters(Optional.of("ofF"),
                Optional.empty(), Optional.empty(), Optional.of(new BigDecimal(220)), ProductService.Sort.priceDESC);
        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(220 >= products.get(0).getPrice().doubleValue());
        Assertions.assertTrue(220 >= products.get(1).getPrice().doubleValue());

        products = productService.getActiveProductsByFilters(Optional.of("off"),
                Optional.empty(), Optional.empty(), Optional.empty(), ProductService.Sort.productNameASC);
        System.out.println(products);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals("Coffee Maker", products.get(0).getName());
        Assertions.assertEquals("Office Chair", products.get(1).getName());
        Assertions.assertEquals("Office Table", products.get(2).getName());

        products = productService.getActiveProductsByFilters(Optional.of("off"),
                Optional.empty(), Optional.empty(), Optional.empty(), ProductService.Sort.productNameDESC);
        System.out.println(products);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals("Coffee Maker", products.get(2).getName());
        Assertions.assertEquals("Office Chair", products.get(1).getName());
        Assertions.assertEquals("Office Table", products.get(0).getName());


    }


    @Test
    public void testGetActiveProductsByFiltersCategory(){
        List<ProductDTO> products = productService.getActiveProductsByFilters(Optional.of("of"),
                Optional.of("oFf"), Optional.empty(), Optional.empty(), ProductService.Sort.priceASC);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals(new BigDecimal("29.99"), products.get(0).getPrice());
        Assertions.assertEquals(new BigDecimal("220.00"), products.get(1).getPrice());
        Assertions.assertEquals(new BigDecimal("567.89"), products.get(2).getPrice());

        products = productService.getActiveProductsByFilters(Optional.empty(),
                Optional.of("OFF"), Optional.empty(), Optional.empty(), ProductService.Sort.priceDESC);
        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals(new BigDecimal("29.99"), products.get(2).getPrice());
        Assertions.assertEquals(new BigDecimal("220.00"), products.get(1).getPrice());
        Assertions.assertEquals(new BigDecimal("567.89"), products.get(0).getPrice());

        products = productService.getActiveProductsByFilters(Optional.empty(),
                Optional.of("off"), Optional.of(new BigDecimal((100))), Optional.empty(), ProductService.Sort.priceASC);
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(new BigDecimal("220.00"), products.get(0).getPrice());
        Assertions.assertEquals(new BigDecimal("567.89"), products.get(1).getPrice());

        products = productService.getActiveProductsByFilters(Optional.empty(),
                Optional.of("off"), Optional.of(new BigDecimal((100))), Optional.of(new BigDecimal((300))), ProductService.Sort.priceASC);
        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(new BigDecimal("220.00"), products.get(0).getPrice());

    }


    @Test
    public void testGetActiveProductsByCategory(){
        List<ProductDTO> outdoor = productService.getProductsByCategory("Outdoor");
        Assertions.assertEquals(1, outdoor.size());
        ProductDTO product = outdoor.get(0);
        Assertions.assertEquals(19, product.getId());
    }

}