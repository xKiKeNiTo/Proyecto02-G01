package com.grupo01.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.repository.JuegoDao;

@Service
public class JuegoServiceImpl implements JuegoService {

	@Autowired
	private JuegoDao juegoDao;

	// Para listar todos los juegos
	public List<Juego> findAll() {
		return juegoDao.findAll();
	}

	public int saveCsv(List<Juego> juegos) {
		int batchSize = 100; // Tamaño para insertar en la base de datos por partes
		int totalSaved = 0;

		try {
			for (int i = 0; i < juegos.size(); i += batchSize) {
				// Dividir la lista en lotes
				List<Juego> batch = juegos.subList(i, Math.min(i + batchSize, juegos.size()));
				juegoDao.saveAll(batch); // Guardar en la base de datos
				totalSaved += batch.size();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al guardar datos desde el CSV: " + e.getMessage(), e);
		}

		return totalSaved; // Retornar el total de registros guardados
	}

	public Juego save(Juego juego) {
		if (juego.getId() != 0) {
			if (!juegoDao.existsById(juego.getId())) {
				throw new RuntimeException("Juego con ID " + juego.getId() + " no encontrado para modificar.");
			}
		}
		return juegoDao.save(juego);
	}


	@Override
	public Juego deleteById(long id) {
		Optional<Juego> juegoOptional = juegoDao.findById(id);
		if (juegoOptional.isPresent()) {
			Juego juegoEliminado = juegoOptional.get();
			juegoDao.delete(juegoEliminado);
			return juegoEliminado;
		} else {
			return null;
		}
	}

	public List<Juego> findByGenre(Genre genre) {
		return juegoDao.findByGenre(genre);
	}
	
	public List<Juego> listarPorSiglo() {
		return juegoDao.listarPorSiglo();
	}
	
	public List<Juego> listarPorConsola(String plataforma) {
		return juegoDao.listarPorConsola(plataforma);
	}

}
