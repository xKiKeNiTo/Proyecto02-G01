package com.grupo01.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.controller.JuegoController;
import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.service.JuegoServiceImpl;
import com.grupo01.spring.utils.CSV;

@WebMvcTest(JuegoController.class)
public class JuegoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CSV csv;

	@MockBean
	private JuegoServiceImpl juegoService;

	private List<Juego> getMockJuegos() {
		return Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));
	}

	@Test
	public void debeGuardarJuegosDesdeCSVYDevolverMensajeExitoso() throws Exception {
		List<Juego> juegos = getMockJuegos();

		when(csv.getJuegos()).thenReturn(juegos);
		when(juegoService.saveCsv(juegos)).thenReturn(juegos.size());

		mockMvc.perform(post("/juegos/save-csv")).andExpect(status().isOk())
				.andExpect(content().string("Se han guardado 2 juegos desde el archivo CSV."));

		verify(csv, times(1)).leerCSV();
		verify(juegoService, times(1)).saveCsv(juegos);
	}

	@Test
	public void debeDevolverBadRequestCuandoJuegoTieneCamposInvalidos() throws Exception {
		String juegoInvalidoJson = "{\"name\": \"\", \"rank\": 1, \"platform\": null}";

		mockMvc.perform(post("/juegos").content(juegoInvalidoJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors").exists())
				.andExpect(
						jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El nombre del juego no puede estar vacío")))
				.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La plataforma no puede estar vacía")));
	}

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
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Super Mario Bros Editado"))
				.andExpect(jsonPath("$.jpSales").value(7.00));

		verify(juegoService, times(1)).save(any(Juego.class));
	}

	@Test
	public void llamoAlEndpointDeGeneroYVerificoLlamadaAlServicio() throws Exception {
		List<Juego> juegos = getMockJuegos();

		when(juegoService.findByGenre(Genre.Action)).thenReturn(juegos);

		mockMvc.perform(get("/juegos/genero/Action").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(juegos.size()));

		verify(juegoService, times(1)).findByGenre(Genre.Action);
	}
}
