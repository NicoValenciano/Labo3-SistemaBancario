package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.MovimientoNotExistsException;

import java.util.List;

public interface MovimientoServiceInterface {

    Movimiento find(long id) throws MovimientoNotExistsException;

    List<Movimiento> getMovimientosByCuenta(long id);
}
