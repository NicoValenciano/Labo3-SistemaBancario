package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleClienteAlreadyExistsException(ClienteAlreadyExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1001);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TipoCuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleTipoCuentaAlreadyExistsException(TipoCuentaAlreadyExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1002);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleCuentaAlreadyExistsException(CuentaAlreadyExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1003);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InputErrorException.class)
    public ResponseEntity<CustomApiError> handleInputErrorException(InputErrorException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1004);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClienteNotExistsException.class)
    public ResponseEntity<CustomApiError> handleClienteNotExistsException(ClienteNotExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(3001);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CuentaNotExistsException.class)
    public ResponseEntity<CustomApiError> handleCuentaNotExistsException(CuentaNotExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(3002);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovimientoNotExistsException.class)
    public ResponseEntity<CustomApiError> handleMovimientoNotExistsException(MovimientoNotExistsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(3003);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CuentaWithoutSufficientFundsException.class)
    public ResponseEntity<CustomApiError> handleCuentaWithoutSufficientFundsException(CuentaWithoutSufficientFundsException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(5001);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TipoCuentaNotSupportedException.class)
    public ResponseEntity<CustomApiError> handleTipoCuentaNotSupportedException(TipoCuentaNotSupportedException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(5002);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TipoMonedaIncompatibleException.class)
    public ResponseEntity<CustomApiError> handleTipoMonedaIncompatibleException(TipoMonedaIncompatibleException ex) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(5003);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


}
