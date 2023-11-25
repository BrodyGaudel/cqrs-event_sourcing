package com.brodygaudel.ebank.query.exception;

public class BalanceNotSufficientException extends RuntimeException{
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
