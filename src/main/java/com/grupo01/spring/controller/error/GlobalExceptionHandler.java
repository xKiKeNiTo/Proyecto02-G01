package com.grupo01.spring.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejador de excepciones para errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage()) // Obtiene el mensaje de error
            .collect(Collectors.toList());

        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST); // Devuelve los errores con el estado 400
    }
    
    // Manejador de excepciones para métodos HTTP no soportados
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    	String metodoNoPermitido = ex.getMethod();
    	String mensaje = String.format(
    		"El método %s no está permitido en este endpoint. Métodos soportados: %s",
    		metodoNoPermitido, ex.getSupportedHttpMethods());
    	
    	return new ResponseEntity<>(Map.of("errors", mensaje), HttpStatus.METHOD_NOT_ALLOWED);   	
    }
}
