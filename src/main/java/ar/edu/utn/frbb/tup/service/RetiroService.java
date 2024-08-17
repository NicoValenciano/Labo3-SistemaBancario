package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.RetiroServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetiroService implements RetiroServiceInterface {

    @Autowired
    CuentaDao cuentaDao;

    @Autowired
    MovimientoDao movimientoDao;


    public OperacionRespuesta hacerRetiro(long idCuenta, MovimientoDto movimientoDto) throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {

        //Validamos que la cuenta exista
        if(cuentaDao.find(idCuenta) == null) {
            throw new CuentaNotExistsException("La cuenta " + idCuenta + " no existe.");
        }
        //Validamos que la cuenta opera con la moneda con la que se intenta operar
        if (!(cuentaDao.find(idCuenta).getMoneda().equals(TipoMoneda.fromString(movimientoDto.getMoneda())))) {
            throw new TipoMonedaIncompatibleException("La cuenta " + idCuenta + " no opera con la moneda con la que se intenta operar");
        }
        //Validamos que la cuenta tenga fondos suficientes
        if (movimientoDto.getMonto() > cuentaDao.find(idCuenta).getBalance()) {
            throw new CuentaWithoutSufficientFundsException("La cuenta " + idCuenta + " no tiene suficiente fondos.");
        }

        //Se hace el retiro y se actualiza la cuenta
        Cuenta cuenta = cuentaDao.find(idCuenta);
        cuenta.setBalance(cuenta.getBalance() - movimientoDto.getMonto());
        cuentaDao.save(cuenta);


        //Se registra el movimiento
        Movimiento movimiento = new Movimiento(movimientoDto);
        movimiento.setNumeroCuenta(idCuenta);
        movimiento.setMonto(movimientoDto.getMonto());
        movimiento.setTipo(TipoMovimiento.DEBITO);
        movimiento.setDescripcionBreve("Retiro de dinero");
        movimientoDao.save(movimiento);

        return new OperacionRespuesta("EXITOSA", "Se retiraron " + cuenta.getMoneda() + "$ " + movimiento.getMonto());
        }

    }
