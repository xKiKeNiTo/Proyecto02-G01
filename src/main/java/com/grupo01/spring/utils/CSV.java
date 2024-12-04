package com.grupo01.spring.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;

/**
 * Clase para las utilidades del manejo de archivos CSV.
 * 
 * Proporciona métodos para leer y procesar datos desde un archivo CSV.
 *
 * @version 1.0
 * @author Kike
 * @date 29/11/2024
 */
@Component
public class CSV {

	private static final Logger log = Logger.getLogger(CSV.class.getName());

	/**
	 * Archivo CSV de entrada.
	 */
	public static final File fichero = new File("vgsales.csv");

	/**
	 * Líneas leídas desde el archivo CSV.
	 */
	private List<String> lineasCSV;

	/**
	 * Constructor principal. Inicializa la lista para almacenar líneas del CSV.
	 */
	public CSV() {
		this.lineasCSV = new ArrayList<>();
	}

	/**
	 * Establece manualmente las líneas CSV para pruebas o personalización.
	 * 
	 * @param lineasCSV Lista de líneas en formato CSV.
	 */
	public void setLineasCSV(List<String> lineasCSV) {
		this.lineasCSV = lineasCSV;
	}

	/**
	 * Método para leer un archivo CSV y cargar los datos en una lista de cadenas.
	 * 
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	public void leerCSV() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			boolean primeraLinea = true; // Para saltar el encabezado

			while ((linea = br.readLine()) != null) {
				if (primeraLinea) {
					primeraLinea = false; // Saltar la primera línea
					continue;
				}
				lineasCSV.add(linea.trim());
			}
		}
	}

	/**
	 * Convierte las líneas del archivo CSV en objetos `Juego`.
	 * 
	 * @return Lista de objetos `Juego` procesados desde el archivo CSV.
	 */
	public List<Juego> getJuegos() {
		List<Juego> juegos = new ArrayList<>();

		for (String linea : lineasCSV) {
			String[] valores = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			try {
				int rank = Integer.parseInt(valores[0].trim());
				String name = valores[1].trim();
				Platform platform = Platform.fromString(valores[2].trim());

				int year;
				try {
					year = Integer.parseInt(valores[3].trim());
				} catch (NumberFormatException e) {
					year = 0; // Valor predeterminado
				}

				Genre genre = Genre.fromString(valores[4].trim());
				String publisher = valores[5].trim();
				double naSales = parseDoubleSafe(valores[6]);
				double euSales = parseDoubleSafe(valores[7]);
				double jpSales = parseDoubleSafe(valores[8]);
				double otherSales = parseDoubleSafe(valores[9]);
				double globalSales = parseDoubleSafe(valores[10]);

				Juego juego = new Juego(0, rank, name, platform, year, genre, publisher, naSales, euSales, jpSales,
						otherSales, globalSales);
				juegos.add(juego);

			} catch (IllegalArgumentException e) {
				log.warning("Error al procesar línea: " + linea + " -> " + e.getMessage());
			}
		}
		return juegos;
	}

	/**
	 * Método auxiliar para manejar conversiones seguras de String a Double.
	 * 
	 * @param value El valor String a convertir.
	 * @return El valor Double convertido o 0.0 si el valor es inválido.
	 */
	private double parseDoubleSafe(String value) {
		try {
			return Double.parseDouble(value.trim());
		} catch (NumberFormatException e) {
			return 0.0; // Valor predeterminado
		}
	}
}
