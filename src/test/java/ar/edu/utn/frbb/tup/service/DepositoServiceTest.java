package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepositoServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private MovimientoDao movimientoDao;

    @InjectMocks
    private DepositoService depositoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHacerDeposito_CuentaExistente() throws CuentaNotExistsException, TipoMonedaIncompatibleException {

        long idCuenta = 123456789;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123456789);
        cuenta.setMoneda(TipoMoneda.PESOS);
        when(cuentaDao.find(idCuenta)).thenReturn(cuenta);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(500.0);

        OperacionRespuesta respuesta = depositoService.hacerDeposito(idCuenta, movimientoDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("Se depositaron PESOS$ 500.0", respuesta.getMensaje());
        verify(cuentaDao, times(3)).find(idCuenta);
        verify(cuentaDao, times(1)).save(cuenta);
        verify(movimientoDao, times(1)).save(any());
    }

    @Test
    void testHacerDeposito_CuentaNoExistente() {
        long idCuenta = 1234;
        when(cuentaDao.find(idCuenta)).thenReturn(null);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMoneda("PESOS");
        movimientoDto.setMonto(500.0);

        assertThrows(CuentaNotExistsException.class, () -> depositoService.hacerDeposito(idCuenta, movimientoDto));
        verify(cuentaDao, times(1)).find(idCuenta);
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
    }

    @Test
    void testHacerDeposito_MonedaIncompatible() {
        long idCuenta = 1234;
        Cuenta cuenta = new Cuenta();
        cuenta.setMoneda(TipoMoneda.DOLARES);
        when(cuentaDao.find(idCuenta)).thenReturn(cuenta);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(500.0);

        assertThrows(TipoMonedaIncompatibleException.class, () -> depositoService.hacerDeposito(idCuenta, movimientoDto));
        verify(cuentaDao, times(2)).find(idCuenta);
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
    }
}
