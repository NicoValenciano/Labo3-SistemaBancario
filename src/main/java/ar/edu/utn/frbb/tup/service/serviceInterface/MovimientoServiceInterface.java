package ar.edu.utn.frbb.tup.service.serviceInterface;

import ar.edu.utn.frbb.tup.model.Movimiento;

import java.util.List;

public interface MovimientoServiceInterface {

    Movimiento find(long id);

    List<Movimiento> getMovimientosByCuenta(long id);
}
