package com.leonardomuniz.inventorysystem.service;

import com.leonardomuniz.inventorysystem.dto.ProductDTO;
import com.leonardomuniz.inventorysystem.exception.BusinessException;
import com.leonardomuniz.inventorysystem.exception.ResourceNotFoundException;
import com.leonardomuniz.inventorysystem.model.Product;
import com.leonardomuniz.inventorysystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product create(ProductDTO dto) {

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessException("Product with this name already exists");
        }

        if (dto.getPrice().doubleValue() > 1_000_000) {
            throw new BusinessException("Price exceeds allowed limit");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());

        return repository.save(product);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product update(Long id, ProductDTO dto) {

        Product existing = findById(id);

        if (!existing.getName().equalsIgnoreCase(dto.getName())
                && repository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessException("Another product with this name already exists");
        }

        if (dto.getQuantity() > 10_000) {
            throw new BusinessException("Quantity exceeds stock policy limit");
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setQuantity(dto.getQuantity());
        existing.setCategory(dto.getCategory());

        return repository.save(existing);
    }

    public void delete(Long id) {

        Product product = findById(id);

        if (product.getQuantity() > 0) {
            throw new BusinessException("Cannot delete product with stock available");
        }

        repository.delete(product);
    }
}
