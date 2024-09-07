package ar.edu.utn.frbb.tup.controller.webmvctest;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.RetirarController;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.RetirarValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.RetiroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static ar.edu.utn.frbb.tup.model.TipoMoneda.valueOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetirarController.class)
public class RetirarControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RetirarValidator retirarValidator;

    @MockBean
    private RetiroService retiroService;

    @Test
    void testRetirar() throws Exception, CuentaWithoutSufficientFundsException {
        long idCuenta = 123456789;
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000.0);
        cuenta.setNumeroCuenta(123456789);
        cuenta.setMoneda(valueOf("PESOS"));

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMonto(999.0);
        movimientoDto.setMoneda("PESOS");

        OperacionRespuesta operacionRespuesta = new OperacionRespuesta("EXITOSA", "Retiro realizado correctamente");

        when(retiroService.hacerRetiro(idCuenta, movimientoDto)).thenReturn(operacionRespuesta);

        mockMvc.perform(post("/api/retirar/{idCuenta}", idCuenta, movimientoDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operacionRespuesta)))
                .andExpect(status().isOk());
    }

    @Test
    void testRetirarCuentaNoExistente() throws Exception, CuentaWithoutSufficientFundsException {
        long idCuenta = 999999L;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMonto(100.0);
        movimientoDto.setMoneda("PESOS");

        when(retiroService.hacerRetiro(eq(idCuenta), any(MovimientoDto.class)))
                .thenThrow(new CuentaNotExistsException("La cuenta no existe"));

        mockMvc.perform(post("/api/retirar/{idCuenta}", idCuenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(3002))
                .andExpect(jsonPath("$.errorMessage").value("La cuenta no existe"));
    }

    @Test
    void testRetirarMonedaIncompatible() throws Exception, CuentaWithoutSufficientFundsException {
        long idCuenta = 123456L;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMonto(100.0);
        movimientoDto.setMoneda("DOLARES");

        when(retiroService.hacerRetiro(eq(idCuenta), any(MovimientoDto.class)))
                .thenThrow(new TipoMonedaIncompatibleException("Tipo de moneda incompatible"));

        mockMvc.perform(post("/api/retirar/{idCuenta}", idCuenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(5003))
                .andExpect(jsonPath("$.errorMessage").value("Tipo de moneda incompatible"));
    }

    @Test
    void testRetirarFondosInsuficientes() throws Exception, CuentaWithoutSufficientFundsException {
        long idCuenta = 123456L;
        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setMonto(10000.0);
        movimientoDto.setMoneda("PESOS");

        when(retiroService.hacerRetiro(eq(idCuenta), any(MovimientoDto.class)))
                .thenThrow(new CuentaWithoutSufficientFundsException("Fondos insuficientes"));

        mockMvc.perform(post("/api/retirar/{idCuenta}", idCuenta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(5001))
                .andExpect(jsonPath("$.errorMessage").value("Fondos insuficientes"));
    }
}
