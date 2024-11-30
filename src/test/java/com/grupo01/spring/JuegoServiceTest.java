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
}
