package com.brodygaudel.ebank.command.exception;

public class InvalidObjectIdException extends RuntimeException{
    public InvalidObjectIdException(String message) {
        super(message);
    }
}
