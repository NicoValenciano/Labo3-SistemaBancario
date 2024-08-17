package ar.edu.utn.frbb.tup.controller.validator;
import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;



@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) {
        validateTipoCuenta(cuentaDto);
        validateMoneda(cuentaDto);
        validateTitular(cuentaDto);
    }

    private void validateTipoCuenta(CuentaDto cuentaDto) {
        if (!("CC".equals(cuentaDto.getTipoCuenta()) || "CA".equals(cuentaDto.getTipoCuenta()))) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
    }

    private void validateMoneda(CuentaDto cuentaDto) {
       if (!("ARS".equals(cuentaDto.getMoneda()) || ("USD".equals(cuentaDto.getMoneda())))){
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }

    private void validateTitular(CuentaDto cuentaDto) {
        if (cuentaDto.getdniTitular() == null || cuentaDto.getdniTitular() < 0) {
            throw new IllegalArgumentException("El numero de cuenta debe existir y ser mayor a 0");
        }
    }
}