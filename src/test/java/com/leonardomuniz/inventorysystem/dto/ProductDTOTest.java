package com.leonardomuniz.inventorysystem.dto;

import com.leonardomuniz.inventorysystem.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDTOTest {

    @Test
    void shouldTestGettersAndSetters() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        BigDecimal price = new BigDecimal("199.99");

        // Act - Invocando os Setters gerados pelo Lombok
        dto.setName("Monitor");
        dto.setDescription("Monitor 4K");
        dto.setPrice(price);
        dto.setQuantity(15);
        dto.setCategory("Electronics");

        // Assert - Invocando os Getters gerados pelo Lombok
        assertEquals("Monitor", dto.getName());
        assertEquals("Monitor 4K", dto.getDescription());
        assertEquals(price, dto.getPrice());
        assertEquals(15, dto.getQuantity());
        assertEquals("Electronics", dto.getCategory());
    }

    @Test
    void shouldConvertFromEntitySuccessfully() {
        // Arrange - Criando a entidade real
        Product product = new Product();
        product.setId(10L);
        product.setName("Keyboard");
        product.setDescription("Mechanical Keyboard");
        product.setPrice(new BigDecimal("150.00"));
        product.setQuantity(50);
        product.setCategory("Peripherals");

        // Act - Usando o seu método estático
        ProductDTO dto = ProductDTO.fromEntity(product);

        // Assert - Verificando se os dados foram passados corretamente
        assertEquals(product.getName(), dto.getName());
        assertEquals(product.getDescription(), dto.getDescription());
        assertEquals(product.getPrice(), dto.getPrice());
        assertEquals(product.getQuantity(), dto.getQuantity());
        assertEquals(product.getCategory(), dto.getCategory());
    }
}