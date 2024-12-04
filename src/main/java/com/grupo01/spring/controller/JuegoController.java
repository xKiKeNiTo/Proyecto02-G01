package com.grupo01.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo01.spring.controller.error.CustomException;
import com.grupo01.spring.controller.error.GlobalExceptionHandler;
import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.service.JuegoServiceImpl;
import com.grupo01.spring.utils.CSV;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los juegos.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
@RestController
@RequestMapping("/juegos")
public class JuegoController {

	@Autowired
	private CSV csv;

	@Autowired
	private JuegoServiceImpl juegoService;

	/**
	 * Lee los datos del archivo CSV y los guarda en la base de datos.
	 *
	 * @return ResponseEntity con el número de juegos guardados o mensaje de error.
	 */
	@PostMapping("/save-csv")
	public ResponseEntity<String> saveCsv() {
		try {
			csv.leerCSV();
			List<Juego> juegos = csv.getJuegos();

			int juegosGuardados = juegoService.saveCsv(juegos);
			return ResponseEntity.ok("Se han guardado " + juegosGuardados + " juegos desde el archivo CSV.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al guardar los datos del CSV: " + e.getMessage());
		}
	}

	/**
	 * Obtiene todos los juegos almacenados.
	 *
	 * @return Lista de juegos con un mensaje descriptivo.
	 */
	@GetMapping
	public ResponseEntity<RespuestaApi<List<Juego>>> obtenerTodos() {
		List<Juego> juegos = juegoService.findAll();
		if (juegos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new RespuestaApi<>("No se encontraron juegos", 204, null));
		}
		return ResponseEntity.ok(new RespuestaApi<>("Lista de juegos encontrada", 200, juegos));
	}

	/**
	 * Guarda un nuevo juego en la base de datos.
	 *
	 * @param nuevoJuego El juego que se desea guardar.
	 * @return ResponseEntity con el juego guardado o mensaje de error.
	 */
	@PostMapping
	public ResponseEntity<?> guardarJuego(@RequestBody @Valid Juego nuevoJuego) {
		try {
			Juego juegoGuardado = juegoService.save(nuevoJuego);
			return ResponseEntity.ok(juegoGuardado);
		} catch (IllegalArgumentException e) {
			// Manejo específico para valores inválidos del Enum
			CustomException ce = new CustomException("Error: Valor inválido para plataforma o género", 204);
			GlobalExceptionHandler geh = new GlobalExceptionHandler();
			//Antiguo return:
			//return ResponseEntity.badRequest().body("Error: Valor inválido para plataforma o género");
			return geh.handleCustomException(ce);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error: Ocurrió un problema al guardar el juego.");
		}
	}

	/**
	 * Edita un juego existente.
	 *
	 * @param juegoExistente Los datos actualizados del juego.
	 * @return ResponseEntity con el juego actualizado.
	 */
	@PostMapping("/edit")
	public ResponseEntity<Juego> editarJuego(@RequestBody @Valid Juego juegoExistente) {
		Juego juegoEditado = juegoService.save(juegoExistente);
		return ResponseEntity.ok(juegoEditado);
	}

	/**
	 * Obtiene los juegos clasificados por siglo.
	 *
	 * @return Lista de juegos clasificados por siglo.
	 */
	@GetMapping("/por-siglo")
	public List<Juego> listarPorSiglo() {
		return juegoService.listarPorSiglo();
	}

	/**
	 * Elimina un juego por su ID y devuelve información del juego eliminado.
	 *
	 * @param id del juego a eliminar.
	 * @return ResponseEntity con detalles del juego eliminado o mensaje de error.
	 */
	@DeleteMapping("deleteById/{id}")
	public ResponseEntity<RespuestaApi<Juego>> eliminarPorId(@PathVariable long id) {
		Juego juegoEliminado = juegoService.deleteById(id);
		if (juegoEliminado != null) {
			return ResponseEntity.ok(new RespuestaApi<>("El juego ha sido eliminado con éxito.", 200, juegoEliminado));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new RespuestaApi<>("El juego no existe.", 404, null));
		}
	}

	/**
	 * Obtiene los juegos filtrados por género.
	 *
	 * @param genre El género de los juegos a filtrar.
	 * @return Lista de juegos del género solicitado.
	 */
	@GetMapping("/genero/{genre}")
	public ResponseEntity<?> buscarPorGenero(@PathVariable Genre genre) {
		List<Juego> juegos = juegoService.findByGenre(genre);
		if (juegos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body("No se encontraron juegos para el género especificado.");
		}
		return ResponseEntity.ok(juegos);
	}

	/**
	 * Obtiene los juegos filtrados por plataforma.
	 *
	 * @param plataforma La plataforma de los juegos a filtrar.
	 * @return Lista de juegos de la plataforma solicitada.
	 */
	@GetMapping("/consola/{plataforma}")
	public ResponseEntity<List<Juego>> listarPorPlataforma(@PathVariable Platform plataforma) {
		List<Juego> juegos = juegoService.listarPorConsola(plataforma);
		if (juegos == null || juegos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(juegos);
	}

	@GetMapping("/year/{year}")
	public ResponseEntity<RespuestaApi<List<Juego>>> findByYear(@PathVariable long year) {
	    List<Juego> juegos = juegoService.findByYear(year);
	    if (juegos.isEmpty()) {
	        return ResponseEntity.status(404)
	                .body(new RespuestaApi<>("No se encontraron juegos para ese año", 404, null));
	    }

	    return ResponseEntity.status(200).body(new RespuestaApi<>("Lista de juegos encontrada", 200, juegos));
	}

	/**
	 * Obtiene los juegos con ventas superiores a la media.
	 *
	 * @return Lista de juegos con ventas globales con valor superior a la media.
	 */
	@GetMapping("/ventas-superiores")
	public List<Juego> listarJuegosVentasSuperiores() {
		return juegoService.listarJuegosVentasSuperiores();
	}


	/**
	 * Eliminar por consola y antes de año
	 *
	 * @param plataform plataform
	 * @param year year
	 * @return {@link ResponseEntity}
	 * @see ResponseEntity
	 * @see RespuestaApi
	 */
	@DeleteMapping("/{plataform}/{year}")
	public ResponseEntity<RespuestaApi<List<Juego>>> deleteByConsoleAndBefore(Platform plataform,long year){
		List<Juego> juegosEliminados = juegoService.deleteByConsoleAndBefore(plataform,year);
		if (!juegosEliminados.isEmpty()) {
			return ResponseEntity.ok(new RespuestaApi<>("Lista de juegos eliminada", 200, juegosEliminados));
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new RespuestaApi<>("No se encontraron juegos", 204, null));
		}
	}



}
