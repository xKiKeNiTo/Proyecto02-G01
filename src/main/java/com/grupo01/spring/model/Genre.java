package com.grupo01.spring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeración para representar los géneros de los juegos.
 * 
 * Proporciona una lista predefinida de géneros y métodos para trabajar con ellos.
 *
 * @version 1.0
 * @author Raul
 * @date 29/11/2024
 */
public enum Genre {
    Action,
    Adventure,
    Fighting,
    Misc,
    Platform,
    Puzzle,
    Racing,
    Role_Playing,
    Shooter,
    Simulation,
    Sports,
    Strategy;

    private static final Logger logger = LoggerFactory.getLogger(Genre.class);

    /**
     * Método para mapear los valores del CSV al Enum Genre.
     *
     * @param genreText El texto del género a mapear.
     * @return El valor del Enum correspondiente.
     * @throws IllegalArgumentException Si el texto no coincide con ningún valor del Enum.
     */
    public static Genre fromString(String genreText) {
        try {
            if ("Role-Playing".equalsIgnoreCase(genreText)) {
                return Role_Playing;
            }
            return Genre.valueOf(genreText.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("El valor '{}' no es un género válido", genreText, e);
            throw e;
        }
    }
}
