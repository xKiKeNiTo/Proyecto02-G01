package com.grupo01.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.service.JuegoServiceImpl;
import com.grupo01.spring.utils.CSV;

import jakarta.validation.Valid;

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

	@GetMapping
	public ResponseEntity<RespuestaApi<List<Juego>>> findAll() {
		List<Juego> juegos = juegoService.findAll();
		if (juegos.isEmpty()) {
			return ResponseEntity.status(204).body(new RespuestaApi<>("No se encontraron juegos", 204, null));
		}

		return ResponseEntity.status(200).body(new RespuestaApi<>("Lista de juegos encontrada", 200, juegos));
	}

	@PostMapping
	public ResponseEntity<?> saveJuego(@RequestBody @Valid Juego nuevoJuego) {
		try {
			Juego juegoGuardado = juegoService.save(nuevoJuego);
			return ResponseEntity.ok(juegoGuardado);
		} catch (IllegalArgumentException e) {
			// Manejo específico para valores inválidos del Enum
			return ResponseEntity.badRequest().body("Error: Valor inválido para plataforma o género.");

		} catch (Exception e) {
			// Manejo general de otras excepciones
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error: Ocurrió un problema al guardar el juego.");
		}
	}

	/**
	 * Edita un juego existente.
	 *
	 * @param juegoExistente el juego con los datos actualizados
	 * @return ResponseEntity con el juego editado
	 */
	@PostMapping("/edit")
	public ResponseEntity<Juego> editJuego(@RequestBody @Valid Juego juegoExistente) {
		Juego juegoEditado = juegoService.save(juegoExistente);
		return ResponseEntity.ok(juegoEditado);
	}

	@GetMapping("/por-siglo")
	public List<Juego> listarPorSiglo() {
		return juegoService.listarPorSiglo();

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<RespuestaApi<Juego>> deleteById(@PathVariable long id) {
		Juego juegoEliminado = juegoService.deleteById(id);
		if (juegoEliminado != null) {
			return ResponseEntity.status(200)
					.body(new RespuestaApi<>("El juego ha sido eliminado con éxito.", 200, juegoEliminado));
		} else {
			return ResponseEntity.status(404).body(new RespuestaApi<>("El juego no existe.", 404, null));
		}
	}

	@GetMapping("/genero/{genre}")
	public ResponseEntity<?> findByGenre(@PathVariable Genre genre) {
		List<Juego> juegos = juegoService.findByGenre(genre);

		if (juegos.isEmpty()) {
			return ResponseEntity.status(204).body("No se encontraron juegos para el género especificado.");
		}
		return ResponseEntity.ok(juegos);
	}

	@GetMapping("/consola/{plataforma}")
	public ResponseEntity<List<Juego>> listarPorConsola(@PathVariable Platform plataforma) {
		List<Juego> juegos = juegoService.listarPorConsola(plataforma);

		if (juegos == null || juegos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(juegos);
	}

	@GetMapping("/{year}")
	public ResponseEntity<RespuestaApi<List<Juego>>> findByYear(@PathVariable long year) {
		List<Juego> juegos = juegoService.findByYear(year);
		if (juegos.isEmpty()) {
			return ResponseEntity.status(204).body(new RespuestaApi<>("No se encontraron juegos para ese año", 204, null));
		}

		return ResponseEntity.status(200).body(new RespuestaApi<>("Lista de juegos encontrada", 200, juegos));
	}
}
