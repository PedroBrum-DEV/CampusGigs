package br.com.fiap.campusgigs.exception;

public class CepServiceUnavailableException extends RuntimeException {
    public CepServiceUnavailableException(String message) {
        super(message);
    }
}