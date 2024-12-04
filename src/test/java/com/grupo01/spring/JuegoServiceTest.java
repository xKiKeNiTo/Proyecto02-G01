package com.grupo01.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.repository.JuegoDao;
import com.grupo01.spring.service.JuegoServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



/**
 * Clase de pruebas unitarias para la implementación del servicio
 * `JuegoServiceImpl`.
 *
 * Valida la lógica de negocio y la interacción con el repositorio.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */

@SpringBootTest
@AutoConfigureMockMvc
public class JuegoServiceTest {

	@Mock
	private JuegoDao juegoDao;

	@InjectMocks
	private JuegoServiceImpl juegoServiceImpl;

	private Juego juegoExistente;

	/**
	 * Configuración inicial para las pruebas.
	 */
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Inicializar un juego existente
		juegoExistente = new Juego(1, 1, "Wii Sports", Platform.Wii, 2006L, Genre.Sports, "Nintendo", 41.49, 29.02,
				3.77, 8.46, 82.74);
	}

	/**
	 * Verifica que los registros desde un CSV se guardan correctamente en lotes.
	 */
	@Test
	public void guardarCsvYVerificarRegistrosGuardadosCorrectamente() {
		List<Juego> juegos = new ArrayList<>();
		for (int i = 0; i < 250; i++) {
			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juegos.add(juego);
		}

		int totalSaved = juegoServiceImpl.saveCsv(juegos);

		assertEquals(juegos.size(), totalSaved,
				"El número total de registros guardados no coincide con la lista de entrada");

		int expectedBatchCalls = (int) Math.ceil(juegos.size() / 100.0);
		verify(juegoDao, times(expectedBatchCalls)).saveAll(anyList());
	}

	/**
	 * Verifica que un juego se guarda correctamente en la base de datos.
	 */
	@Test
	public void guardarJuegoYVerificarQueSeGuardaCorrectamente() {
		Juego juego = new Juego(0, 1, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 29.08, 3.58,
				6.81, 0.77, 40.24);

		when(juegoDao.save(any(Juego.class))).thenReturn(juego);

		Juego juegoGuardado = juegoServiceImpl.save(juego);

		verify(juegoDao, times(1)).save(juegoGuardado);

		assertNotNull(juegoGuardado);
		assertEquals("Super Mario Bros", juegoGuardado.getName());
		assertEquals(1985, juegoGuardado.getYear());
	}

	/**
	 * Verifica que todos los juegos se recuperan correctamente.
	 */
	@Test
	public void listarTodosLosJuegosYVerificarQueSeDevuelvenCorrectamente() {
		List<Juego> juegos = new ArrayList<>();

		for (int i = 0; i < 5; i++) {

			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juegos.add(juego);
		}

		when(juegoDao.findAll()).thenReturn(juegos);

		List<Juego> resultado = juegoServiceImpl.findAll();

		assertEquals(juegos.size(), resultado.size(), "La cantidad de juegos devuelta no coincide");
		assertEquals("Juego 1", resultado.get(0).getName());

		verify(juegoDao, times(1)).findAll();
	}

	/**
	 * Verifica que un juego existente se modifica correctamente.
	 */
	@Test
	public void modificarJuegoExistenteYVerificarActualizacionCorrecta() {
		when(juegoDao.existsById(1L)).thenReturn(true);
		when(juegoDao.save(juegoExistente)).thenReturn(juegoExistente);

		juegoExistente.setName("Wii Sports Updated");
		juegoExistente.setGlobalSales(85.00);

		Juego juegoModificado = juegoServiceImpl.save(juegoExistente);

		assertNotNull(juegoModificado);
		assertEquals("Wii Sports Updated", juegoModificado.getName());
		assertEquals(85.00, juegoModificado.getGlobalSales());
	}

	/**
	 * Verifica que se lanza una excepción al intentar modificar un juego
	 * inexistente.
	 */
	@Test
	public void lanzarExcepcionCuandoSeIntentaModificarJuegoInexistente() {
		when(juegoDao.existsById(3L)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> {
			Juego juegoInexistente = new Juego(3, 2, "Juego Inexistente", Platform.PS4, 2022, Genre.Action,
					"Desconocido", 1.0, 1.0, 1.0, 1.0, 4.0);
			juegoServiceImpl.save(juegoInexistente);
		});
	}


	/**
	 * Verifica que los juegos filtrados por género se recuperan correctamente.
	 */
	@Test
	public void listarJuegosFiltradosPorGeneroYVerificarResultadosCorrectos() {
		List<Juego> juegos = new ArrayList<>();
		juegos.add(
				new Juego(1, 1, "Juego Acción 1", Platform.PS4, 2021, Genre.Action, "Sony", 10.5, 8.3, 4.7, 2.1, 25.6));
		juegos.add(new Juego(2, 2, "Juego Acción 2", Platform.NES, 2020, Genre.Action, "Microsoft", 12.0, 9.0, 5.0, 3.0,
				29.0));

		// Configurar el mock para que devuelva la lista filtrada
		when(juegoDao.findByGenre(Genre.Action)).thenReturn(juegos);

		// Llamar al método del servicio
		List<Juego> resultado = juegoServiceImpl.findByGenre(Genre.Action);

		// Verificar los resultados
		assertEquals(2, resultado.size(), "La cantidad de juegos filtrados no coincide");
		assertEquals("Juego Acción 1", resultado.get(0).getName(), "El primer juego filtrado no coincide");
		assertEquals("Juego Acción 2", resultado.get(1).getName(), "El segundo juego filtrado no coincide");

		// Verificar que el repositorio fue llamado exactamente una vez
		verify(juegoDao, times(1)).findByGenre(Genre.Action);
	}



	@Test
	public void testConsolaYAnyoInvalido() throws Exception {
		// Configurar datos inválidos
		Platform consolaInvalida = Platform.valueOf("Plataforma invalida");
		long anyoInvalido = -100;

		// Configurar el comportamiento del mock para manejar datos inválidos
		when(juegoDao.deleteByConsoleAndBefore(any(Platform.class), eq(anyoInvalido)))
				.thenThrow(new IllegalArgumentException("Plataforma o año inválido"));

		// Realizar la petición al endpoint del controlador
		mockMvc.perform(get("/juegos").param("platform", consolaInvalida.toString()).param("year", String.valueOf(anyoInvalido)))
				.andExpect(status().isBadRequest()) // Verificar que el estatus HTTP es 400 (Bad Request)
				.andExpect(jsonPath("$.error").value("Plataforma o año inválido")); // Verificar el mensaje de error

		// Verificar que el servicio fue llamado con los parámetros correctos
		verify(juegoDao, times(1)).deleteByConsoleAndBefore(consolaInvalida, anyoInvalido);
	}

	
	/**
	 * Verifica que los juegos listados con ventas superiores a la media se recuperan correctamente.
	 */
	@Test
	public void listarJuegosVentasSuperioresYVerificarResultadosCorrectos() {
		List<Juego> juegos = new ArrayList<>();
		juegos.add(
				new Juego(1, 1, "Juego Acción 1", Platform.PS4, 2021, Genre.Action, "Sony", 10.5, 8.3, 4.7, 2.1, 25.6));
		juegos.add(new Juego(2, 2, "Juego Acción 2", Platform.NES, 2020, Genre.Action, "Microsoft", 12.0, 9.0, 5.0, 3.0,
				29.0));

		when(juegoDao.listarJuegosVentasSuperiores()).thenReturn(juegos);

		List<Juego> resultado = juegoServiceImpl.listarJuegosVentasSuperiores();

		assertEquals(2, resultado.size(), "La cantidad de juegos filtrados no coincide");
		assertEquals("Juego Acción 1", resultado.get(0).getName());
		
	    verify(juegoDao, times(1)).listarJuegosVentasSuperiores();

	}

}
