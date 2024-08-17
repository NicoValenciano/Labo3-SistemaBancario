package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.persistanceInterface.CuentaDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuentaDao  extends AbstractBaseDao implements CuentaDaoInterface {

    @Autowired
    MovimientoDao movimientoDao;

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public Cuenta findMovimientos(long id, boolean loadComplete) {
        if (getInMemoryDatabase().get(id) == null)
            return null;
        Cuenta cuenta = ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
        if (loadComplete) {
            for (Movimiento movimiento :
                    movimientoDao.getMovimientosByCuenta(id)) {
                cuenta.addMovimiento(movimiento);
            }
        }
        return cuenta;
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void delete(long id) {
        getInMemoryDatabase().remove(id);
    }
}
