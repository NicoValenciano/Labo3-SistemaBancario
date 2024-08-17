package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

@Component
public class ConsultarSaldoValidator {
    public void validate(long idCuenta) {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("El id de la cuenta debe ser mayor a 0");
        }
    }
}
