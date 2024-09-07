package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @Mock
    private CuentaValidator cuentaValidator;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCuenta() throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        CuentaDto cuentaDto = new CuentaDto();
        Cuenta cuentaCreada = new Cuenta();

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuentaCreada);

        Cuenta resultado = cuentaController.crearCuenta(cuentaDto);

        assertEquals(cuentaCreada, resultado);
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    @Test
    void testBuscarCuentaPorId() {
        long id = 123456789;
        Cuenta cuentaEsperada = new Cuenta();
        cuentaEsperada.setNumeroCuenta(id);

        when(cuentaService.find(id)).thenReturn(cuentaEsperada);

        Cuenta cuentaObtenida = cuentaController.buscarCuentaPorId(id);

        assertEquals(cuentaEsperada, cuentaObtenida);
        verify(cuentaService, times(1)).find(id);
    }

    @Test
    void testGetCuentasByClienteDni() {
        long dni = 12345678;
        List<Cuenta> cuentasEsperadas = Arrays.asList(new Cuenta(), new Cuenta());

        when(cuentaService.getCuentasByCliente(dni)).thenReturn(cuentasEsperadas);

        List<Cuenta> cuentasObtenidas = cuentaController.getCuentasByClienteDni(dni);

        assertEquals(cuentasEsperadas, cuentasObtenidas);
        verify(cuentaService, times(1)).getCuentasByCliente(dni);
    }

    @Test
    void testEliminarCuenta() {
        long id = 123456789;

        cuentaController.eliminarCuenta(id);

        verify(cuentaService, times(1)).eliminarCuenta(id);
    }

    @Test
    void testGetHistoricoTransacciones() {
        long idCuenta = 123456789;
        List<Movimiento> movimientos = Arrays.asList(new Movimiento(), new Movimiento());

        when(movimientoService.getMovimientosByCuenta(idCuenta)).thenReturn(movimientos);

        HistoricoRespuesta historicoRespuesta = cuentaController.getMovimientosByCuenta(idCuenta);

        assertEquals(idCuenta, historicoRespuesta.getNumeroCuenta());
        assertEquals(movimientos, historicoRespuesta.getTransferencias());
        verify(movimientoService, times(1)).getMovimientosByCuenta(idCuenta);
    }
}