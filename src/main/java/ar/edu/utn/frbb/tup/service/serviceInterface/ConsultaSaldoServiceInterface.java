package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;

public interface ConsultaSaldoServiceInterface {

    OperacionRespuesta consultarSaldo(long idCuenta) throws CuentaNotExistsException;
}
