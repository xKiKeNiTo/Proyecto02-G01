package com.grupo01.spring.controller.error;

/**
 * Clase para poder personalizar lo que vamos a devolver en las excepciones
 */
public class CustomException extends RuntimeException {
	private final int errorCode;

	public CustomException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
