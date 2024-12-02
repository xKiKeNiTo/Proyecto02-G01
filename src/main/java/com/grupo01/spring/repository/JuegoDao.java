package com.grupo01.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;

public interface JuegoDao extends JpaRepository<Juego, Long>{

	List<Juego> findByGenre(Genre genre);
	
}
