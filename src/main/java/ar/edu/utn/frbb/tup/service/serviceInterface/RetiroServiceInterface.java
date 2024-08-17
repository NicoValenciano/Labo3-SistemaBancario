package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;

public interface RetiroServiceInterface {
    OperacionRespuesta hacerRetiro(long idCuenta, MovimientoDto movimientoDto) throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException;
}
