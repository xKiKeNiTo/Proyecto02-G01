package com.grupo01.spring.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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
	 * Captura excepciones de validación y retorna una respuesta simplificada.
	 *
	 * @param ex Excepción generada por errores de validación.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		// Construir una respuesta simplificada
		List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
			Map<String, Object> error = new HashMap<>();
			error.put("field", fieldError.getField());
			error.put("message", fieldError.getDefaultMessage());
			error.put("code", HttpStatus.BAD_REQUEST.value());
			return error;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Captura excepciones de deserialización de valores inválidos y retorna una
	 * respuesta simplificada.
	 *
	 * @param ex Excepción generada por valores inválidos en deserialización.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
		String fieldName = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		String message = String.format("El valor '%s' no es válido para el campo '%s'. Valores aceptados: %s",
				ex.getValue(), fieldName,
				ex.getTargetType().getEnumConstants() != null ? List.of(ex.getTargetType().getEnumConstants())
						: "Desconocido");

		Map<String, Object> error = Map.of("field", fieldName, "message", message, "code",
				HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(Map.of("errors", List.of(error)), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Manejador de excepciones personalizadas.
	 *
	 * @param ex Excepción personalizada lanzada por la lógica de negocio.
	 * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP
	 *         correspondiente.
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
	 * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP
	 *         405 (Method Not Allowed).
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		String metodoNoPermitido = ex.getMethod();
		String mensaje = String.format("El método %s no está permitido en este endpoint. Métodos soportados: %s",
				metodoNoPermitido, ex.getSupportedHttpMethods());

		return new ResponseEntity<>(Map.of("errors", mensaje), HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Captura excepciones de tipo `MethodArgumentTypeMismatchException` y retorna
	 * un mensaje simplificado.
	 *
	 * @param ex Excepción lanzada cuando un argumento no puede ser convertido.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		// Obtener el nombre del campo y los valores aceptados si es un Enum
		String fieldName = ex.getName();
		String rejectedValue = String.valueOf(ex.getValue());
		String validValues = ex.getRequiredType() != null && ex.getRequiredType().isEnum()
				? List.of(ex.getRequiredType().getEnumConstants()).toString()
				: "N/A";

		String message = String.format("El valor '%s' no es válido para el campo '%s'. Valores aceptados: %s",
				rejectedValue, fieldName, validValues);

		// Crear el cuerpo de respuesta
		Map<String, Object> errorResponse = Map.of("field", fieldName, "message", message, "code",
				HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(Map.of("errors", List.of(errorResponse)), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex) {
		Map<String, Object> errorResponse = Map.of("message", "La ruta solicitada no existe: " + ex.getRequestURL(),
				"code", 404);

		return new ResponseEntity<>(Map.of("errors", List.of(errorResponse)), HttpStatus.NOT_FOUND);
	}

}
