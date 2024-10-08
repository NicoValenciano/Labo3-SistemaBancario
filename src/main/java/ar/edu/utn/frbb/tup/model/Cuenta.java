package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cuenta {
    private long numeroCuenta;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDateTime fechaCreacion;
    double balance;
    TipoCuenta tipoCuenta;
    long titular;
    TipoMoneda moneda;
    @JsonIgnore
    private Set<Movimiento> movimientos = new HashSet<>();

    public Cuenta() {
        this.numeroCuenta = Math.abs(new Random().nextLong());
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Cuenta(CuentaDto cuentaDto) throws TipoCuentaNotSupportedException {
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 0;
        this.numeroCuenta = Math.abs(new Random().nextLong());
    }

    public long getTitular() {
        return titular;
    }

    public void setTitular(long titular) {
        this.titular = titular;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void addMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
        movimiento.setNumeroCuenta(this.getNumeroCuenta());
    }

}