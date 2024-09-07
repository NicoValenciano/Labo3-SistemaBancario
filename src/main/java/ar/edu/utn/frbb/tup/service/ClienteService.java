package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.ClienteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteService implements ClienteServiceInterface {

    @Autowired
    ClienteDao clienteDao;

    public void ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException, ClienteNotExistsException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setTitular(dniTitular);
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        titular.addCuenta(cuenta);
        clienteDao.save(titular);
    }

    public Cliente buscarClientePorDni(long dni) throws ClienteNotExistsException {
        Cliente cliente = clienteDao.find(dni, true);
        if(cliente == null) {
            throw new ClienteNotExistsException("El cliente no existe");
        }
        return cliente;
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws ClienteNotExistsException {
        Cliente cliente = buscarClientePorDni(clienteDto.getDni());
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setDni(clienteDto.getDni());
        cliente.setFechaNacimiento(LocalDate.parse(clienteDto.getFechaNacimiento()));
        cliente.setTipoPersona(TipoPersona.fromString(clienteDto.getTipoPersona()));
        clienteDao.save(cliente);
        return cliente;
    }

    public void eliminarCliente(long dni) throws ClienteNotExistsException {
        Cliente cliente = buscarClientePorDni(dni);
        clienteDao.delete(cliente);
    }
}
