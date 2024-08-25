package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.controller.DepositarController;
import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.DepositarValidator;
import ar.edu.utn.frbb.tup.controller.validator.OperacionValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.DepositoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DepositarControllerTest {

    @Mock
    private DepositarValidator depositarValidator;

    @Mock
    private DepositoService depositoService;

    @InjectMocks
    private DepositarController depositarController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositarExitoso() throws CuentaNotExistsException, TipoMonedaIncompatibleException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);

        OperacionRespuesta respuestaEsperada = new OperacionRespuesta("EXITOSA", "Depósito realizado correctamente");

        when(depositoService.hacerDeposito(idCuenta, movimientoDto)).thenReturn(respuestaEsperada);
        OperacionRespuesta respuestaObtenida = depositarController.depositar(idCuenta, movimientoDto);

        assertEquals(respuestaEsperada, respuestaObtenida);
        verify(depositarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(depositarValidator, times(1)).validateOperacion(movimientoDto);
        verify(depositoService, times(1)).hacerDeposito(idCuenta, movimientoDto);
    }

    @Test
    void testCuentaNoExistente() throws TipoMonedaIncompatibleException, CuentaNotExistsException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        doThrow(CuentaNotExistsException.class).when(depositoService).hacerDeposito(idCuenta, movimientoDto);

        assertThrows(CuentaNotExistsException.class, () -> depositarController.depositar(idCuenta, movimientoDto));
        verify(depositarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(depositarValidator, times(1)).validateOperacion(movimientoDto);
        verify(depositoService, times(1)).hacerDeposito(idCuenta, movimientoDto);
    }

    @Test
    void testTipoMonedaIncompatible() throws CuentaNotExistsException, TipoMonedaIncompatibleException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(100.00);
        doThrow(TipoMonedaIncompatibleException.class).when(depositoService).hacerDeposito(idCuenta, movimientoDto);

        assertThrows(TipoMonedaIncompatibleException.class, () -> depositarController.depositar(idCuenta, movimientoDto));
        verify(depositarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(depositarValidator, times(1)).validateOperacion(movimientoDto);
        verify(depositoService, times(1)).hacerDeposito(idCuenta, movimientoDto);
    }
}
