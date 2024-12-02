package com.grupo01.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo01.spring.model.Juego;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoDao extends JpaRepository<Juego, Long>{

    @Query("FROM Juego WHERE year =?1")
    List<Juego> findByYear(long year);
}
