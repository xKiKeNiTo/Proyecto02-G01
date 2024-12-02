package com.grupo01.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupo01.spring.model.Juego;

public interface JuegoDao extends JpaRepository<Juego, Integer>{
	
	// Lista todos los juegos del siglo XX
    @Query("FROM Juego WHERE year <= 2000")
    List<Juego> listarPorSiglo();

}
