package com.grupo01.spring.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase para manejar excepciones globales en la aplicación. Captura y gestiona
 * errores comunes de manera centralizada.
 *
 * @version 1.0
 * @author Raul
 * @date 02/12/2024
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Manejador de excepciones para errores de validación.
	 *
	 * @param ex La excepción que contiene los errores de validación.
	 * @return Una respuesta con los mensajes de error y el estado HTTP 400.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage()) // Obtiene el mensaje de error
				.collect(Collectors.toList());

		return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST); // Devuelve los errores con el
																						// estado 400
	}

	
	/**
     * Manejador de excepciones personalizadas.
     *
     * @param ex Excepción personalizada lanzada por la lógica de negocio.
     * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP correspondiente.
     */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		Map<String, Object> errorResponse = Map.of("message", ex.getMessage(), "code", ex.getErrorCode());
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode()));
	}

	/**
     * Manejador de excepciones para métodos HTTP no soportados.
     *
     * @param ex Excepción lanzada cuando se usa un método HTTP no soportado.
     * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP 405 (Method Not Allowed).
     */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		String metodoNoPermitido = ex.getMethod();
		String mensaje = String.format("El método %s no está permitido en este endpoint. Métodos soportados: %s",
				metodoNoPermitido, ex.getSupportedHttpMethods());

		return new ResponseEntity<>(Map.of("errors", mensaje), HttpStatus.METHOD_NOT_ALLOWED);
	}
}
