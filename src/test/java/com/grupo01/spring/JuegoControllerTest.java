package com.grupo01.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.controller.JuegoController;
import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.service.JuegoServiceImpl;
import com.grupo01.spring.utils.CSV;

/**
 * Clase de pruebas unitarias para el controlador JuegoController.
 *
 * Valida los endpoints del controlador y su interacción con los servicios
 * simulados.
 * 
 * @author equipo
 * @version 1.0
 * @date 03/12/2024
 */
@WebMvcTest(JuegoController.class)
public class JuegoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CSV csv;

	@MockitoBean
	private JuegoServiceImpl juegoService;

	/**
	 * Prueba que los juegos se guardan correctamente desde el archivo CSV.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeGuardarJuegosDesdeCSVYDevolverMensajeExitoso() throws Exception {
		List<Juego> juegos = Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));

		when(csv.getJuegos()).thenReturn(juegos);
		when(juegoService.saveCsv(juegos)).thenReturn(juegos.size());

		mockMvc.perform(post("/juegos/save-csv")).andExpect(status().isOk())
				.andExpect(content().string("Se han guardado 2 juegos desde el archivo CSV."));

		verify(csv, times(1)).leerCSV();
		verify(juegoService, times(1)).saveCsv(juegos);
	}

	/**
	 * Prueba que devuelve un error 400 cuando los datos de entrada son inválidos.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeDevolverBadRequestCuandoJuegoTieneCamposInvalidos() throws Exception {
		String juegoInvalidoJson = "{\"name\": \"\", \"rank\": 1, \"platform\": null}";

		mockMvc.perform(post("/juegos").content(juegoInvalidoJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists())
				.andExpect(
						jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El nombre del juego no puede estar vacío")))
				.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La plataforma no puede estar vacía")));
	}

	/**
	 * Prueba que un juego existente se edita correctamente.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeModificarJuegoExistenteYDevolverDatosActualizados() throws Exception {
		Juego juegoEditado = new Juego(1, 7, "Super Mario Bros Editado", Platform.NES, 1985, Genre.Platform, "Nintendo",
				40.24, 28.32, 7.00, 0.77, 75.84);

		when(juegoService.save(any(Juego.class))).thenReturn(juegoEditado);

		String juegoEditadoJson = """
				{
				    "id": 1,
				    "rank": 7,
				    "name": "Super Mario Bros Editado",
				    "platform": "NES",
				    "year": 1985,
				    "genre": "Platform",
				    "publisher": "Nintendo",
				    "naSales": 40.24,
				    "euSales": 28.32,
				    "jpSales": 7.00,
				    "otherSales": 0.77,
				    "globalSales": 75.84
				}
				""";

		mockMvc.perform(post("/juegos/edit").content(juegoEditadoJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Super Mario Bros Editado"))
				.andExpect(jsonPath("$.jpSales").value(7.00));

		verify(juegoService, times(1)).save(any(Juego.class));
	}

	/**
	 * Prueba que el endpoint para buscar juegos por género llama correctamente al
	 * servicio.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void llamoAlEndpointDeGeneroYVerificoLlamadaAlServicio() throws Exception {
		List<Juego> juegos = new ArrayList<>();
		juegos.add(
				new Juego(1, 1, "Juego Acción 1", Platform.PS4, 2021, Genre.Action, "Sony", 10.5, 8.3, 4.7, 2.1, 25.6));
		juegos.add(new Juego(2, 2, "Juego Acción 2", Platform.NES, 2020, Genre.Action, "Microsoft", 12.0, 9.0, 5.0, 3.0,
				29.0));

		when(juegoService.findByGenre(Genre.Action)).thenReturn(juegos);

		mockMvc.perform(get("/juegos/genero/Action").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(juegoService, times(1)).findByGenre(Genre.Action);
	}

	/**
	 * Prueba que el endpoint para listar juegos del siglo XX funciona
	 * correctamente.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void llamoEndpointVerificoLlamadaServicioSiglo() throws Exception {
		List<Juego> juegos = Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));

		when(juegoService.listarPorSiglo()).thenReturn(juegos);

		mockMvc.perform(get("/juegos/por-siglo")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

		verify(juegoService, times(1)).listarPorSiglo();
	}

	/**
	 * Prueba que el endpoint para listar juegos por consola funciona correctamente.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void llamoEndpointVerificoLlamadaServicioConsola() throws Exception {
		Platform consola = Platform.X360;
		List<Juego> juegosMock = Arrays.asList(
				new Juego(1, 1, "Call of Duty Black Ops", Platform.X360, 2010, Genre.Shooter, "Treyarch", 30.56, 15.71,
						7.61, 2.71, 56.59),
				new Juego(2, 2, "Fable II", Platform.X360, 2008, Genre.Role_Playing, "Lionhead Studios", 15.14, 8.74,
						4.52, 1.1, 29.50));

		when(juegoService.listarPorConsola(consola)).thenReturn(juegosMock);

		mockMvc.perform(get("/juegos/consola/{consola}", consola).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(juegoService).listarPorConsola(consola);
	}
	
	/**
	 * Prueba que el endpoint para listar juegos con ventas mayores a la media 
	 * funciona correctamente.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void llamoEndpointVerificoLlamadaServicioVentas() throws Exception {
		List<Juego> juegos = Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));

		when(juegoService.listarJuegosVentasSuperiores()).thenReturn(juegos);

		mockMvc.perform(get("/juegos/ventas-superiores")).andExpect(status().isOk());

		verify(juegoService, times(1)).listarJuegosVentasSuperiores();
	}

}
