package com.grupo01.spring;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.utils.CSV;

/**
 * Clase de pruebas unitarias para la clase `CSV`.
 *
 * Valida la funcionalidad de lectura y procesamiento de datos desde archivos
 * CSV.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
public class CSVTest {

	private CSV csv;

	/**
	 * Configuración inicial para las pruebas.
	 */
	@BeforeEach
	void setUp() {
		csv = new CSV();
	}

	/**
	 * Verifica que el archivo CSV se lee correctamente y contiene el número
	 * esperado de líneas.
	 *
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	@Test
	void leeCSV() throws IOException {
		// Verifica que el archivo existe antes de leerlo
		File fichero = new File("vgsales.csv");
		assertTrue(fichero.exists(), "El archivo CSV no existe en la ruta especificada.");

		// Llama al método leerCSV()
		csv.leerCSV();

		// Verifica que las líneas se hayan leído correctamente
		int actualLines = csv.getJuegos().size();
		System.out.println("Registros procesados: " + actualLines); // Depuración
		int expectedLines = 16599; // Ajusta este valor según el archivo real
		assertEquals(expectedLines, actualLines, "El número de registros leídos no coincide con el esperado.");
	}

	/**
	 * Verifica que las líneas CSV se conviertan correctamente en objetos `Juego`.
	 */
	@Test
	void devuelveObjeto() {
		// Configura las líneas CSV directamente como datos de prueba
		csv.setLineasCSV(List.of("1,Super Mario Bros,NES,1985,Platform,Nintendo,40.24,28.32,6.81,0.77,75.84",
				"2,Juego Ejemplo,PC,1984,Puzzle,Nintendo,23.20,2.26,4.22,0.58,30.26"));

		// Llama al método getJuegos()
		List<Juego> juegos = csv.getJuegos();

		// Comprobaciones
		assertNotNull(juegos, "La lista de juegos no debería ser nula");
		assertFalse(juegos.isEmpty(), "La lista de juegos no debería estar vacía");
		assertEquals(2, juegos.size(), "El tamaño de la lista de juegos no coincide con el esperado");

		// Verifica los datos de los objetos
		Juego juego1 = juegos.get(0);
		assertEquals(1, juego1.getRank());
		assertEquals("Super Mario Bros", juego1.getName());
		assertEquals("Nintendo", juego1.getPublisher());

		Juego juego2 = juegos.get(1);
		assertEquals(2, juego2.getRank());
		assertEquals("Juego Ejemplo", juego2.getName());
		assertEquals("PC", juego2.getPlatform().toString());
	}
}
