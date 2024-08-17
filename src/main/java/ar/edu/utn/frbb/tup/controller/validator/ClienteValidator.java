package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteValidator {


    public void validate(ClienteDto clienteDto) {
        LocalDate fechaActual = LocalDate.now();
        if (!("F".equalsIgnoreCase(clienteDto.getTipoPersona()) || "J".equalsIgnoreCase(clienteDto.getTipoPersona()))) {
            throw new IllegalArgumentException("El tipo de persona no es correcto");
        }
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error en el formato de fecha");
        }
        if (clienteDto.getTipoPersona().isEmpty() || clienteDto.getTipoPersona() == null) {
            throw new IllegalArgumentException("Propiedad tipoPersona no puede ser nula o estar vacia");}

        if (clienteDto.getFechaNacimiento().isEmpty() || clienteDto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("Propiedad fechaNacimiento no puede ser nula o estar vacia");
        } else if (LocalDate.parse(clienteDto.getFechaNacimiento()).isAfter(fechaActual)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede estar en el futuro");}

        if (clienteDto.getBanco().isEmpty() || clienteDto.getBanco() == null) {
            throw new IllegalArgumentException("Propiedad banco no puede ser nula o estar vacia");
        }
        if (clienteDto.getApellido().isEmpty() || clienteDto.getApellido() == null) {
            throw new IllegalArgumentException("Propiedad apellido no puede ser nula o estar vacia");
        }
        if (clienteDto.getNombre().isEmpty() || clienteDto.getNombre() == null) {
            throw new IllegalArgumentException("Propiedad nombre no puede ser nula o estar vacia");
        }
        if (clienteDto.getDni() == null) {
            throw new IllegalArgumentException("Propiedad dni no puede ser nula o estar vacia");
        }
    }
}