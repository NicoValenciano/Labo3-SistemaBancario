
package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private ClienteDto clienteDto;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento(String.valueOf(LocalDate.of(2020, 2, 7)));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setFechaNacimiento(String.valueOf(LocalDate.of(1978,3,25)));
        clienteDto.setDni(29857643);
        clienteDto.setTipoPersona(String.valueOf("f"));

        clienteService.darDeAltaCliente(clienteDto);

        verify(clienteDao, times(1)).find(29857643, false);
        verify(clienteDao, times(1)).save(any(Cliente.class));

    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(String.valueOf(LocalDate.of(1978, 3,25)));
        pepeRino.setTipoPersona(String.valueOf("f"));

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(String.valueOf(LocalDate.of(1978, 3,25)));
        pepeRino.setTipoPersona(String.valueOf("f"));

        Cliente cliente = new Cliente(pepeRino);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta,cliente.getDni());

        verify(clienteDao, times(1)).save(cliente);

        assertEquals(1, (cliente.getCuentas().size()));
        assertEquals(cliente.getDni(), cuenta.getTitular());

    }

    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        ClienteDto luciano = new ClienteDto();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(String.valueOf(LocalDate.of(1978, 3,25)));
        luciano.setTipoPersona(String.valueOf("f"));

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cliente cliente = new Cliente(luciano);

        when(clienteDao.find(26456439, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta,cliente.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2,cliente.getDni()));
        verify(clienteDao, times(1)).save(cliente);
        assertEquals(1, cliente.getCuentas().size());
        assertEquals(cliente.getDni(), cuenta.getTitular());

    }

    //1. Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    //2. Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino
    //3. Testear clienteService.buscarPorDni

    //1. Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    @Test
    public void testAgregarCuentasAClienteCAyCC() throws TipoCuentaAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(String.valueOf(LocalDate.of(1978, 3,25)));
        pepeRino.setTipoPersona(String.valueOf("f"));

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cliente cliente = new Cliente(pepeRino);

        when(clienteDao.find(26456439, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta,cliente.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        clienteService.agregarCuenta(cuenta2, cliente.getDni());
        verify(clienteDao, times(2)).save(cliente);
        assertEquals(2,cliente.getCuentas().size());
        assertEquals(cliente.getDni(), cuenta.getTitular());
        assertEquals(cliente.getDni(), cuenta2.getTitular());

    }
    //2. Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...
    @Test
    public void testAgregarCuentasAClienteCA() throws TipoCuentaAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(String.valueOf(LocalDate.of(1978, 3,25)));
        pepeRino.setTipoPersona(String.valueOf("f"));

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cliente cliente = new Cliente(pepeRino);

        when(clienteDao.find(26456439, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        clienteService.agregarCuenta(cuenta2, cliente.getDni());
        verify(clienteDao, times(2)).save(cliente);
        assertEquals(2, cliente.getCuentas().size());
        assertEquals(cliente.getDni(), cuenta.getTitular());
        assertEquals(cliente.getDni(), cuenta2.getTitular());

    }
    //3. Testear clienteService.buscarPorDni

    @Test
    public void testBuscarClientePorDniPositive() throws IllegalArgumentException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);
        assertEquals(pepeRino, clienteService.buscarClientePorDni(26456439));

    }

    @Test
    public void testBuscarClientePorDniNegative() throws IllegalArgumentException {
        // No mockeo ningún cliente
        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarClientePorDni(26456439));
    }

    @Test
    public void testBuscarClientePorDniNegative2() throws IllegalArgumentException {
        // Creo un cliente, se busca otro cliente que no existe
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarClientePorDni(32686279));
    }

    @Test
    void testModificarCliente() {
        long dni = 12345678;
        Cliente clienteExistente = new Cliente();
        clienteExistente.setDni(dni);
        clienteExistente.setNombre("Juan");
        clienteExistente.setApellido("Pérez");
        clienteExistente.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        clienteExistente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan Modificado");
        clienteDto.setApellido("Pérez Modificado");
        clienteDto.setFechaNacimiento("1995-10-20");
        clienteDto.setTipoPersona(String.valueOf(TipoPersona.PERSONA_JURIDICA));

        when(clienteDao.find(dni, true)).thenReturn(clienteExistente);

        Cliente clienteModificado = clienteService.modificarCliente(clienteDto, dni);

        assertEquals("Juan Modificado", clienteModificado.getNombre());
        assertEquals("Pérez Modificado", clienteModificado.getApellido());
        assertEquals(LocalDate.of(1995, 10, 20), clienteModificado.getFechaNacimiento());
        assertEquals(TipoPersona.PERSONA_JURIDICA, clienteModificado.getTipoPersona());
        verify(clienteDao, times(1)).find(dni, true);
        verify(clienteDao, times(1)).save(clienteModificado);
    }

    @Test
    void testEliminarCliente() {
        long dni = 12345678;
        Cliente clienteExistente = new Cliente();
        clienteExistente.setDni(dni);

        when(clienteDao.find(dni, true)).thenReturn(clienteExistente);

        clienteService.eliminarCliente(dni);

        verify(clienteDao, times(1)).find(dni, true);
        verify(clienteDao, times(1)).delete(clienteExistente);
    }
}
