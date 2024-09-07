package ar.edu.utn.frbb.tup.controller.webmvctest;

import ar.edu.utn.frbb.tup.controller.MovimientoController;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.MovimientoNotExistsException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovimientoController.class)
public class MovimientoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovimientoService movimientoService;

    @Test
    void testGetMovimientoById() throws MovimientoNotExistsException, Exception {
        long id = 123456789;
        Movimiento movimientoEsperado = new Movimiento();
        movimientoEsperado.setId(id);
        movimientoEsperado.setMonto(1000.0);
        movimientoEsperado.setNumeroCuenta(987654321);
        movimientoEsperado.setTipo(TipoMovimiento.CREDITO);
        movimientoEsperado.setDescripcionBreve("Deposito de prueba");

        when(movimientoService.find(id)).thenReturn(movimientoEsperado);

        mockMvc.perform(get("/movimiento/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoEsperado)))
                .andExpect(status().isOk());

    }

}
