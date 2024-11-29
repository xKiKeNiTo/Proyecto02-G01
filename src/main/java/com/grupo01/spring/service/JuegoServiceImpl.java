package com.grupo01.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.repository.JuegoDao;

@Service
public class JuegoServiceImpl implements JuegoService{

    // private static final Logger log = LoggerFactory.getLogger(JuegoController.class);	

    @Autowired
	private JuegoDao juegoDao;
	
	//Para Listar Todos
    @Override
	public List<Juego> findAll(){
		return juegoDao.findAll();
	}

}
