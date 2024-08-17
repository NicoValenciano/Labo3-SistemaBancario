package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;

import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferenciaController {

    @Autowired
    TransferenciaService TransferenciaService;

    @Autowired
    TransferenciaValidator TransferenciaValidator;

    @PostMapping()
    public OperacionRespuesta transferir(@RequestBody MovimientoDto movimientoDto) throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        TransferenciaValidator.validateOperacion(movimientoDto);
        OperacionRespuesta operacionRespuesta = new OperacionRespuesta();
        return TransferenciaService.hacerTransferencia(movimientoDto);

    }
}
