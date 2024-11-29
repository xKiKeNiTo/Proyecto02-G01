package com.grupo01.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.repository.JuegoDao;
import com.grupo01.spring.utils.CSV;

@Service
public class JuegoServiceImpl implements JuegoService {

	// private static final Logger log =
	// LoggerFactory.getLogger(JuegoController.class);

	@Autowired
	private JuegoDao juegoDao;

	@Autowired
	private CSV csv;

	// Para Listar Todos
	@Override
	public List<Juego> findAll() {
		return juegoDao.findAll();
	}

	public int saveCsv(List<Juego> juegos) {
		try {
			// Leer el archivo CSV y obtener la lista de juegos
			csv.leerCSV();
			// Guardar todos los juegos en la base de datos
			juegoDao.saveAll(juegos);
			return juegos.size(); // Retornar el número total de juegos guardados
		} catch (Exception e) {
			throw new RuntimeException("Error al guardar datos desde el CSV: " + e.getMessage(), e);
		}
	}

}
