package com.grupo01.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;

/**
 * Repositorio para la gestión de la entidad Juego.
 *
 * Proporciona consultas personalizadas para la base de datos.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
public interface JuegoDao extends JpaRepository<Juego, Long> {

	/**
	 * Lista todos los juegos publicados en o antes del año 2000 (siglo XX).
	 *
	 * @return Lista de juegos del siglo XX.
	 */
	@Query("FROM Juego WHERE year <= 2000")
	List<Juego> listarPorSiglo();

	/**
	 * Lista todos los juegos por género.
	 *
	 * @param genre Género de los juegos a buscar.
	 * @return Lista de juegos del género especificado.
	 */
	List<Juego> findByGenre(Genre genre);

	/**
	 * Lista todos los juegos filtrados por plataforma.
	 *
	 * @param plataforma Plataforma en la que se publicaron los juegos.
	 * @return Lista de juegos de la plataforma especificada.
	 */
	@Query("FROM Juego WHERE platform = :plataforma")
	List<Juego> listarPorConsola(@Param("plataforma") Platform plataforma);

	/**
	 * find by year
	 *
	 * @param year year
	 * @return {@link List}
	 * @see List
	 * @see Juego
	 */
	@Query("FROM Juego WHERE year =?1")
	List<Juego> findByYear(long year);

	/**
	 * Lista todos los juegos con ventas superiores a la media.
	 *
	 * @return Lista de juegos con ventas globales con valor superior a la media.
	 */
	@Query("FROM Juego WHERE globalSales > (SELECT AVG(globalSales) FROM Juego)")
	List<Juego> listarJuegosVentasSuperiores();

	/**
	 * delete by console and before
	 *
	 * @param plataform plataform
	 * @param year      year
	 * @return {@link List}
	 * @see List
	 * @see Juego
	 */
	@Query("FROM Juego WHERE platform = :plataform AND year<= :year")
	List<Juego> deleteByConsoleAndBefore(@Param("plataform") Platform plataform, @Param("year") Long year);
}
