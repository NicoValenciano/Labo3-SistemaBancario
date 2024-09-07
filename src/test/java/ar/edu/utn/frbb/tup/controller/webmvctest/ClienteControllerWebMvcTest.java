package ar.edu.utn.frbb.tup.controller.webmvctest;

import ar.edu.utn.frbb.tup.controller.ClienteController;
import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteValidator clienteValidator;

    @MockBean
    private ClienteService clienteService;

    @Test
    void testCrearCliente() throws Exception, ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente clienteCreado = new Cliente();

        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(clienteCreado);

        mockMvc.perform(post("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testBuscarClientePorDni() throws Exception {
        long dni = 12345678;
        Cliente clienteEsperado = new Cliente();
        clienteEsperado.setDni(dni);
        clienteEsperado.setNombre("Juan");
        clienteEsperado.setApellido("Perez");
        clienteEsperado.setFechaNacimiento(LocalDate.parse("1980-11-12"));

        when(clienteService.buscarClientePorDni(dni)).thenReturn(clienteEsperado);

        mockMvc.perform(get("/api/cliente/{dni}", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteEsperado)))
                .andExpect(status().isOk());
    }

    @Test
    void testModificarCliente() throws Exception {
        ClienteDto clienteDto = new ClienteDto();
        long dni = 12345678;
        Cliente clienteModificado = new Cliente();

        when(clienteService.modificarCliente(clienteDto)).thenReturn(clienteModificado);

        mockMvc.perform(put("/api/cliente/{dni}", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarCliente() throws Exception {
        long dni = 12345678;

        doNothing().when(clienteService).eliminarCliente(dni);

        mockMvc.perform(delete("/api/cliente/{dni}", dni))
                .andExpect(status().isOk());
    }
}