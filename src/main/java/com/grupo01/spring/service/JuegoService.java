package com.grupo01.spring.service;
import java.util.List;

import com.grupo01.spring.model.Juego;

public interface JuegoService {

    //Para listar todos los juegos
    public List<Juego> findAll();
    
    /**
     * Guarda un juego en la base de datos.
     * 
     * @param juego El objeto Juego a guardar.
     * @return El objeto guardado (incluyendo el ID generado).
     */
    public Juego save(Juego juego);
    
    public Juego deleteById(long id);
    
}
