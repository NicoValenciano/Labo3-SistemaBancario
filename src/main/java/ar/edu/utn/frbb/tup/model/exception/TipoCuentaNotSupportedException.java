package ar.edu.utn.frbb.tup.model.exception;

public class TipoCuentaNotSupportedException extends Exception {
    public TipoCuentaNotSupportedException(String message) {
        super(message);
    }

    public static class CantidadNegativaException extends Throwable {
    }

    public static class NoAlcanzaException extends Throwable {
    }
}
