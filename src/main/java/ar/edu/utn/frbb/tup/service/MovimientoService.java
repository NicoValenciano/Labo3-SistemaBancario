package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.MovimientoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovimientoService implements MovimientoServiceInterface {

    @Autowired
    MovimientoDao movimientoDao;

    public Movimiento find(long id){
        return movimientoDao.find(id);
    }

    public List<Movimiento> getMovimientosByCuenta(long id) {
        return movimientoDao.getMovimientosByCuenta(id);
    }
}
