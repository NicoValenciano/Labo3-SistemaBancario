package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.validator.RetirarValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.service.RetiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retirar")
public class RetirarController {

    @Autowired
    RetiroService RetiroService;

    @Autowired
    RetirarValidator RetirarValidator;

    @PostMapping("/{idCuenta}")
    public OperacionRespuesta retirar(@PathVariable long idCuenta, @RequestBody MovimientoDto movimientoDto) throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        RetirarValidator.validateIdCuenta(idCuenta);
        RetirarValidator.validateOperacion(movimientoDto);
        return RetiroService.hacerRetiro(idCuenta,movimientoDto);
    }
}
