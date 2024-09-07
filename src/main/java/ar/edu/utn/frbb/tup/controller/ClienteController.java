package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;


    @PostMapping
    public Cliente crearCliente(@RequestBody ClienteDto clienteDto) throws InputErrorException, ClienteAlreadyExistsException {
        clienteValidator.validate(clienteDto);
        return clienteService.darDeAltaCliente(clienteDto);
    }

    @GetMapping("/{dni}")
    public Cliente buscarClientePorDni(@PathVariable long dni) throws ClienteNotExistsException {
        return clienteService.buscarClientePorDni(dni);
    }

   @PutMapping("/{dni}")
    public Cliente modificarCliente(@RequestBody ClienteDto clienteDto) throws ClienteAlreadyExistsException, InputErrorException, ClienteNotExistsException {
        clienteValidator.validate(clienteDto);
        return clienteService.modificarCliente(clienteDto);
    }

    @DeleteMapping("/{dni}")
    public void eliminarCliente(@PathVariable long dni) throws ClienteNotExistsException {
        clienteService.eliminarCliente(dni);
    }

}
