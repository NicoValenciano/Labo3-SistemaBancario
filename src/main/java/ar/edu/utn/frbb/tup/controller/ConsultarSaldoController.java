package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ConsultarSaldoValidator;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.service.ConsultaSaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultarSaldo")
public class ConsultarSaldoController {

    @Autowired
    ConsultaSaldoService ConsultaSaldoService;

    @Autowired
    ConsultarSaldoValidator ConsultarSaldoValidator;

    @GetMapping("/{idCuenta}")
    public OperacionRespuesta consultarSaldo(@PathVariable long idCuenta) throws CuentaNotExistsException{
        ConsultarSaldoValidator.validate(idCuenta);
        return ConsultaSaldoService.consultarSaldo(idCuenta);
    }
}
