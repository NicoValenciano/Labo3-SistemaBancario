package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.serviceInterface.CuentaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CuentaService implements CuentaServiceInterface {
   // CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    @Autowired
    CuentaDao cuentaDao;

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNotSupportedException, ClienteNotExistsException {
        Cuenta cuenta = new Cuenta(cuentaDto);

        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        //Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNotSupportedException("La cuenta " + cuenta.getNumeroCuenta() + " no es soportada por el banco");
        }

        clienteService.agregarCuenta(cuenta, cuentaDto.getdniTitular());
        cuentaDao.save(cuenta);
        return cuenta;
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }

    public List<Cuenta> getCuentasByCliente(long dni) throws ClienteNotExistsException {
        clienteService.buscarClientePorDni(dni);
        return cuentaDao.getCuentasByCliente(dni);
    }

    public boolean tipoCuentaEstaSoportada(Cuenta cuenta){
        String tipoCuenta = String.valueOf(cuenta.getTipoCuenta());
        String moneda = String.valueOf(cuenta.getMoneda());
        boolean soportada = false;
        if (tipoCuenta.equals("CAJA_AHORRO") && moneda.equals("USD"))
            soportada = true;
        if (tipoCuenta.equals("CAJA_AHORRO") && moneda.equals("PESOS"))
            soportada = true;
        if (tipoCuenta.equals("CUENTA_CORRIENTE") && moneda.equals("PESOS"))
            soportada = true;
        return soportada;
    }

    public void eliminarCuenta(long id) {
        cuentaDao.delete(id);
    }
}
