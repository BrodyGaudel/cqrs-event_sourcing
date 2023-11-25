package com.brodygaudel.ebank.query.exception;

public class AccountNotActivatedException extends RuntimeException{
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
