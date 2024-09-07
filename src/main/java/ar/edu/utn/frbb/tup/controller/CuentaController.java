package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, CuentaAlreadyExistsException, ClienteNotExistsException {
        cuentaValidator.validate(cuentaDto);
        try {
            return cuentaService.darDeAltaCuenta(cuentaDto);
        } catch (TipoCuentaAlreadyExistsException | CuentaAlreadyExistsException | TipoCuentaNotSupportedException | ClienteNotExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public Cuenta buscarCuentaPorId(@PathVariable long id) {
        return cuentaService.find(id);
    }

    @GetMapping("/cliente/{dni}")
    public List<Cuenta> getCuentasByClienteDni(@PathVariable long dni) throws ClienteNotExistsException {
        return cuentaService.getCuentasByCliente(dni);
    }

    @DeleteMapping("/{id}")
    public void eliminarCuenta(@PathVariable long id) {
        cuentaService.eliminarCuenta(id);
    }

    @GetMapping("/{idCuenta}/transacciones")
    public HistoricoRespuesta getMovimientosByCuenta(@PathVariable long idCuenta) {
        List<Movimiento> movimientos = movimientoService.getMovimientosByCuenta(idCuenta);
        return new HistoricoRespuesta(idCuenta, movimientos);
    }

}
