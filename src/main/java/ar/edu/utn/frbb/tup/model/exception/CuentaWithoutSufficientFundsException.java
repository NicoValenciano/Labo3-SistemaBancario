package ar.edu.utn.frbb.tup.model.exception;

public class CuentaWithoutSufficientFundsException extends Exception {
    public CuentaWithoutSufficientFundsException(String message) {
        super(message);
    }
}