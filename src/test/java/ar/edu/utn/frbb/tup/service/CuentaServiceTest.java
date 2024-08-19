package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class CuentaServiceTest {
    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaDto cuentaDto;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente

    //    1 - cuenta existente
    @Test
    public void testCuentaAlreadyExists() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CA");

        when(cuentaDao.find(anyLong())).thenReturn(new Cuenta());
        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));

    }
    //    2 - cuenta no soportada
    @Test
    public void testTipoCuentaNoSoportada() throws TipoCuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("TIPO_NO_SOPORTADO");

        assertThrows(TipoCuentaNotSupportedException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
        verify(cuentaDao, never()).save(any());
        verify(clienteService, never()).agregarCuenta(any(), anyLong());
    }
    //    3 - cliente ya tiene cuenta de ese tipo
    @Test
    public void testClienteTieneCuentaDeEsteTipo() throws TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CA");
        cuentaDto.setDniTitular(123456789);

        Cuenta cuenta = new Cuenta(cuentaDto)
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setNumeroCuenta(123456789);

        doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }
    //    4 - cuenta creada exitosamente
    @Test
    public void testCuentaCreadaExitosamente() throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, ClienteAlreadyExistsException, TipoCuentaNotSupportedException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CA");
        cuentaDto.setDniTitular(123456789);

        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);

        assertEquals(TipoMoneda.PESOS, cuenta.getMoneda());
        assertEquals("CAJA_AHORRO", cuenta.getTipoCuenta().toString());
        verify(cuentaDao, times(1)).save(cuenta);
        verify(clienteService, times(1)).agregarCuenta(cuenta, 123456789);
    }
}
