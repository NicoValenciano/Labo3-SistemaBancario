package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
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

class ConsultaSaldoServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private MovimientoDao movimientoDao;

    @InjectMocks
    private ConsultaSaldoService consultaSaldoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsultarSaldo_CuentaExistente() throws CuentaNotExistsException {
        long idCuenta = 123456789;
        Cuenta cuenta = new Cuenta();
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        when(cuentaDao.find(idCuenta)).thenReturn(cuenta);

        OperacionRespuesta respuesta = consultaSaldoService.consultarSaldo(idCuenta);

        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("El saldo de la cuenta es: PESOS$ 1000.0", respuesta.getMensaje());
        verify(cuentaDao, times(2)).find(idCuenta);
        verify(movimientoDao, times(1)).save(any());
    }

    @Test
    void testConsultarSaldo_CuentaNoExistente() {
        long idCuenta = 1234;
        when(cuentaDao.find(idCuenta)).thenReturn(null);

        assertThrows(CuentaNotExistsException.class, () -> consultaSaldoService.consultarSaldo(idCuenta));
        verify(cuentaDao, times(1)).find(idCuenta);
        verify(movimientoDao, never()).save(any());
    }
}
