package com.leonardomuniz.inventorysystem.controller;

import com.leonardomuniz.inventorysystem.dto.ProductDTO;
import com.leonardomuniz.inventorysystem.exception.BusinessException;
import com.leonardomuniz.inventorysystem.model.Product;
import com.leonardomuniz.inventorysystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // Listar produtos
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", service.findAll());
        return "products/list";
    }

    // Formulário de criação
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("productId", 0L); // padronizando o modelAttribute
        return "products/form";
    }

    // Criar produto
    @PostMapping
    public String create(@Valid @ModelAttribute("productDTO") ProductDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "products/form";
        }

        try {
            service.create(dto);
        } catch (BusinessException ex) {
            model.addAttribute("errorTitle", "Violação de Regra de Negócio");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("status", 400); // ✅ agora o status é definido
            return "error";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Produto criado com sucesso!");
        return "redirect:/products";
    }

    // Formulário de edição
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        Product product = service.findById(id);
        if (product == null) {
            model.addAttribute("errorTitle", "Produto não encontrado");
            model.addAttribute("errorMessage", "O produto com id " + id + " não existe");
            model.addAttribute("status", 404);
            return "error";
        }

        model.addAttribute("productDTO", ProductDTO.fromEntity(product));
        model.addAttribute("productId", id);

        return "products/form";
    }

    // Atualizar produto
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("productDTO") ProductDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            return "products/form";
        }

        try {
            service.update(id, dto);
        } catch (BusinessException ex) {
            model.addAttribute("errorTitle", "Violação de Regra de Negócio");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("status", 400);
            return "error";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Produto atualizado com sucesso!");
        return "redirect:/products";
    }

    // Deletar produto
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
        } catch (BusinessException ex) {
            model.addAttribute("errorTitle", "Violação de Regra de Negócio");
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("status", 400);
            return "error";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Produto excluído com sucesso!");
        return "redirect:/products";
    }
}
