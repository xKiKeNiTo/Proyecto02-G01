package com.grupo01.spring.controller;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.service.JuegoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/juegos")
public class JuegoController {

    @Autowired
    private JuegoService service;

    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);

    //Listar juegos
    @GetMapping
    public ResponseEntity<RespuestaApi<List<Juego>>> findAll() {
        List<Juego> juegos = service.findAll();
        if(juegos.isEmpty()){
            return ResponseEntity.status(204).body(
                    new RespuestaApi<>("No se encontraron juegos",204,null)
            );
        }

        return ResponseEntity.status(200).body(
                new RespuestaApi<>("Lista de juegos encontrada",200,juegos)
        );
    }
}
