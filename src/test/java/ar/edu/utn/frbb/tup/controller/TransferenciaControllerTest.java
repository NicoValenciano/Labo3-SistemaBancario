package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransferenciaControllerTest {

    @Mock
    private TransferenciaValidator transferenciaValidator;

    @Mock
    private TransferenciaService transferenciaService;

    @InjectMocks
    private TransferenciaController transferenciaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferirExitosa() throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        OperacionRespuesta respuestaEsperada = new OperacionRespuesta("EXITOSA", "Transferencia realizada correctamente");

        when(transferenciaService.hacerTransferencia(movimientoDto)).thenReturn(respuestaEsperada);

        OperacionRespuesta respuestaObtenida = transferenciaController.transferir(movimientoDto);

        assertEquals(respuestaEsperada, respuestaObtenida);
        verify(transferenciaValidator, times(1)).validateOperacion(movimientoDto);
        verify(transferenciaService, times(1)).hacerTransferencia(movimientoDto);
    }

    @Test
    void testCuentaNoExistente() throws CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException, CuentaNotExistsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        doThrow(CuentaNotExistsException.class).when(transferenciaService).hacerTransferencia(movimientoDto);

        assertThrows(CuentaNotExistsException.class, () -> transferenciaController.transferir(movimientoDto));
        verify(transferenciaValidator, times(1)).validateOperacion(movimientoDto);
        verify(transferenciaService, times(1)).hacerTransferencia(movimientoDto);
    }

    @Test
    void testCuentaSinFondosSuficientes() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("USD");
        movimientoDto.setMonto(100.00);
        doThrow(CuentaWithoutSufficientFundsException.class).when(transferenciaService).hacerTransferencia(movimientoDto);

        assertThrows(CuentaWithoutSufficientFundsException.class, () -> transferenciaController.transferir(movimientoDto));
        verify(transferenciaValidator, times(1)).validateOperacion(movimientoDto);
        verify(transferenciaService, times(1)).hacerTransferencia(movimientoDto);
    }

    @Test
    void testTipoMonedaIncompatible() throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(100.00);
        doThrow(TipoMonedaIncompatibleException.class).when(transferenciaService).hacerTransferencia(movimientoDto);

        assertThrows(TipoMonedaIncompatibleException.class, () -> transferenciaController.transferir(movimientoDto));
        verify(transferenciaValidator, times(1)).validateOperacion(movimientoDto);
        verify(transferenciaService, times(1)).hacerTransferencia(movimientoDto);
    }
}