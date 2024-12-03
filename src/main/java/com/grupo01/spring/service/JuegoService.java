package com.grupo01.spring.service;

import java.util.List;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;

/**
 * Interfaz para la gestión de juegos.
 *
 * Define las operaciones CRUD y consultas personalizadas para la entidad Juego.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
public interface JuegoService {

	/**
	 * Recupera todos los juegos almacenados en la base de datos.
	 *
	 * @return Lista de todos los juegos.
	 */
	List<Juego> findAll();

	/**
	 * Guarda un juego en la base de datos.
	 *
	 * @param juego El objeto Juego a guardar.
	 * @return El objeto guardado (incluyendo el ID generado).
	 */
	Juego save(Juego juego);

	/**
	 * Recupera una lista de juegos que pertenecen al género especificado.
	 *
	 * @param genre El género de los juegos a buscar.
	 * @return Lista de juegos del género especificado.
	 */
	List<Juego> findByGenre(Genre genre);

	/**
	 * Recupera todos los juegos publicados en el siglo XX.
	 *
	 * @return Lista de juegos publicados antes del año 2001.
	 */
	List<Juego> listarPorSiglo();

	/**
	 * Elimina un juego de la base datos dado su identificador.
	 *
	 * @param id El identificador del juego a borrar.
	 * @return El juego eliminado.
	 */
	Juego deleteById(long id);

	/**
	 * Recupera una lista de juegos publicados en una plataforma específica.
	 *
	 * @param plataforma La plataforma de la que queremos obtener el listado.
	 * @return Lista de juegos publicados en la plataforma especificada.
	 */
	List<Juego> listarPorConsola(Platform plataforma);

	List<Juego> findByYear(long year);
}
