package ar.edu.utn.frbb.tup.controller.webmvctest;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.TransferenciaController;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferenciaController.class)
public class TransferenciaControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferenciaValidator transferenciaValidator;

    @MockBean
    private TransferenciaService transferenciaService;

    @Test
    void testRealizarTransferenciaExitosa() throws Exception, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(123456L);
        movimientoDto.setCuentaDestino(789012L);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("PESOS");

        OperacionRespuesta respuestaEsperada = new OperacionRespuesta("EXITOSA", "Transferencia realizada correctamente");

        when(transferenciaService.hacerTransferencia(any(MovimientoDto.class)))
                .thenReturn(respuestaEsperada);

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITOSA"))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada correctamente"));
    }

    @Test
    void testRealizarTransferenciaCuentaOrigenNoExiste() throws Exception, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(999999L);
        movimientoDto.setCuentaDestino(789012L);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("PESOS");

        when(transferenciaService.hacerTransferencia(any(MovimientoDto.class)))
                .thenThrow(new CuentaNotExistsException("La cuenta origen no existe"));

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(3002))
                .andExpect(jsonPath("$.errorMessage").value("La cuenta origen no existe"));
    }

    @Test
    void testRealizarTransferenciaFondosInsuficientes() throws Exception, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(123456L);
        movimientoDto.setCuentaDestino(789012L);
        movimientoDto.setMonto(10000.0);
        movimientoDto.setMoneda("PESOS");

        when(transferenciaService.hacerTransferencia(any(MovimientoDto.class)))
                .thenThrow(new CuentaWithoutSufficientFundsException("Fondos insuficientes en la cuenta origen"));

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(5001))
                .andExpect(jsonPath("$.errorMessage").value("Fondos insuficientes en la cuenta origen"));
    }

    @Test
    void testRealizarTransferenciaMonedaIncompatible() throws Exception, CuentaWithoutSufficientFundsException {
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setCuentaOrigen(123456L);
        movimientoDto.setCuentaDestino(789012L);
        movimientoDto.setMonto(1000.0);
        movimientoDto.setMoneda("DOLARES");

        when(transferenciaService.hacerTransferencia(any(MovimientoDto.class)))
                .thenThrow(new TipoMonedaIncompatibleException("Tipo de moneda incompatible"));

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(5003))
                .andExpect(jsonPath("$.errorMessage").value("Tipo de moneda incompatible"));
    }

}
