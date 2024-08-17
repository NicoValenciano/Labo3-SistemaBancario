package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;
import ar.edu.utn.frbb.tup.persistence.persistanceInterface.MovimientoDaoInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientoDao extends AbstractBaseDao implements MovimientoDaoInterface {


    public void save(Movimiento movimiento) {
        MovimientoEntity entity = new MovimientoEntity(movimiento);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Movimiento find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((MovimientoEntity) getInMemoryDatabase().get(id)).toMovimiento();
    }

    public List<Movimiento> getMovimientosByCuenta(long idCuenta) {
        List<Movimiento> movimientosDeLaCuenta = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            MovimientoEntity movimiento = ((MovimientoEntity) object);
            if (movimiento.getNumeroCuenta().equals(idCuenta)) {
                movimientosDeLaCuenta.add(movimiento.toMovimiento());
            }
        }
        return movimientosDeLaCuenta;
    }

    @Override
    protected String getEntityName() {
        return "MOVIMIENTO";
    }
}
