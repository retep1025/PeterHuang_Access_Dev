package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.access.productInventoryTracker.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    // Helper method to convert Product to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.isAvailable()
        );
    }
    
    // Get all products as DTOs
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


    public static final String wildCard = "%%%s%%";

    /**
     *
     * @param productName - find by using wild card search min character length to start search is 3 characters
     * @param category - find by using wild card search min character length to start search is 3 characters
     * @param startPrice - price greater or equal to the starting price
     * @param endPrice - price less than or equal to end price
     * @return products that match the filter condition(s), or empty list if no filter are specified
     */
    public List<ProductDTO> getActiveProductsByFilters(Optional<String> productName, Optional<String> category,
                                                       Optional<BigDecimal> startPrice, Optional<BigDecimal> endPrice,
                                                       Sort sort
                                                       ) {

        Optional<String> productNameSearch = productName.map(name -> {
            return name.length() >= 3? String.format(wildCard, name.toLowerCase()):null;
        });

        Optional<String> categorySearch = category.map(cat -> {
            return cat.length() >= 3? String.format(wildCard, cat.toLowerCase()): null;
        });

        if(productNameSearch.isPresent() || categorySearch.isPresent() || startPrice.isPresent() || endPrice.isPresent()) {
            List<ProductDTO> products = productRepository.findByFilters(productNameSearch.orElse(null),
                            categorySearch.orElse(null), startPrice.orElse(null)
                            , endPrice.orElse(null))
                    .parallelStream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            products.sort(sort.getComparator());

            return products;
        } else {
            return Collections.EMPTY_LIST;
        }
    }


    // AI Generated
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findProductsByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public static enum Sort{
        productNameASC(Comparator.comparing(ProductDTO::getName)),
        productNameDESC(Comparator.comparing(ProductDTO::getName).reversed()),
        categoryASC(Comparator.comparing(ProductDTO::getCategory)),
        categoryDESC(Comparator.comparing(ProductDTO::getCategory).reversed()),
        priceASC(Comparator.comparing(ProductDTO::getPrice)),
        priceDESC(Comparator.comparing(ProductDTO::getPrice).reversed()),
        ;
        private final Comparator<ProductDTO> comparator;

        private Sort(Comparator<ProductDTO> comparator) {
            this.comparator = comparator;
        }

        public Comparator<ProductDTO> getComparator() {
            return comparator;
        }

    }
}
