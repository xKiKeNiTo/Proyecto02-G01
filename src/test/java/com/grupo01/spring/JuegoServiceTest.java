package com.grupo01.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.repository.JuegoDao;
import com.grupo01.spring.service.JuegoServiceImpl;

@SpringBootTest
public class JuegoServiceTest {

	@Mock
	private JuegoDao juegoDao;

	private Juego juegoExistente;

	@Test
	public void testDevuelveJuegos() {
		List<Juego> juegos = new ArrayList<>();
		for (int i = 0; i < 5; i++) { // Crear 250 registros simulados
			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juegos.add(juego);
		}

		// Configurar el mock para que devuelva la lista simulada cuando se llame a
		// findAll
		when(juegoDao.findAll()).thenReturn(juegos);

		// Llamo al metodo del servicio
		List<Juego> resultado = juegoServiceImpl.findAll();

		// Verificar resultados
		assertEquals(juegos.size(), resultado.size(), "La cantidad de juegos devuelta no coincide");
		assertEquals("Juego 1", resultado.getFirst().getName(), "El primer juego no coincide");

		// Verificar que el repositorio se llamó la cantidad correcta de veces
		int expectedBatchCalls = (int) Math.ceil(juegos.size() / 100.0);
		verify(juegoDao, times(expectedBatchCalls)).saveAll(anyList());
	}

	@Test
	public void testSaveCsv() {
		List<Juego> juegos = new ArrayList<>();
		for (int i = 0; i < 250; i++) { // Crear 250 registros simulados
			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juegos.add(juego);
		}
	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Inicializar un juego existente
		juegoExistente = new Juego(1, 1, "Wii Sports", Platform.Wii, 2006L, Genre.Sports, "Nintendo", 41.49, 29.02,
				3.77, 8.46, 82.74);
	}

	@InjectMocks
	private JuegoServiceImpl juegoServiceImpl;

	@Test
	public void guardaCsv() {
		List<Juego> juegos = new ArrayList<>();
		for (int i = 0; i < 250; i++) { // Crear 250 registros simulados
			Juego juego = new Juego();
			juego.setRank(i + 1);
			juego.setName("Juego " + (i + 1));
			juegos.add(juego);
		}

		int totalSaved = juegoServiceImpl.saveCsv(juegos);

		// Verificar que se guardan todos los registros
		assertEquals(juegos.size(), totalSaved,
				"El número total de registros guardados no coincide con la lista de entrada");

		// Verificar que el repositorio se llamó la cantidad correcta de veces
		int expectedBatchCalls = (int) Math.ceil(juegos.size() / 100.0);
		verify(juegoDao, times(expectedBatchCalls)).saveAll(anyList());
	}

	@Test
	public void guardaJuego() {
		// Datos de entrada
		Juego juego = new Juego(0, 1, "Super Mario Bros", Platform.NES, 1985, Genre.Platform, "Nintendo", 29.08, 3.58,
				6.81, 0.77, 40.24);

		// Simular el comportamiento del mock
		when(juegoDao.save(any(Juego.class))).thenReturn(juego);

		// Ejecutar el método a probar
		Juego juegoGuardado = juegoServiceImpl.save(juego);

		// Verificar que el DAO fue llamado con el juego correcto
		verify(juegoDao, times(1)).save(juegoGuardado);

		// Afirmaciones
		assertNotNull(juegoGuardado);
		assertEquals("Super Mario Bros", juegoGuardado.getName());
		assertEquals(1985, juegoGuardado.getYear());
		assertEquals(Platform.NES, juegoGuardado.getPlatform());
		assertEquals(Genre.Platform, juegoGuardado.getGenre());
	}

	@Test
	public void modificaJuego() {
		// Mockear el comportamiento del repositorio
		when(juegoDao.existsById(1)).thenReturn(true);
		when(juegoDao.save(juegoExistente)).thenReturn(juegoExistente);

		// Modificar el juego
		juegoExistente.setName("Wii Sports Updated");
		juegoExistente.setGlobalSales(85.00);

		// Llamar al método del servicio
		Juego juegoModificado = juegoServiceImpl.save(juegoExistente);

		// Verificar el resultado
		assertNotNull(juegoModificado);
		assertEquals("Wii Sports Updated", juegoModificado.getName());
		assertEquals(85.00, juegoModificado.getGlobalSales());
	}

	@Test
	public void modificaInexistente() {
		// Mockear el comportamiento del repositorio para ID inexistente
		when(juegoDao.existsById(3)).thenReturn(false);

		// Verificar que se lanza la excepción al intentar modificar un juego
		// inexistente
		assertThrows(RuntimeException.class, () -> {
			Juego juegoInexistente = new Juego(3, 2, "Juego Inexistente", Platform.PS4, 2022, Genre.Action,
					"Desconocido", 1.0, 1.0, 1.0, 1.0, 4.0);
			juegoServiceImpl.save(juegoInexistente);
		});
	}
}
