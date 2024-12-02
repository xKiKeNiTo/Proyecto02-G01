package com.grupo01.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

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

@WebMvcTest(JuegoController.class)
public class JuegoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CSV csv;

	@MockitoBean
	private JuegoServiceImpl juegoService;

	@Test
	public void devuelveJuegos() throws Exception {
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
	public void juegoInvalido() throws Exception {
		// Se crea un JSON con datos inválidos (nombre vacío, que se ha puesto como
		// obligatorio en el modelo Juego)
		String juegoInvalidoJson = "{\"name\": \"\", \"rank\": 1}";

	    // Realiza la solicitud POST
	    mockMvc.perform(post("/juegos")
	            .content(juegoInvalidoJson)
	            .contentType(MediaType.APPLICATION_JSON))
	        // Verifica que se devuelve 400 Bad Request
	        .andExpect(status().isBadRequest())
	        // Verifica que el mensaje de error incluye el detalle esperado
	        .andExpect(jsonPath("$.errors").exists()) // Se asegura de que hay un campo 'errors'
	        .andExpect(jsonPath("$.errors[0]").value("El nombre del juego no puede estar vacío"));  // Verifica que el primer mensaje de error es el esperado
	}
	
	@Test
	public void modificaJuegoEndpoint() throws Exception {
		// Datos simulados de juego existente a editar
		Juego juegoExistente = new Juego(1, 7,"Super Mario Bros",Platform.NES,1985,Genre.Platform,"Nintendo",40.24,28.32,6.81,0.77,75.84);
		
		// Datos simulados del juego actualizados
		Juego juegoEditado = new Juego(1, 7,"Super Mario Bros Editado",Platform.NES,1985,Genre.Platform,"Nintendo",40.24,28.32,7.00,0.77,75.84);
		
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
	    mockMvc.perform(post("/juegos/edit")
	            .content(juegoEditadoJson)
	            .contentType(MediaType.APPLICATION_JSON))
	    // Verifica el estado HTTP 200 OK
	    		.andExpect(status().isOk())
	    // Verifica los campos de respuesta si están cambiados
	    		.andExpect(jsonPath("$.id").value(1))
	            .andExpect(jsonPath("$.name").value("Super Mario Bros Editado"))
	            .andExpect(jsonPath("$.jpSales").value(7.00));
	    
	    // Comprueba que el método save fue llamado una vez con los datos actualizados
	    verify(juegoService, times(1)).save(any(Juego.class));
	}
					
}