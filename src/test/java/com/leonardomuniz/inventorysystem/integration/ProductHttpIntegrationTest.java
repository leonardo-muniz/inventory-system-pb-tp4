package com.leonardomuniz.inventorysystem.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductHttpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFullFlow() throws Exception {
        // 1. GET Listagem
        mockMvc.perform(get("/products")).andExpect(status().isOk());

        // 2. POST Create Sucesso
        mockMvc.perform(post("/products")
                        .param("name", "Integrado")
                        .param("price", "50.0")
                        .param("quantity", "5")
                        .param("category", "Geral"))
                .andExpect(status().is3xxRedirection());

        // 3. POST Create Erro de Validação
        mockMvc.perform(post("/products")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"));

        // 4. POST Delete (Ajustado para o que o seu código realmente faz)
        // Como o produto tem estoque (quantity=5), ele vai cair na sua regra de negócio
        mockMvc.perform(post("/products/1/delete"))
                .andExpect(status().isOk()) // Mudamos de is3xxRedirection para isOk
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", "Cannot delete product with stock available"));
    }
}