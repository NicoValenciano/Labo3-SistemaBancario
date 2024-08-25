package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ConsultarSaldoValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.service.ConsultaSaldoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ConsultarSaldoControllerTest {

    @Mock
    private ConsultarSaldoValidator consultarSaldoValidator;

    @Mock
    private ConsultaSaldoService consultaSaldoService;

    @InjectMocks
    private ConsultarSaldoController consultarSaldoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsultarSaldo_IdCuentaValido() throws CuentaNotExistsException {
        long idCuenta = 1234;
        String estadoEsperado = "EXITOSA";
        String mensajeEsperado = "El saldo de la cuenta es: $ 5000.0PESOS";

        OperacionRespuesta respuestaEsperada = new OperacionRespuesta(estadoEsperado, mensajeEsperado);

        when(consultaSaldoService.consultarSaldo(idCuenta)).thenReturn(respuestaEsperada);

        OperacionRespuesta respuestaObtenida = consultarSaldoController.consultarSaldo(idCuenta);

        assertEquals(estadoEsperado, respuestaObtenida.getEstado());
        assertEquals(mensajeEsperado, respuestaObtenida.getMensaje());
        verify(consultarSaldoValidator, times(1)).validate(idCuenta);
        verify(consultaSaldoService, times(1)).consultarSaldo(idCuenta);
    }

    @Test
    void testCuentaNoExistente() throws IllegalArgumentException, CuentaNotExistsException {
        long idCuenta = 123456789;
        doThrow(CuentaNotExistsException.class).when(consultaSaldoService).consultarSaldo(idCuenta);

        assertThrows(CuentaNotExistsException.class, () -> consultarSaldoController.consultarSaldo(idCuenta));
        verify(consultarSaldoValidator, times(1)).validate(idCuenta);
        verify(consultaSaldoService, times(1)).consultarSaldo(idCuenta);
    }

    @Test
    void testValidacionFallida() throws CuentaNotExistsException {
        long idCuenta = 123456789;
        doThrow(IllegalArgumentException.class).when(consultarSaldoValidator).validate(idCuenta);

        assertThrows(IllegalArgumentException.class, () -> consultarSaldoController.consultarSaldo(idCuenta));
        verify(consultarSaldoValidator, times(1)).validate(idCuenta);
        verify(consultaSaldoService, never()).consultarSaldo(anyLong());
    }
}