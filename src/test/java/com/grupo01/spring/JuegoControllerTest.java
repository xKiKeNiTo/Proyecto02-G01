package com.grupo01.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	@MockitoBean
	private CSV csv;

	@MockitoBean
	private JuegoServiceImpl juegoService;

	@Test
	public void debeGuardarJuegosDesdeCSVYDevolverMensajeExitoso() throws Exception {
		// Preparación de datos simulados
		List<Juego> juegos = Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));

		when(csv.getJuegos()).thenReturn(juegos); // Simula que CSV.getJuegos devuelve la lista
		when(juegoService.saveCsv(juegos)).thenReturn(juegos.size()); // Simula guardado exitoso

		mockMvc.perform(post("/juegos/save-csv")) // Solicitud POST al endpoint
				.andExpect(status().isOk()) // Comprueba que el estado HTTP es 200 OK
				.andExpect(content().string("Se han guardado 2 juegos desde el archivo CSV.")); // Verifica el mensaje
																								// en la respuesta

		verify(csv, times(1)).leerCSV(); // Asegura que se llamó al método leerCSV()
		verify(juegoService, times(1)).saveCsv(juegos); // Asegura que se llamó al método saveCsv()
	}

	@Test
	public void debeDevolverBadRequestCuandoJuegoTieneCamposInvalidos() throws Exception {
		// Crear un JSON con múltiples campos inválidos (nombre vacío, plataforma nula)
		String juegoInvalidoJson = "{\"name\": \"\", \"rank\": 1, \"platform\": null}";

		// Realiza la solicitud POST
		mockMvc.perform(post("/juegos").content(juegoInvalidoJson).contentType(MediaType.APPLICATION_JSON))
				// Verifica que se devuelve 400 Bad Request
				.andExpect(status().isBadRequest())
				// Verifica que hay un campo 'errors' en la respuesta
				.andExpect(jsonPath("$.errors").exists())
				// Verifica que el mensaje de error incluye "El nombre del juego no puede estar
				// vacío"
				.andExpect(
						jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El nombre del juego no puede estar vacío")))
				// Verifica que el mensaje de error incluye "La plataforma no puede estar vacía"
				.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La plataforma no puede estar vacía")));
	}

	@Test
	public void debeModificarJuegoExistenteYDevolverDatosActualizados() throws Exception {
		// Datos simulados de juego existente a editar
		Juego juegoExistente = new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo",
				40.24, 28.32, 6.81, 0.77, 75.84);

		// Datos simulados del juego actualizados
		Juego juegoEditado = new Juego(1, 7, "Super Mario Bros Editado", Platform.NES, 1985, Genre.Platform, "Nintendo",
				40.24, 28.32, 7.00, 0.77, 75.84);

		// Simula el comportamiento del servicio
		when(juegoService.save(any(Juego.class))).thenReturn(juegoEditado);

		// JSON del juego actualizado
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
		// Realiza la solicitud POST al endpoint /juegos/edit
		mockMvc.perform(post("/juegos/edit").content(juegoEditadoJson).contentType(MediaType.APPLICATION_JSON))
				// Verifica el estado HTTP 200 OK
				.andExpect(status().isOk())
				// Verifica los campos de respuesta si están cambiados
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Super Mario Bros Editado"))
				.andExpect(jsonPath("$.jpSales").value(7.00));

		// Comprueba que el método save fue llamado una vez con los datos actualizados
		verify(juegoService, times(1)).save(any(Juego.class));
	}

	@Test
	public void llamoAlEndpointDeGeneroYVerificoLlamadaAlServicio() throws Exception {
		// Preparar datos simulados
		List<Juego> juegos = new ArrayList<>();
		juegos.add(
				new Juego(1, 1, "Juego Acción 1", Platform.PS4, 2021, Genre.Action, "Sony", 10.5, 8.3, 4.7, 2.1, 25.6));
		juegos.add(new Juego(2, 2, "Juego Acción 2", Platform.NES, 2020, Genre.Action, "Microsoft", 12.0, 9.0, 5.0, 3.0,
				29.0));

		// Configurar el mock del servicio
		when(juegoService.findByGenre(Genre.Action)).thenReturn(juegos);

		// Realizar una solicitud GET al endpoint del controlador
		mockMvc.perform(get("/juegos/genero/Action").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()); // Verificar que la respuesta tiene el estado HTTP 200

		// Verificar que el servicio fue llamado con el género correcto
		verify(juegoService, times(1)).findByGenre(Genre.Action);
	}

	@Test
	public void llamoEndpointVerificoLlamadaServicioSiglo() throws Exception {
		// Preparación de datos simulados
		List<Juego> juegos = Arrays.asList(
				new Juego(1, 7, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 40.24, 28.32, 6.81,
						0.77, 75.84),
				new Juego(2, 23, "Juego Ejemplo", Platform.PC, 1984, Genre.Puzzle, "Nintendo", 23.20, 2.26, 4.22, 0.58,
						30.26));

		// Configuración del mock
		when(juegoService.listarPorSiglo()).thenReturn(juegos);

		// Llamada al endpoint y verificar el comportamiento
		mockMvc.perform(get("/juegos/por-siglo")).andExpect(status().isOk()) // Status 200
				.andExpect(jsonPath("$.length()").value(2)); // Verificar que hay 2 elementos

		// Verificar que el servicio fue llamado una vez
		verify(juegoService, times(1)).listarPorSiglo();
	}

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


	@Test
	public void llamoEndpointVerificoLlamadaServicioYear() throws Exception {
		// Año para filtrar juegos
		long year = 2001;

		List<Juego> juegos = new ArrayList<>();
		for (int i = 0; i < 5; i++) { // Crear 5 registros simulados
			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juego.setYear(2001);
			juegos.add(juego);
		}

		// Simular el comportamiento del servicio
		when(juegoService.findByYear(year)).thenReturn(juegos);

		// Realizar una solicitud GET al endpoint
		mockMvc.perform(get("/juegos/year")
						.param("year", String.valueOf(year)) // Añadir parámetro al request
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Verificar que el estado HTTP es 200 OK
				.andExpect(jsonPath("$.length()").value(juegos.size()))// Verificar que la respuesta tiene la cantidad correcta de juegos
				.andExpect(jsonPath("$[0].name").value("Juego 1"));


		// Verificar que el método findByYear del servicio se llamó con el parámetro correcto
		verify(juegoService, times(1)).findByYear(year);
	}

}