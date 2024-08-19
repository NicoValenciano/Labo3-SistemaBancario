package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.service.ClienteService;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)

public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteController clienteController;

    @InjectMocks
    private ClienteService clienteService;


}
