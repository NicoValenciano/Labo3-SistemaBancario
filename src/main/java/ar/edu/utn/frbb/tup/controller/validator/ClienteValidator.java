package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ClienteValidator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validate(ClienteDto clienteDto) throws InputErrorException {
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        validarString(clienteDto.getNombre(), "Nombre");
        validarString(clienteDto.getApellido(), "Apellido");
        validarString(clienteDto.getBanco(), "Banco");
        validarDni(clienteDto.getDni());
        validarFechaNacimiento(clienteDto.getFechaNacimiento());
        validarTipoPersona(clienteDto.getTipoPersona());
    }

    public void validarString(String string, String campo) throws InputErrorException {
        if (string == null) {
            throw new InputErrorException("El campo " + campo + " ingresado no puede ser nulo");
        }

        if (!string.matches("[a-zA-Z]+")) {
            throw new InputErrorException("El " + campo + " ingresado no es valido.");
        }
    }

    public void validarDni(long dni) throws InputErrorException {
        String dniString = String.valueOf(dni);
        if (dniString.length() < 7 || dniString.length() > 8) {
            throw new InputErrorException("El DNI ingresado no es valido");
        }

        if (!dniString.matches("\\d{7,8}")) {
            throw new InputErrorException("El DNI ingresado no es valido");
        }
    }

    public void validarFechaNacimiento(String fechaNacimientoString) {
        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoString, DATE_FORMATTER);
            LocalDate fechaActual = LocalDate.now();

            if (fechaNacimiento.isAfter(fechaActual)) {
                throw new InputErrorException("La FECHA DE NACIMIENTO ingresada no es valida.");
            }

        } catch (DateTimeParseException | InputErrorException e) {
            throw new IllegalArgumentException("La FECHA DE NACIMIENTO ingresada no es valida.");
        }
    }

    public void validarTipoPersona(String tipoPersona){

        if (!("F".equalsIgnoreCase(tipoPersona) || "J".equalsIgnoreCase(tipoPersona))) {
            throw new IllegalArgumentException("El tipo de persona no es correcto");
        }
    }
}

