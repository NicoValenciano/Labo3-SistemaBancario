package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.RetirarController;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.RetirarValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.RetiroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RetirarControllerTest {

    @Mock
    private RetirarValidator retirarValidator;

    @Mock
    private RetiroService retiroService;

    @InjectMocks
    private RetirarController retirarController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetirarExitoso() throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        OperacionRespuesta respuestaEsperada = new OperacionRespuesta("EXITOSA", "Retiro realizado correctamente");

        when(retiroService.hacerRetiro(idCuenta, movimientoDto)).thenReturn(respuestaEsperada);

        OperacionRespuesta respuestaObtenida = retirarController.retirar(idCuenta, movimientoDto);

        assertEquals(respuestaEsperada, respuestaObtenida);
        verify(retirarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(retirarValidator, times(1)).validateOperacion(movimientoDto);
        verify(retiroService, times(1)).hacerRetiro(idCuenta, movimientoDto);
    }

    @Test
    void testCuentaNoExistente() throws CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException, CuentaNotExistsException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        doThrow(CuentaNotExistsException.class).when(retiroService).hacerRetiro(idCuenta, movimientoDto);

        assertThrows(CuentaNotExistsException.class, () -> retirarController.retirar(idCuenta, movimientoDto));
        verify(retirarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(retirarValidator, times(1)).validateOperacion(movimientoDto);
        verify(retiroService, times(1)).hacerRetiro(idCuenta, movimientoDto);
    }

    @Test
    void testCuentaSinFondosSuficientes() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        doThrow(CuentaWithoutSufficientFundsException.class).when(retiroService).hacerRetiro(idCuenta, movimientoDto);

        assertThrows(CuentaWithoutSufficientFundsException.class, () -> retirarController.retirar(idCuenta, movimientoDto));
        verify(retirarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(retirarValidator, times(1)).validateOperacion(movimientoDto);
        verify(retiroService, times(1)).hacerRetiro(idCuenta, movimientoDto);
    }

    @Test
    void testTipoMonedaIncompatible() throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        long idCuenta = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(100.00);
        doThrow(TipoMonedaIncompatibleException.class).when(retiroService).hacerRetiro(idCuenta, movimientoDto);

        assertThrows(TipoMonedaIncompatibleException.class, () -> retirarController.retirar(idCuenta, movimientoDto));
        verify(retirarValidator, times(1)).validateIdCuenta(idCuenta);
        verify(retirarValidator, times(1)).validateOperacion(movimientoDto);
        verify(retiroService, times(1)).hacerRetiro(idCuenta, movimientoDto);
    }
}