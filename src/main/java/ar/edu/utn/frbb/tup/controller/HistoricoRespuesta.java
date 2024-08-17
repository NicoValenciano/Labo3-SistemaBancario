package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Movimiento;


import java.util.List;


public class HistoricoRespuesta {
    private long numeroCuenta;
    private List<Movimiento> transferencias;

    public HistoricoRespuesta(long numeroCuenta, List<Movimiento> movimientos) {
        this.numeroCuenta = numeroCuenta;
        this.transferencias = movimientos;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public List<Movimiento> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<Movimiento> transferencias) {
        this.transferencias = transferencias;
    }
}
