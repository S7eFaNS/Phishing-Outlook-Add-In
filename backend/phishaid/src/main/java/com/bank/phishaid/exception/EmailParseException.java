package com.bank.phishaid.exception;

//parsing of raw .eml failure

public class EmailParseException extends RuntimeException {
    public EmailParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
