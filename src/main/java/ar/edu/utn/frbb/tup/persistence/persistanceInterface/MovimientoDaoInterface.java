package ar.edu.utn.frbb.tup.persistence.persistanceInterface;

import ar.edu.utn.frbb.tup.model.Movimiento;

import java.util.List;

public interface MovimientoDaoInterface {

        void save(Movimiento movimiento);

        Movimiento find(long id);

        List<Movimiento> getMovimientosByCuenta(long idCuenta);

}
