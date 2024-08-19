package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona{

    private TipoPersona tipoPersona;
    private String banco;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime fechaAlta;
    @JsonIgnore
    private Set<Cuenta> cuentas = new HashSet<>();

    public Cliente() {
        super();
    }
    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento());
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
        fechaAlta = LocalDateTime.now();
        banco = clienteDto.getBanco();
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setTitular(this.getDni());
    }

    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        for (Cuenta cuenta:
                cuentas) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
                return true;
            }
        }
        return false;
    }

}