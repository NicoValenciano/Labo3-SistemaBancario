package ar.edu.utn.frbb.tup.controller.webmvctest;

import ar.edu.utn.frbb.tup.controller.ConsultarSaldoController;
import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.validator.ConsultarSaldoValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.service.ConsultaSaldoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static ar.edu.utn.frbb.tup.model.TipoMoneda.valueOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@WebMvcTest(ConsultarSaldoController.class)
public class ConsultarSaldoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConsultarSaldoValidator consultarSaldoValidator;

    @MockBean
    private ConsultaSaldoService consultarSaldoService;


    @Test
    void testConsultarSaldo() throws CuentaNotExistsException, Exception {
        long idCuenta = 123456789;
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000.0);
        cuenta.setNumeroCuenta(123456789);
        cuenta.setMoneda(valueOf("PESOS"));

        OperacionRespuesta operacionRespuesta = new OperacionRespuesta("EXITOSA", "El saldo de la cuenta es: PESOS$ 1000.0");

        when(consultarSaldoService.consultarSaldo(idCuenta)).thenReturn(operacionRespuesta);

        mockMvc.perform(get("/api/consultarSaldo/{idCuenta}", idCuenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operacionRespuesta)))
                .andExpect(status().isOk());

    }
}
