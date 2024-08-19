package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenciaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private MovimientoDao movimientoDao;

    private BanelcoService banelcoService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        banelcoService = new BanelcoService();
    }

    @Test
    void testHacerTransferencia_Exitosa_MismoBanco() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        BanelcoService banelcoService = mock(BanelcoService.class);
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
        verify(cuentaDao, times(4)).find(idCuentaOrigen);
        verify(cuentaDao, times(3)).find(idCuentaDestino);
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
        movimientoDto.setMonto(501.0);

        OperacionRespuesta respuesta = transferenciaService.hacerTransferencia(movimientoDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        assertEquals("Transferencia exitosa", respuesta.getMensaje());
        verify(cuentaDao, times(4)).find(idCuentaOrigen);
        verify(cuentaDao, times(3)).find(idCuentaDestino);
        verify(clienteDao, times(2)).find(cuentaOrigen.getTitular(), true);
        verify(clienteDao, times(2)).find(cuentaDestino.getTitular(), true);
        verify(cuentaDao, times(1)).save(cuentaOrigen);
        verify(cuentaDao, times(1)).save(cuentaDestino);
        verify(movimientoDao, times(2)).save(any());
    }
    @Test
    void testHacerTransferencia_Fallida_DiferenteBanco() throws CuentaNotExistsException, TipoMonedaIncompatibleException, CuentaWithoutSufficientFundsException {
        long idCuentaOrigen = 123456789;
        long idCuentaDestino = 987654321;
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(2000.0);
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
        //when(BanelcoService.aprobarTransaccion(anyDouble())).thenReturn(true);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(idCuentaOrigen);
        movimientoDto.setCuentaDestino(idCuentaDestino);
        movimientoDto.setMoneda("ARS");
        movimientoDto.setMonto(1002.0); // Monto superior a 1000 para que la transacción no sea aprobada

        OperacionRespuesta respuesta = transferenciaService.hacerTransferencia(movimientoDto);

        //assertEquals("FALLIDA", respuesta.getEstado());
        assertEquals("Transferencia fallida.", respuesta.getMensaje());
        verify(cuentaDao, times(1)).find(idCuentaOrigen);
        verify(cuentaDao, times(1)).find(idCuentaDestino);
        verify(clienteDao, times(1)).find(cuentaOrigen.getTitular(), true);
        verify(clienteDao, times(1)).find(cuentaDestino.getTitular(), true);
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
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
        verify(cuentaDao, times(3)).find(idCuentaOrigen);
        verify(cuentaDao, times(2)).find(idCuentaDestino);
        verify(clienteDao, never()).find(anyLong(), anyBoolean());
        verify(cuentaDao, never()).save(any());
        verify(movimientoDao, never()).save(any());
}
}
