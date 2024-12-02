package com.grupo01.spring.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;

public interface JuegoDao extends JpaRepository<Juego, Long>{

	
	// Lista todos los juegos del siglo XX
    @Query("FROM Juego WHERE year <= 2000")
    List<Juego> listarPorSiglo();

	List<Juego> findByGenre(Genre genre);


	/**
	 * Query para filtrar por la plataforma en la que se ha publicado
	 * 
	 * @param anotacion que permite asociar el parametro "consola" de la consulta con el argumento del metodo.
	 */
	@Query("FROM Juego WHERE platform = :consola")
	List<Juego> listarPorConsola(@Param("consola") String consola);

	
}
