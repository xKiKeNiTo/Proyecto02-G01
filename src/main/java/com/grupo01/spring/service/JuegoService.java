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
    
    /**
     * Elimina un juego de la base datos pasado su identificador
     * 
     * @param id el identificador del juego a borrar
     * @return boolean true si se ha eliminado correctamente, false si no
     */
    public boolean deleteById(long id);
    
    /**
     * Lista todos los juegos que se publicaron en una plataforma
     * 
     * @param plataforma, El nombre de la plataforma de la que queremos obtener el listado
     * @return Lista con los los juegos
     */
    public List<Juego> listarPorConsola(String plataforma);
    
}
