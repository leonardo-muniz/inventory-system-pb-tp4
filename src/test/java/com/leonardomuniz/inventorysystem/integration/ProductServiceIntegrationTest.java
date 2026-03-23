package com.leonardomuniz.inventorysystem.integration;

import com.leonardomuniz.inventorysystem.dto.ProductDTO;
import com.leonardomuniz.inventorysystem.exception.BusinessException;
import com.leonardomuniz.inventorysystem.model.Product;
import com.leonardomuniz.inventorysystem.repository.ProductRepository;
import com.leonardomuniz.inventorysystem.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateProductSuccessfully() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setDescription("Gaming machine");
        dto.setPrice(new BigDecimal("5000"));
        dto.setQuantity(10);
        dto.setCategory("Electronics");

        Product saved = service.create(dto);

        assertNotNull(saved.getId());
        assertEquals("Laptop", saved.getName());
        assertEquals(1, repository.count());
    }

    @Test
    void shouldNotAllowDuplicateName() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setPrice(new BigDecimal("5000"));
        dto.setQuantity(10);
        dto.setCategory("Electronics");

        service.create(dto);

        ProductDTO duplicate = new ProductDTO();
        duplicate.setName("Laptop");
        duplicate.setPrice(new BigDecimal("6000"));
        duplicate.setQuantity(5);
        duplicate.setCategory("Electronics");

        assertThrows(BusinessException.class, () -> service.create(duplicate));
    }

    @Test
    void shouldFindAllProducts() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Mouse");
        dto.setPrice(new BigDecimal("100"));
        dto.setQuantity(50);
        dto.setCategory("Accessories");

        service.create(dto);

        List<Product> products = service.findAll();

        assertEquals(1, products.size());
    }

    @Test
    void shouldDeleteProductWhenNoStock() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Keyboard");
        dto.setPrice(new BigDecimal("200"));
        dto.setQuantity(0);
        dto.setCategory("Accessories");

        Product product = service.create(dto);

        service.delete(product.getId());

        assertEquals(0, repository.count());
    }

    @Test
    void shouldNotDeleteProductWithStock() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Monitor");
        dto.setPrice(new BigDecimal("1500"));
        dto.setQuantity(5);
        dto.setCategory("Electronics");

        Product product = service.create(dto);

        assertThrows(BusinessException.class, () -> service.delete(product.getId()));
    }
}
