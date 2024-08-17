package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaIncompatibleException;

public class OperacionValidator {

    public void validateIdCuenta(long idCuenta) {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("El id de la cuenta debe ser mayor a 0");
        }
    }

    public void validateOperacion(MovimientoDto movimientoDto) throws TipoMonedaIncompatibleException {
        double monto =  0.0;
        monto += movimientoDto.getMonto();

        if (movimientoDto.getMonto() <= 0 || movimientoDto.getMonto() == null) {
            throw new IllegalArgumentException("El monto de la operación debe existir y ser mayor a 0");
        }
        if (movimientoDto.getMoneda().isEmpty() || movimientoDto.getMoneda() == null) {
            throw new IllegalArgumentException("Propiedad moneda no puede estar vacia o ser nula");
        }else if (!((movimientoDto.getMoneda().equals("ARS") || ((movimientoDto.getMoneda().equals("USD")))))) {
            throw new TipoMonedaIncompatibleException("El tipo de moneda no es correcto");

        }
    }
}
