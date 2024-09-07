package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;

import java.util.List;

public interface CuentaServiceInterface {

    Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNotSupportedException, ClienteNotExistsException;

    Cuenta find(long id);

    List<Cuenta> getCuentasByCliente(long dni) throws ClienteNotExistsException;

    boolean tipoCuentaEstaSoportada(Cuenta cuenta);

    void eliminarCuenta(long id);
}
