package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.DepositoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositoService implements DepositoServiceInterface {

    @Autowired
    CuentaDao cuentaDao;

    @Autowired
    MovimientoDao movimientoDao;

    public OperacionRespuesta hacerDeposito(long idCuenta, MovimientoDto movimientoDto) throws CuentaNotExistsException, TipoMonedaIncompatibleException {

        //Validamos que la cuenta exista
        if(cuentaDao.find(idCuenta) == null) {
            throw new CuentaNotExistsException("La cuenta " + idCuenta + " no existe.");
        }

        //Validamos que la cuenta opera con la moneda con la que se intenta operar
        if (!(cuentaDao.find(idCuenta).getMoneda().equals(TipoMoneda.fromString(movimientoDto.getMoneda())))) {
            throw new TipoMonedaIncompatibleException("La cuenta " + idCuenta + " no opera con la moneda con la que se intenta operar");
        }

        //Se hace el depósito y se actualiza la cuenta
        Cuenta cuenta = cuentaDao.find(idCuenta);
        cuenta.setBalance(cuenta.getBalance() + movimientoDto.getMonto());
        cuentaDao.save(cuenta);

        //Se registra el movimiento
        Movimiento movimiento = new Movimiento(movimientoDto);
        movimiento.setNumeroCuenta(idCuenta);
        movimiento.setMonto(movimientoDto.getMonto());
        movimiento.setTipo(TipoMovimiento.CREDITO);
        movimiento.setDescripcionBreve("Deposito de dinero");
        movimientoDao.save(movimiento);

        return new OperacionRespuesta("EXITOSA", "Se depositaron " + cuenta.getMoneda() + "$ " + movimiento.getMonto());
    }
}

