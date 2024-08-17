package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.ConsultaSaldoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaSaldoService implements ConsultaSaldoServiceInterface {

    @Autowired
    CuentaDao cuentaDao;

    @Autowired
    MovimientoDao movimientoDao;

    public OperacionRespuesta consultarSaldo(long idCuenta) throws CuentaNotExistsException {

        //Validamos que la cuenta exista
        if(cuentaDao.find(idCuenta) == null) {
            throw new CuentaNotExistsException("La cuenta " + idCuenta + " no existe.");
        }else {
            //Se consulta el saldo
            Cuenta cuenta = cuentaDao.find(idCuenta);
            double saldo = cuenta.getBalance();

            //Se registra el movimiento
            Movimiento movimiento = new Movimiento();
            movimiento.setNumeroCuenta(idCuenta);
            movimiento.setMonto(saldo);
            movimiento.setTipo(TipoMovimiento.CONSULTA_DE_SALDO);
            movimiento.setDescripcionBreve("Consulta de saldo");;
            movimientoDao.save(movimiento);

            return new OperacionRespuesta("EXITOSA", "El saldo de la cuenta es: " + cuenta.getMoneda() + "$ " + saldo);
        }
    }
}
