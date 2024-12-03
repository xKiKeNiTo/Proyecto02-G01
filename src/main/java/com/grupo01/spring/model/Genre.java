package com.grupo01.spring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeración para representar los géneros de los juegos.
 *
 * Proporciona una lista predefinida de géneros y métodos para trabajar con ellos.
 *
 * @version 1.3
 * @author Raul
 * @date 03/12/2024
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
     * @return El valor del Enum correspondiente, o null si no se encuentra una coincidencia.
     */
    public static Genre fromString(String genreText) {
        if (genreText == null || genreText.isBlank()) {
            logger.warn("El género proporcionado está vacío o es nulo");
            return null;
        }

        try {
            // Convertir el texto a "Title Case" para coincidir con el formato del enum del CSV
            String normalizedGenre = genreText.substring(0, 1).toUpperCase() + genreText.substring(1).toLowerCase();
            return Genre.valueOf(normalizedGenre);
        } catch (IllegalArgumentException e) {
            logger.warn("El valor '{}' no es un género válido. Será ignorado.", genreText);
            return null; // Retorna null en caso de valor inválido
        }
    }
}
