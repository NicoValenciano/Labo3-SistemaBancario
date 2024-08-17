package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimientoEntity  extends BaseEntity {
    LocalDateTime fecha;
    long id;
    String tipo;
    long numeroCuenta;
    String descripcionBreve;
    double monto;


    public MovimientoEntity(Movimiento movimiento) {
        super(movimiento.getId());
        this.fecha = movimiento.getFecha();
        this.tipo = (movimiento.getTipo() != null) ? movimiento.getTipo().toString() : null;
        this.numeroCuenta = movimiento.getNumeroCuenta();
        this.monto = movimiento.getMonto();
        this.descripcionBreve = movimiento.getDescripcionBreve();
    }

    public Movimiento toMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(this.getId());
        movimiento.setFecha(this.fecha);
        movimiento.setTipo(TipoMovimiento.valueOf(this.tipo));
        movimiento.setNumeroCuenta(this.numeroCuenta);
        movimiento.setDescripcionBreve(this.descripcionBreve);
        movimiento.setMonto(this.monto);
        return movimiento;
    }

    public LocalDateTime getFecha() {return fecha;}

    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getNumeroCuenta() {return numeroCuenta;}

    public void setNumeroCuenta(long numeroCuenta) {this.numeroCuenta = numeroCuenta;}

    public double getMonto() {return monto;}

    public void setMonto(double monto) {this.monto = monto;}

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }
}
