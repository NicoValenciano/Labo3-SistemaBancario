package ar.edu.utn.frbb.tup.model.exception;

public class CuentaWithoutSufficientFundsException extends Throwable {
    public CuentaWithoutSufficientFundsException(String message) {
        super(message);
    }
}