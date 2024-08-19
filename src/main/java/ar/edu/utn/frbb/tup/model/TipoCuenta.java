package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;

public enum TipoCuenta {

    CUENTA_CORRIENTE ("CC"),
    CAJA_AHORRO ("CA");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoCuenta fromString(String text) throws TipoCuentaNotSupportedException {
        for (TipoCuenta tipo : TipoCuenta.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new TipoCuentaNotSupportedException("No se pudo encontrar un TipoCuenta con la descripción: " + text);
    }
}