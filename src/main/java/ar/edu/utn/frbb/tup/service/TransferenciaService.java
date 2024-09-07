package ar.edu.utn.frbb.tup.service;


import ar.edu.utn.frbb.tup.controller.OperacionRespuesta;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaWithoutSufficientFundsException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.TransferenciaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaService implements TransferenciaServiceInterface {
    @Autowired
    ClienteDao clienteDao;

    @Autowired
    CuentaDao cuentaDao;

    @Autowired
    MovimientoDao movimientoDao;

    //Aclaración: se considera que el sistema puede conocer los datos de los clientes, independientemente de que banco sean.
    //podemos presuponer que el sistema es capaz de obtener los datos de los clientes al consultar la capa de persistencia.

    public OperacionRespuesta hacerTransferencia(MovimientoDto movimientoDto) throws CuentaNotExistsException, CuentaWithoutSufficientFundsException, TipoMonedaIncompatibleException {
        Cuenta cuentaOrigen;
        Cuenta cuentaDestino;

        //Validamos que la cuenta origen exista
        if(cuentaDao.find(movimientoDto.getCuentaOrigen()) == null) {
            throw new CuentaNotExistsException("La cuenta " + movimientoDto.getCuentaOrigen() + " no existe.");
        } else {
            cuentaOrigen = cuentaDao.find(movimientoDto.getCuentaOrigen());
        }
        //Validamos que la cuenta destino exista
        if (cuentaDao.find(movimientoDto.getCuentaDestino()) == null) {
            throw new CuentaNotExistsException("La cuenta " + movimientoDto.getCuentaDestino() + " no existe.");
        } else{
            cuentaDestino = cuentaDao.find(movimientoDto.getCuentaDestino());
        }
        //Validamos que la cuenta origen opera con la moneda con la que se intenta operar
        if (!(cuentaOrigen.getMoneda().equals(TipoMoneda.fromString(movimientoDto.getMoneda())))) {
            throw new TipoMonedaIncompatibleException("La cuenta " + movimientoDto.getCuentaOrigen() + " no opera con la moneda con la que se intenta operar");
        }
        //Validamos que la cuenta destino opera con la moneda con la que se intenta operar
        if (!(cuentaDestino.getMoneda().equals(TipoMoneda.fromString(movimientoDto.getMoneda())))) {
            throw new TipoMonedaIncompatibleException("La cuenta " + movimientoDto.getCuentaDestino() + " no opera con la moneda con la que se intenta operar");
        }
        //Validamos que la cuenta origen tenga fondos suficientes
        if (movimientoDto.getMonto() > cuentaOrigen.getBalance()) {
            throw new CuentaWithoutSufficientFundsException("La cuenta " + movimientoDto.getCuentaOrigen() + " no tiene suficiente fondos.");
        }

        Cliente titularOrigen = clienteDao.find(cuentaOrigen.getTitular(), true);
        Cliente titularDestino = clienteDao.find(cuentaDestino.getTitular(), true);

        // Evaluamos si la transferencia es posible
        // Si la cuenta origen y la cuenta destino pertenecen al mismo banco, la transferencia es posible.
        // Si la cuenta origen y la cuenta destino pertenecen a bancos diferentes, la transferencia ES POSIBLE si el monto a transferir NO es divisible por 3.
        if (!(titularOrigen.getBanco().equals(titularDestino.getBanco()) && BanelcoService.aprobarTransaccion(movimientoDto.getMonto()))) {
            return new OperacionRespuesta("FALLIDA", "Transferencia fallida.");
        } else {
            //Realizamos la transferencia y actualizamos las cuentas
            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - movimientoDto.getMonto());
            cuentaDao.save(cuentaOrigen);
            cuentaDestino.setBalance(cuentaDestino.getBalance() + movimientoDto.getMonto());
            cuentaDao.save(cuentaDestino);

            //Creamos el movimiento de la cuenta origen
            Movimiento movimientoOrigen = new Movimiento(movimientoDto);
            movimientoOrigen.setNumeroCuenta(movimientoDto.getCuentaOrigen());
            movimientoOrigen.setMonto(movimientoDto.getMonto());
            movimientoOrigen.setTipo(TipoMovimiento.DEBITO);
            movimientoOrigen.setDescripcionBreve("Transferencia saliente");
            movimientoDao.save(movimientoOrigen);

            //Creamos el movimiento de la cuenta destino
            Movimiento movimientoDestino = new Movimiento(movimientoDto);
            movimientoDestino.setNumeroCuenta(movimientoDto.getCuentaDestino());
            movimientoDestino.setMonto(movimientoDto.getMonto());
            movimientoDestino.setTipo(TipoMovimiento.CREDITO);
            movimientoDestino.setDescripcionBreve("Transferencia entrante");
            movimientoDao.save(movimientoDestino);

            return new OperacionRespuesta("EXITOSA", "Transferencia exitosa");
        }
    }
}
