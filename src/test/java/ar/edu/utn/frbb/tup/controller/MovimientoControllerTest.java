package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.controller.MovimientoController;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.MovimientoNotExistsException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MovimientoControllerTest {

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovimientoById() throws MovimientoNotExistsException {
        long id = 123456789;
        Movimiento movimientoEsperado = new Movimiento();
        movimientoEsperado.setId(id);

        when(movimientoService.find(id)).thenReturn(movimientoEsperado);

        Movimiento movimientoObtenido = movimientoController.getMovimientoById(id);

        assertEquals(movimientoEsperado, movimientoObtenido);
        verify(movimientoService, times(1)).find(id);
    }

    @Test
    void testMovimientoNoExistente() throws MovimientoNotExistsException {
        long id = 123456789;
        when(movimientoService.find(id)).thenThrow(MovimientoNotExistsException.class);

        assertThrows(MovimientoNotExistsException.class, () -> movimientoController.getMovimientoById(id));
        verify(movimientoService, times(1)).find(id);
    }
}
