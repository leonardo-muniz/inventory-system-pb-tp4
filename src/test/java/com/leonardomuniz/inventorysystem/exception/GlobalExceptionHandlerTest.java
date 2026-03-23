package com.leonardomuniz.inventorysystem.exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private Model model;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        // O ExtendedModelMap é uma implementação real e super leve do Model do Spring
        // Perfeita para testar sem precisar subir o contexto web!
        model = new ExtendedModelMap();
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found message");
        String viewName = exceptionHandler.handleNotFound(ex, model);

        assertEquals("error", viewName);
        assertEquals("Not found message", model.getAttribute("errorMessage"));
    }

    @Test
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Business error message");
        String viewName = exceptionHandler.handleBusiness(ex, model);

        assertEquals("error", viewName);
        assertEquals("Business error message", model.getAttribute("errorMessage"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Criamos a exceção passando null nos parâmetros, o que é suficiente para o teste unitário
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, new org.springframework.validation.BeanPropertyBindingResult(null, "null"));

        String viewName = exceptionHandler.handleValidation(ex, model);

        assertEquals("error", viewName);
        assertEquals("Validation error. Please check your input.", model.getAttribute("errorMessage"));
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolationException ex = new ConstraintViolationException("Constraint error", Collections.emptySet());
        String viewName = exceptionHandler.handleConstraint(ex, model);

        assertEquals("error", viewName);
        assertEquals("Constraint violation.", model.getAttribute("errorMessage"));
    }

    @Test
    void shouldHandleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("DB error");
        String viewName = exceptionHandler.handleDatabase(ex, model);

        assertEquals("error", viewName);
        assertEquals("Database integrity error.", model.getAttribute("errorMessage"));
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new Exception("Some random generic error");
        String viewName = exceptionHandler.handleGeneric(ex, model);

        assertEquals("error", viewName);
        assertEquals("Unexpected error occurred.", model.getAttribute("errorMessage"));
    }
}