package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;

import java.util.List;

public interface CuentaServiceInterface {

    Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNotSupportedException;

    Cuenta find(long id);

    List<Cuenta> getCuentasByCliente(long dni);

    boolean tipoCuentaEstaSoportada(Cuenta cuenta);

    void eliminarCuenta(long id);
}
