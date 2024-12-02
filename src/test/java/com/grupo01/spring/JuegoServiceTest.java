package com.grupo01.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupo01.spring.model.Juego;
import com.grupo01.spring.repository.JuegoDao;
import com.grupo01.spring.service.JuegoServiceImpl;

@SpringBootTest
public class JuegoServiceTest {

    @Mock
    private JuegoDao juegoDao;

    @InjectMocks
    private JuegoServiceImpl juegoServiceImpl;

    @Test
    public void testDevuelveJuegos() {
        List<Juego> juegos = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Crear 5 registros simulados
            Juego juego = new Juego();
            juego.setRank(i + 1);
            juego.setName("Juego " + (i + 1));
            juegos.add(juego);
        }

        // Configurar el mock para que devuelva la lista simulada cuando se llame a findAll
        when(juegoDao.findAll()).thenReturn(juegos);

        //Llamo al metodo del servicio
        List<Juego> resultado = juegoServiceImpl.findAll();

        //Verificar resultados
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

        int totalSaved = juegoServiceImpl.saveCsv(juegos);

        // Verificar que se guardan todos los registros
        assertEquals(juegos.size(), totalSaved, "El número total de registros guardados no coincide con la lista de entrada");

        // Verificar que el repositorio se llamó la cantidad correcta de veces
        int expectedBatchCalls = (int) Math.ceil(juegos.size() / 100.0);
        verify(juegoDao, times(expectedBatchCalls)).saveAll(anyList());
    }

    @Test
    public void testEliminaJuego() {
        Integer juegoId = 1; // ID del juego a eliminar

        // Llamar al método del servicio
        juegoServiceImpl.deleteById(juegoId);

        // Verificar que el método deleteById se llamó con el ID correcto
        verify(juegoDao, times(1)).deleteById(juegoId);
    }
}
