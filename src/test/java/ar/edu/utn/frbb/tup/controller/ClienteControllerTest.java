package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.ClienteController;
import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@WebMvcTest(ClienteController.class)

class ClienteControllerTest {

    @Mock
    private ClienteValidator clienteValidator;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCliente() throws ClienteAlreadyExistsException, InputErrorException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente clienteCreado = new Cliente();

        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(clienteCreado);

        Cliente resultado = clienteController.crearCliente(clienteDto);

        assertEquals(clienteCreado, resultado);
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    @Test
    void testBuscarClientePorDni() {
        long dni = 12345678;
        Cliente clienteEsperado = new Cliente();
        clienteEsperado.setDni(dni);

        when(clienteService.buscarClientePorDni(dni)).thenReturn(clienteEsperado);

        Cliente clienteObtenido = clienteController.buscarClientePorDni(dni);

        assertEquals(clienteEsperado, clienteObtenido);
        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    @Test
    void testModificarCliente() throws ClienteAlreadyExistsException, InputErrorException {
        ClienteDto clienteDto = new ClienteDto();
        long dni = 12345678;
        Cliente clienteModificado = new Cliente();

        when(clienteService.modificarCliente(clienteDto, dni)).thenReturn(clienteModificado);

        Cliente resultado = clienteController.modificarCliente(clienteDto, dni);

        assertEquals(clienteModificado, resultado);
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).modificarCliente(clienteDto, dni);
    }

    @Test
    void testEliminarCliente() {
        long dni = 12345678;

        clienteController.eliminarCliente(dni);

        verify(clienteService, times(1)).eliminarCliente(dni);
    }
}
