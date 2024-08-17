package ar.edu.utn.frbb.tup.persistence.persistanceInterface;

import ar.edu.utn.frbb.tup.model.Cliente;

public interface ClienteDaoInterface {

    Cliente find(long dni, boolean loadComplete);

    void save(Cliente cliente);

    void delete(Cliente cliente);

}
