package com.grupo01.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.repository.JuegoDao;

@Service
public class JuegoServiceImpl implements JuegoService {

	@Autowired
	private JuegoDao juegoDao;

	@Override
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
		return juegoDao.save(juego);
	}

	@Override
	public List<Juego> listarPorSiglo() {
		return juegoDao.listarPorSiglo();
	}

}
