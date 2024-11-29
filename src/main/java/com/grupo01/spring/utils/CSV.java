package com.grupo01.spring.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
//import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
//import com.grupo01.spring.service.JuegoService;
import com.grupo01.spring.service.JuegoServiceImpl;

/**
 * Clase para las utilidades del CSV
 * 
 * @version 1.0
 * @author kikev
 */
public class CSV {

	public static File fichero = new File("vgsales.csv");


	private static final Logger log = Logger.getLogger(CSV.class.getName());

	private List<String> lineasCSV;

	@Autowired
	private JuegoServiceImpl juegoService;

	// Constructor
	public CSV() {
		this.lineasCSV = new ArrayList<>();
	}

	/**
	 * Método para leer un archivo CSV y cargarlo en una lista de listas.
	 * 
	 */

	public void leerCSV() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			boolean primeraLinea = true; // Para saltar el encabezado

			while ((linea = br.readLine()) != null) {
				// Saltar la primera línea (encabezado)
				if (primeraLinea) {
					primeraLinea = false;
					continue;
				}
				// Usamos una expresión regular para manejar las comas dentro de las comillas
				String[] valores = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				lineasCSV.add(linea.trim()); // Almacenar solo las líneas de datos
			}
		}
	}

	/**
	 * Obtener los juegos leídos del archivo CSV y guardarlos en la base de datos.
	 */
	public List<Juego> getJuegos() {
		List<Juego> juegos = new ArrayList<>();

		// Convertir cada línea en un objeto Juego
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
				System.err.println("Error al procesar línea: " + linea + " -> " + e.getMessage());
			}
		}
		return juegos; // Asegúrate de que este método devuelva la lista
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
			return 0.0; // Valor predeterminado para errores de formato
		}
	}

	/**
	 * Metodo para guardar, añadiendo, una lista de objetos genericos, comun para
	 * Juegos, Generos, Plataformas...
	 * 
	 * @param <T>     objeto generico
	 * @param objetos Lista de objetos generica, si se quiere añadir uno solo, se ha
	 *                de meter en una lista de un solo objeto
	 * @param f       objeto que hace referencia al fichero CSV, en este caso,
	 *                estatico en la clase CSV
	 * @return true si se ha conseguido hacer con exito, false si no
	 */
//	public static <T> boolean guardarCSV(List<T> objetos, File f) {
//
//		if (objetos == null || objetos.isEmpty()) {
//			System.err.println("No hay objetos para guardar");
//			log.warning("No hay objetos para guardar");
//			return false;
//		}
//
//		try (FileWriter fw = new FileWriter(f, true)) { // "true" añade el contenido y no lo sobreescribe
//			// Escribir encabezado
//			Class<?> clase = objetos.get(0).getClass(); // Obtener la clase del primer objeto
//			Field[] campos = clase.getDeclaredFields(); // Obtener los campos de la clase
//
////			if (!f.exists()) {
////
////				// Escribir nombres de los campos como encabezado
////				for (int i = 0; i < campos.length; i++) {
////					fw.write(campos[i].getName());
////					if (i < campos.length - 1) {
////						fw.write(",");
////					}
////				}
////				fw.write("\n");
////
////			}
//
//			// Escribir los valores de los objetos
//			for (T objeto : objetos) {
//				fw.write("\n");
//				for (int i = 0; i < campos.length; i++) {
//					campos[i].setAccessible(true); // Permitir acceso a campos privados
//					Object valor = campos[i].get(objeto); // Obtener el valor del campo
//					fw.write(valor != null ? valor.toString() : ""); // Manejar valores nulos
//					if (i < campos.length - 1) {
//						fw.write(",");
//					}
//				}
//			}
//
//			System.out.println("Archivo CSV guardado exitosamente");
//			log.info("Archivo CSV guardado exitosamente");
//			return true;
//
//		} catch (IOException | IllegalAccessException e) {
//
//			System.err.println("Error al guardar el CSV: " + e.getMessage());
//			log.warning("Error al guardar el CSV");
//			return false;
//
//		}
//
//	}

}
