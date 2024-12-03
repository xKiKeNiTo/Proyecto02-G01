package com.grupo01.spring.controller;

/**
 * Clase genérica para manejar las respuestas de la API.
 *
 * @param <T> Tipo genérico de los datos incluidos en la respuesta.
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
public class RespuestaApi<T> {

	private String mensaje;
	private int status;
	private T data;

	/**
	 * Constructor principal para inicializar la respuesta.
	 *
	 * @param mensaje Mensaje descriptivo de la respuesta.
	 * @param status  Código de estado HTTP de la respuesta.
	 * @param data    Datos específicos de la respuesta.
	 */
	public RespuestaApi(String mensaje, int status, T data) {
		this.mensaje = mensaje;
		this.status = status;
		this.data = data;
	}

	/**
	 * Obtiene el mensaje descriptivo.
	 *
	 * @return El mensaje de la respuesta.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Establece un nuevo mensaje descriptivo.
	 *
	 * @param mensaje El mensaje a establecer.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene el código de estado HTTP.
	 *
	 * @return El código de estado HTTP.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Establece un nuevo código de estado HTTP.
	 *
	 * @param status El código de estado a establecer.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Obtiene los datos específicos de la respuesta.
	 *
	 * @return Los datos incluidos en la respuesta.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Establece nuevos datos para la respuesta.
	 *
	 * @param data Los datos a establecer.
	 */
	public void setData(T data) {
		this.data = data;
	}
}
