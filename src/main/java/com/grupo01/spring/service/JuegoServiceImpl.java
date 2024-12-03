package com.grupo01.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo01.spring.controller.error.CustomException;
import com.grupo01.spring.model.Genre;
import com.grupo01.spring.model.Juego;
import com.grupo01.spring.model.Platform;
import com.grupo01.spring.repository.JuegoDao;

/**
 * Implementación del servicio para la gestión de juegos.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad
 * Juego.
 *
 * @version 1.0
 * @author Equipo
 * @date 03/12/2024
 */
@Service
public class JuegoServiceImpl implements JuegoService {

	@Autowired
	private JuegoDao juegoDao;

	/**
	 * Recupera todos los juegos de la base de datos.
	 *
	 * @return Lista de todos los juegos.
	 */
	@Override
	public List<Juego> findAll() {
		return juegoDao.findAll();
	}

	/**
	 * Guarda una lista de juegos en la base de datos en lotes.
	 *
	 * @param juegos Lista de juegos a guardar.
	 * @return El número total de registros guardados.
	 */
	@Transactional
	public int saveCsv(List<Juego> juegos) {
		int batchSize = 100; // Tamaño del lote
		int totalSaved = 0;

		try {
			for (int i = 0; i < juegos.size(); i += batchSize) {
				List<Juego> batch = juegos.subList(i, Math.min(i + batchSize, juegos.size()));
				juegoDao.saveAll(batch);
				totalSaved += batch.size();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al guardar datos desde el CSV: " + e.getMessage(), e);
		}

		return totalSaved;
	}

	/**
	 * Guarda o actualiza un juego en la base de datos.
	 *
	 * @param juego Objeto Juego a guardar.
	 * @return El juego guardado (incluyendo ID generado si es nuevo).
	 */
	@Transactional
	public Juego save(Juego juego) {
		if (juego.getId() != 0) {
			if (!juegoDao.existsById(juego.getId())) {
				throw new CustomException("Juego con ID " + juego.getId() + " no encontrado para modificar", 404);
			}
		}
		return juegoDao.save(juego);
	}

	/**
	 * Elimina un juego de la base de datos dado su ID.
	 *
	 * @param id ID del juego a eliminar.
	 * @return El juego eliminado.
	 * @throws RuntimeException Si el juego no se encuentra.
	 */
	@Override
	@Transactional
	public Juego deleteById(long id) {
		Optional<Juego> juegoOptional = juegoDao.findById(id);
		if (juegoOptional.isPresent()) {
			Juego juegoEliminado = juegoOptional.get();
			juegoDao.delete(juegoEliminado);
			return juegoEliminado;
		} else {
			throw new RuntimeException("Juego con ID " + id + " no encontrado para eliminar.");
		}
	}

	/**
	 * Recupera una lista de juegos según su género.
	 *
	 * @param genre Género de los juegos a buscar.
	 * @return Lista de juegos del género especificado.
	 */
	@Override
	public List<Juego> findByGenre(Genre genre) {
		return juegoDao.findByGenre(genre);
	}

	/**
	 * Recupera una lista de juegos publicados en el siglo XX.
	 *
	 * @return Lista de juegos del siglo XX.
	 */
	@Override
	public List<Juego> listarPorSiglo() {
		return juegoDao.listarPorSiglo();
	}

	/**
	 * Recupera una lista de juegos publicados en una plataforma específica.
	 *
	 * @param plataforma Plataforma de la que queremos obtener juegos.
	 * @return Lista de juegos publicados en la plataforma especificada.
	 */
	@Override
	public List<Juego> listarPorConsola(Platform plataforma) {
		return juegoDao.listarPorConsola(plataforma);
	}

	@Override
	public List<Juego> findByYear(long year) {
		return juegoDao.findByYear(year);
	}

	public List<Juego> deleteByConsoleAndBefore(Platform plataform,long year){
		return juegoDao.deleteByConsoleAndBefore(plataform,year);
	}
}
