package ar.edu.utn.frbb.tup.controller;


import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.MovimientoNotExistsException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movimiento")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping("/{id}")
    public Movimiento getMovimientoById(@PathVariable long id) throws MovimientoNotExistsException {
        return movimientoService.find(id);
    }

}
