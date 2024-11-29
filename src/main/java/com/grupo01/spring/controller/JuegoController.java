package com.grupo01.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.service.JuegoServiceImpl;
import com.grupo01.spring.utils.CSV;

@RestController
@RequestMapping("/juegos")
public class JuegoController {

	@Autowired
	private CSV csv; // Clase para manejar el CSV

	@Autowired
	private JuegoServiceImpl juegoService;

	/**
	 * Lee los datos del CSV y los guarda en la base de datos.
	 *
	 * @return ResponseEntity con el número de juegos guardados o un mensaje de
	 *         error.
	 */
	@PostMapping("/save-csv")
	public ResponseEntity<String> saveCsv() {
		try {
			// Leer el CSV y obtener la lista de juegos
			csv.leerCSV();
			List<Juego> juegos = csv.getJuegos();

			// Llamar a JuegoServiceImpl para guardar los datos
			int juegosGuardados = juegoService.saveCsv(juegos);

			return ResponseEntity.ok("Se han guardado " + juegosGuardados + " juegos desde el archivo CSV.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al guardar los datos del CSV: " + e.getMessage());
		}
	}

}
