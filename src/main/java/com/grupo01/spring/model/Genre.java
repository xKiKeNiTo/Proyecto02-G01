package com.grupo01.spring.model;

/**
 * Enum Genre 
 * @version 1
 * @author raul_
 * 29/11/2024
 */

public enum Genre{
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
	
	//Método para mapear los valores del CSV al Enum Genre
	public static Genre fromString(String genreText) {
	    switch (genreText) {
	        case "Role-Playing":
	            return Role_Playing;
	        default:
	            return Genre.valueOf(genreText); // Si coincide exactamente, usar el valor del Enum
	    }
	}
};


