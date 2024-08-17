package ar.edu.utn.frbb.tup.persistence.persistanceInterface;

import ar.edu.utn.frbb.tup.model.Cuenta;

import java.util.List;

public interface CuentaDaoInterface {

    void save(Cuenta cuenta);

    Cuenta find(long id);

    Cuenta findMovimientos(long id, boolean loadComplete);

    List<Cuenta> getCuentasByCliente(long dni);
}
