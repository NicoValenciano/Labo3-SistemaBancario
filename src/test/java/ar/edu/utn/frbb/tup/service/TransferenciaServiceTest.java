package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenciaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private MovimientoDao movimientoDao;

    @Mock
    private BanelcoService banelcoService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHacerTransferencia_CuentaOrigenNotExistsException() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(987654321);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("ARS");

        when(cuentaDao.find(idCuentaOrigen)).thenReturn(null);
        assertThrows(CuentaNotExistsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));

        verify(cuentaDao, times(1)).find(idCuentaOrigen);
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testHacerTransferencia_CuentaDestinoNotExistsException() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaDestino = 987654321;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaDestino);
        movimientoDto.setCuentaDestino(123456789);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("ARS");

        when(cuentaDao.find(idCuentaDestino)).thenReturn(null);
        assertThrows(CuentaNotExistsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));

        verify(cuentaDao, times(1)).find(idCuentaDestino);
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testHacerTransferencia_TipoMonedaIncompatibleException_Origen() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(987654321);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("ARS");


        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setTitular(idCuentaOrigen);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setTitular(idCuentaDestino);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);


        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        assertThrows(TipoMonedaIncompatibleException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));

        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testHacerTransferencia_TipoMonedaIncompatibleException_Destino() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(987654321);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("USD");


        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setTitular(idCuentaOrigen);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setTitular(idCuentaDestino);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);


        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        assertThrows(TipoMonedaIncompatibleException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));

        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testHacerTransferencia_CuentaWithoutSufficientFundsException() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(987654321);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("ARS");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setTitular(idCuentaOrigen);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setTitular(idCuentaDestino);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        assertThrows(CuentaWithoutSufficientFundsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));

        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
        verify(clienteDao, never()).save(any(Cliente.class));
    }

    @Test
    void testHacerTransferencia_Exitosa_MismoBanco() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {

        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(1000.0);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setMoneda(TipoMoneda.PESOS);

        Cliente clienteOrigen = new Cliente();
        clienteOrigen.setBanco("BANCO_ORIGEN");
        Cliente clienteDestino = new Cliente();
        clienteDestino.setBanco("BANCO_ORIGEN");

        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        when(clienteDao.find(cuentaOrigen.getTitular(), true)).thenReturn(clienteOrigen);
        when(clienteDao.find(cuentaDestino.getTitular(), true)).thenReturn(clienteDestino);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(500.0);

        OperacionRespuesta respuesta = transferenciaService.hacerTransferencia(movimientoDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("Transferencia exitosa", respuesta.getMensaje());
        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(clienteDao, times(2)).find(cuentaOrigen.getTitular(), true);
        verify(clienteDao, times(2)).find(cuentaDestino.getTitular(), true);
        verify(cuentaDao, times(1)).save(cuentaOrigen);
        verify(cuentaDao, times(1)).save(cuentaDestino);
        verify(movimientoDao, times(2)).save(any());
    }
    @Test
    void testHacerTransferencia_Exitosa_DiferenteBanco() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(1000.0);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setMoneda(TipoMoneda.PESOS);

        Cliente clienteOrigen = new Cliente();
        clienteOrigen.setBanco("BANCO_ORIGEN");
        Cliente clienteDestino = new Cliente();
        clienteDestino.setBanco("BANCO_DESTINO");

        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        when(clienteDao.find(cuentaOrigen.getTitular(), true)).thenReturn(clienteOrigen);
        when(clienteDao.find(cuentaDestino.getTitular(), true)).thenReturn(clienteDestino);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(500.0);

        when(banelcoService.aprobarTransaccion(movimientoDto.getMonto())).thenReturn(true);

        OperacionRespuesta respuesta = transferenciaService.hacerTransferencia(movimientoDto);

        assertTrue(banelcoService.aprobarTransaccion(movimientoDto.getMonto()));
        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("Transferencia exitosa", respuesta.getMensaje());
        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(clienteDao, times(2)).find(cuentaOrigen.getTitular(), true);
        verify(clienteDao, times(2)).find(cuentaDestino.getTitular(), true);
        verify(cuentaDao, times(1)).save(cuentaOrigen);
        verify(cuentaDao, times(1)).save(cuentaDestino);
        verify(movimientoDao, times(2)).save(any());
    }

    @Test
    void testHacerTransferencia_Fallida_DiferenteBanco() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(123456L);
        movimientoDto.setCuentaDestino(789012L);
        movimientoDto.setMonto(1002.0);
        movimientoDto.setMoneda("ARS");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456L);
        cuentaOrigen.setBalance(2000.0);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setTitular(1L);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(789012L);
        cuentaDestino.setBalance(500.0);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setTitular(2L);

        Cliente titularOrigen = new Cliente();
        titularOrigen.setDni(1L);
        titularOrigen.setBanco("BancoA");

        Cliente titularDestino = new Cliente();
        titularDestino.setDni(2L);
        titularDestino.setBanco("BancoB");

        when(cuentaDao.find(123456L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(789012L)).thenReturn(cuentaDestino);
        when(clienteDao.find(1L, true)).thenReturn(titularOrigen);
        when(clienteDao.find(2L, true)).thenReturn(titularDestino);
        when(banelcoService.aprobarTransaccion(1002.0)).thenReturn(false);

        OperacionRespuesta resultado = transferenciaService.hacerTransferencia(movimientoDto);

        assertEquals("FALLIDA", resultado.getEstado());
        assertEquals("Transferencia fallida.", resultado.getMensaje());
        verify(cuentaDao, never()).save(any(Cuenta.class));
        verify(movimientoDao, never()).save(any(Movimiento.class));
    }

    @Test
    void testHacerTransferencia_CuentaOrigenNoExistente() {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        when(cuentaDao.find(idCuentaOrigen)).thenReturn(null);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("PESOS");
        movimientoDto.setMonto(500.0);

        assertThrows(CuentaNotExistsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));
        verify(cuentaDao, times(1)).find(idCuentaOrigen);
        verify(cuentaDao, never()).find(idCuentaDestino);
        verify(clienteDao, never()).find(anyLong(), anyBoolean());
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
    }

    @Test
    void testHacerTransferencia_CuentaDestinoNoExistente() {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        when(cuentaDao.find(idCuentaDestino)).thenReturn(null);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("PESOS");
        movimientoDto.setMonto(500.0);

        assertThrows(CuentaNotExistsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));
        verify(cuentaDao, times(1)).find(idCuentaOrigen);
        verify(cuentaDao, never()).find(idCuentaDestino);
        verify(clienteDao, never()).find(anyLong(), anyBoolean());
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
    }

    @Test
    void testHacerTransferencia_CuentaOrigenSinSaldo() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException  {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(0.0);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        Cliente clienteOrigen = new Cliente();
        clienteOrigen.setBanco("BANCO_ORIGEN");
        Cliente clienteDestino = new Cliente();
        clienteDestino.setBanco("BANCO_DESTINO");
        when(cuentaDao.find(idCuentaOrigen)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(idCuentaDestino)).thenReturn(cuentaDestino);
        when(clienteDao.find(cuentaOrigen.getTitular(), true)).thenReturn(clienteOrigen);
        when(clienteDao.find(cuentaDestino.getTitular(), true)).thenReturn(clienteDestino);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(500.0);

        assertThrows(CuentaWithoutSufficientFundsException.class, () -> transferenciaService.hacerTransferencia(movimientoDto));
        verify(cuentaDao, times(2)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(clienteDao, never()).find(anyLong(), anyBoolean());
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
}
}
