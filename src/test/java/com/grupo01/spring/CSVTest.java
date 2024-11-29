package com.grupo01.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.grupo01.spring.utils.CSV;

public class CSVTest {

	private CSV csv;

	@BeforeEach
	void setUp() {
		// Inicializa la instancia de CSV
		csv = new CSV();
	}

	@Test
	void leeCSV() throws IOException {
		// Verifica que el archivo existe antes de leerlo
		File fichero = new File("vgsales.csv");
		assertFalse(!fichero.exists(), "El archivo CSV no existe en la ruta especificada.");

		// Llama al método leerCSV()
		csv.leerCSV();

		// Verifica que las líneas se hayan leído correctamente
		int expectedLines = 16599; // Cambia este valor al número esperado de registros (excluyendo el encabezado)
		assertEquals(expectedLines, csv.getJuegos().size(),
				"El número de registros leídos no coincide con el esperado.");
	}
}
