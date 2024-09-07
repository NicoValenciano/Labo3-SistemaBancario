package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.ClienteService;

public interface ClienteServiceInterface {

    void ClienteService(ClienteDao clienteDao);

    Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException;

    void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException, ClienteNotExistsException;

    Cliente buscarClientePorDni(long dni) throws ClienteNotExistsException;

}
