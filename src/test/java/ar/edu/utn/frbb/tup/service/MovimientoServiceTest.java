package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.MovimientoNotExistsException;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    @Mock
    private MovimientoDao movimientoDao;

    @InjectMocks
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFind() throws MovimientoNotExistsException {
        long id = 123456789;
        Movimiento movimiento = new Movimiento();
        when(movimientoDao.find(id)).thenReturn(movimiento);

        Movimiento resultado = movimientoService.find(id);

        assertEquals(movimiento, resultado);
        verify(movimientoDao, times(2)).find(id);
    }

    @Test
    void testGetMovimientosByCuenta() {
        long idCuenta = 123456789;
        Movimiento movimiento1 = new Movimiento();
        Movimiento movimiento2 = new Movimiento();
        List<Movimiento> movimientos = Arrays.asList(movimiento1, movimiento2);
        when(movimientoDao.getMovimientosByCuenta(idCuenta)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.getMovimientosByCuenta(idCuenta);

        assertEquals(movimientos, resultado);
        verify(movimientoDao, times(1)).getMovimientosByCuenta(idCuenta);
    }

    @Test
    void testFindExcepcion() {
        long id = 123456789;
        when(movimientoDao.find(id)).thenThrow(new RuntimeException("Error al buscar movimiento"));

        assertThrows(RuntimeException.class, () -> movimientoService.find(id));
        verify(movimientoDao, times(1)).find(id);
    }
}
