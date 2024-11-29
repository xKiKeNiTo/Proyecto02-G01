package com.grupo01.spring.service;
import java.util.List;

import com.grupo01.spring.model.Juego;

public interface JuegoService {

    //Para listar todos los juegos
    public List<Juego> findAll();
}
