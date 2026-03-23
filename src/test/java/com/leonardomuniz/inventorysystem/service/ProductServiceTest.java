package com.leonardomuniz.inventorysystem.service;

import com.leonardomuniz.inventorysystem.dto.ProductDTO;
import com.leonardomuniz.inventorysystem.exception.BusinessException;
import com.leonardomuniz.inventorysystem.exception.ResourceNotFoundException;
import com.leonardomuniz.inventorysystem.model.Product;
import com.leonardomuniz.inventorysystem.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private ProductDTO dto;
    private Product product;

    @BeforeEach
    void setup() {
        dto = new ProductDTO();
        dto.setName("Laptop");
        dto.setDescription("Gaming laptop");
        dto.setPrice(BigDecimal.valueOf(5000));
        dto.setQuantity(5);
        dto.setCategory("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(BigDecimal.valueOf(5000));
        product.setQuantity(5);
        product.setCategory("Electronics");
    }

    // ✅ CREATE SUCCESS
    @Test
    void shouldCreateProductSuccessfully() {
        when(repository.existsByNameIgnoreCase(dto.getName())).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product saved = service.create(dto);

        assertNotNull(saved);
        assertEquals("Laptop", saved.getName());
        verify(repository).save(any(Product.class));
    }

    // ❌ CREATE - duplicate name
    @Test
    void shouldThrowWhenNameAlreadyExists() {
        when(repository.existsByNameIgnoreCase(dto.getName())).thenReturn(true);

        assertThrows(BusinessException.class, () -> service.create(dto));
    }

    // ❌ CREATE - price too high
    @Test
    void shouldThrowWhenPriceExceedsLimit() {
        dto.setPrice(BigDecimal.valueOf(2_000_000));
        when(repository.existsByNameIgnoreCase(dto.getName())).thenReturn(false);

        assertThrows(BusinessException.class, () -> service.create(dto));
    }

    // ✅ FIND BY ID SUCCESS
    @Test
    void shouldFindProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product found = service.findById(1L);

        assertEquals("Laptop", found.getName());
    }

    // ❌ FIND BY ID NOT FOUND
    @Test
    void shouldThrowWhenProductNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    // ✅ UPDATE SUCCESS
    @Test
    void shouldUpdateProductSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        Product updated = service.update(1L, dto);

        assertEquals("Laptop", updated.getName());
        verify(repository).save(product);
    }

    // ❌ UPDATE - name conflict
    @Test
    void shouldThrowWhenUpdatingWithDuplicateName() {
        Product another = new Product();
        another.setId(2L);
        another.setName("Phone");

        when(repository.findById(1L)).thenReturn(Optional.of(another));
        when(repository.existsByNameIgnoreCase(dto.getName())).thenReturn(true);

        assertThrows(BusinessException.class, () -> service.update(1L, dto));
    }

    // ❌ UPDATE - quantity too high
    @Test
    void shouldThrowWhenQuantityExceedsLimit() {
        dto.setQuantity(20_000);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(BusinessException.class, () -> service.update(1L, dto));
    }

    // ❌ DELETE - stock > 0
    @Test
    void shouldNotDeleteWhenStockAvailable() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(BusinessException.class, () -> service.delete(1L));
        verify(repository, never()).delete(any());
    }

    // ✅ DELETE SUCCESS
    @Test
    void shouldDeleteWhenStockIsZero() {
        product.setQuantity(0);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repository).delete(product);
    }

    // ✅ UPDATE - change name successfully (Cobre o branch que faltava)
    @Test
    void shouldUpdateProductWhenChangingToValidNewName() {
        // O produto existente no banco se chama "Laptop"
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        // O usuário quer mudar o nome para "Desktop"
        dto.setName("Desktop");

        // Simulamos que o nome "Desktop" AINDA NÃO existe no banco
        when(repository.existsByNameIgnoreCase("Desktop")).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product updated = service.update(1L, dto);

        assertEquals("Desktop", updated.getName());
        verify(repository).save(product);
    }
}
