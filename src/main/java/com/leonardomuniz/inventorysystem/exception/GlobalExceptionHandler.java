package com.leonardomuniz.inventorysystem.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusiness(BusinessException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // Validation errors (@Valid in forms)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("errorMessage", "Validation error. Please check your input.");
        return "error";
    }

    // Bean validation (rare but good to cover)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraint(ConstraintViolationException ex, Model model) {
        model.addAttribute("errorMessage", "Constraint violation.");
        return "error";
    }

    // Database integrity issues (unique constraints, etc)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDatabase(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("errorMessage", "Database integrity error.");
        return "error";
    }

    // Fallback (last line of defense)
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Unexpected error occurred.");
        return "error";
    }
}
