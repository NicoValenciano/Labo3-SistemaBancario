package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Random;

public class Movimiento {
    @JsonIgnore
    private long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime fecha;
    private TipoMovimiento tipo;
    private long numeroCuenta;
    private double monto;
    private String descripcionBreve;


    public Movimiento() {
        this.id = Math.abs(new Random().nextLong());
        this.fecha = LocalDateTime.now();
    }
    public Movimiento(MovimientoDto movimientoDto) {
        this.id = Math.abs(new Random().nextLong());
        this.fecha = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public Movimiento setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
        return this;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

}
