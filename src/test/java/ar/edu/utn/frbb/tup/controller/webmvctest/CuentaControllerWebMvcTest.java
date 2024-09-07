package ar.edu.utn.frbb.tup.controller.webmvctest;


import ar.edu.utn.frbb.tup.controller.CuentaController;
import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuentaController.class)
public class CuentaControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CuentaValidator cuentaValidator;

    @MockBean
    private CuentaService cuentaService;

    @MockBean
    private MovimientoService movimientoService;

    @Test
    void testCrearCuenta() throws TipoCuentaNotSupportedException, Exception, CuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        Cuenta cuentaCreada = new Cuenta();

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuentaCreada);

        mockMvc.perform(post("/api/cuenta", cuentaDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuentaDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testBuscarCuentaPorDni() throws Exception {
        long id = 12345678;
        Cuenta cuentaEsperada = new Cuenta();
        cuentaEsperada.setNumeroCuenta(id);
        cuentaEsperada.setTitular(789456123);
        cuentaEsperada.setBalance(1000.0);
        cuentaEsperada.setMoneda(TipoMoneda.valueOf("PESOS"));
        cuentaEsperada.setTipoCuenta(TipoCuenta.valueOf("CAJA_AHORRO"));

        when(cuentaService.find(id)).thenReturn(cuentaEsperada);

        mockMvc.perform(get("/api/cuenta/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuentaEsperada)))
                .andExpect(status().isOk());
    }


}
