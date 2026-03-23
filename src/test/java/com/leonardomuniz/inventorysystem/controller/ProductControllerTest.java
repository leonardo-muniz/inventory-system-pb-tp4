package com.leonardomuniz.inventorysystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.leonardomuniz.inventorysystem.dto.ProductDTO;
import com.leonardomuniz.inventorysystem.exception.BusinessException;
import com.leonardomuniz.inventorysystem.model.Product;
import com.leonardomuniz.inventorysystem.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // --- TESTES: list ---
    @Test
    void list_DeveRetornarViewProductsList() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));
    }

    // --- TESTES: createForm ---
    @Test
    void createForm_DeveRetornarViewProductsForm() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attributeExists("productDTO"))
                .andExpect(model().attribute("productId", 0L));
    }

    // --- TESTES: create ---
    @Test
    void create_DeveCriarProdutoERedirecionar() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Notebook")
                        .param("description", "Notebook de teste")
                        .param("price", "3500.00")
                        .param("quantity", "10")
                        .param("category", "Eletrônicos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(productService, times(1)).create(any(ProductDTO.class));
    }

    @Test
    void create_DeveRetornarFormQuandoHouverErroDeValidacao() throws Exception {
        // Enviar requisição sem parâmetros para forçar erro no @Valid
        mockMvc.perform(post("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    void create_DeveRetornarViewErrorQuandoBusinessException() throws Exception {
        doThrow(new BusinessException("Erro de negócio")).when(productService).create(any(ProductDTO.class));

        mockMvc.perform(post("/products")
                        .param("name", "Notebook")
                        .param("description", "Notebook de teste")
                        .param("price", "3500.00")
                        .param("quantity", "10")
                        .param("category", "Eletrônicos"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", 400))
                .andExpect(model().attributeExists("errorMessage"));
    }

    // --- TESTES: editForm ---
    @Test
    void editForm_DeveRetornarViewProductsFormQuandoEncontrarProduto() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setName("Notebook");
        mockProduct.setPrice(new java.math.BigDecimal("3500.00"));
        mockProduct.setQuantity(10);
        mockProduct.setCategory("Eletrônicos");

        when(productService.findById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/products/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attributeExists("productDTO"))
                .andExpect(model().attribute("productId", 1L));
    }

    @Test
    void editForm_DeveRetornarViewErrorQuandoProdutoNaoEncontrado() throws Exception {
        when(productService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/products/99/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", 404))
                .andExpect(model().attributeExists("errorMessage"));
    }

    // --- TESTES: update ---
    @Test
    void update_DeveAtualizarProdutoERedirecionar() throws Exception {
        mockMvc.perform(post("/products/1")
                        .param("name", "Notebook Atualizado")
                        .param("description", "Descrição atualizada")
                        .param("price", "4000.00")
                        .param("quantity", "15")
                        .param("category", "Eletrônicos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(productService, times(1)).update(eq(1L), any(ProductDTO.class));
    }

    @Test
    void update_DeveRetornarFormQuandoHouverErroDeValidacao() throws Exception {
        mockMvc.perform(post("/products/1")) // Sem parâmetros forçará o erro
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attribute("productId", 1L))
                .andExpect(model().hasErrors());
    }

    @Test
    void update_DeveRetornarViewErrorQuandoBusinessException() throws Exception {
        doThrow(new BusinessException("Erro de negócio")).when(productService).update(eq(1L), any(ProductDTO.class));

        mockMvc.perform(post("/products/1")
                        .param("name", "Notebook")
                        .param("price", "3500.00")
                        .param("quantity", "10")
                        .param("category", "Eletrônicos"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", 400));
    }

    // --- TESTES: delete ---
    @Test
    void delete_DeveDeletarProdutoERedirecionar() throws Exception {
        mockMvc.perform(post("/products/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(productService, times(1)).delete(1L);
    }

    @Test
    void delete_DeveRetornarViewErrorQuandoBusinessException() throws Exception {
        doThrow(new BusinessException("Erro de negócio ao excluir")).when(productService).delete(1L);

        mockMvc.perform(post("/products/1/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("status", 400));
    }
}