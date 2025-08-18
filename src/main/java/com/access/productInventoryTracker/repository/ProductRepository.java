package com.access.productInventoryTracker.repository;

import com.access.productInventoryTracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can add custom methods here if needed, for example: 
    // List<Product> findByCategory(String category);


    //not a good idea to do select *
    @Query(value = "SELECT p.id, p.name, p.price, p.category, p.available FROM product p " +
                   "WHERE p.category = :category " +
                   "AND p.available = true " +
                   "ORDER BY p.price DESC",
           nativeQuery = true)
    List<Product> findProductsByCategory(@Param("category") String category);


    /**
     * This method should perform for most shops that contains few thousand products, but it will not scale
     * with product with higher number of products because of the wild card search is slowing database search on
     * product name and category
     * @param productName
     * @param category
     * @param startPrice
     * @param endPrice
     * @param available
     * @return
     */
    @Query(value = "SELECT p.id, p.name, p.price, p.category, p.available FROM product p " +
            "WHERE (:category IS NULL OR LOWER(p.category) LIKE :category) " +
            "AND (:productName IS NULL OR LOWER(p.name) LIKE :productName) " +
            "AND (:startPrice IS NULL OR p.price >= :startPrice) " +
            "AND (:endPrice IS NULL OR p.price <= :endPrice) " +
            "AND (:available IS NULL OR p.available = :available) ",
            nativeQuery = true)
    List<Product> findByFilters(@Param("productName") String productName, @Param("category") String category,
                                @Param("startPrice") BigDecimal startPrice, @Param("endPrice") BigDecimal endPrice,
                                @Param("available") Boolean available);


}
