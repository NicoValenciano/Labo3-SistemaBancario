package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.DepositarValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.DepositoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/depositar")
public class DepositarController {
    @Autowired
    DepositoService DepositoService;

    @Autowired
    DepositarValidator DepositarValidator;

    @PostMapping("/{idCuenta}")
    public OperacionRespuesta depositar(@PathVariable long idCuenta, @RequestBody MovimientoDto movimientoDto) throws CuentaNotExistsException, TipoMonedaIncompatibleException {
        DepositarValidator.validateIdCuenta(idCuenta);
        DepositarValidator.validateOperacion(movimientoDto);
        return DepositoService.hacerDeposito(idCuenta, movimientoDto);
    }
}
