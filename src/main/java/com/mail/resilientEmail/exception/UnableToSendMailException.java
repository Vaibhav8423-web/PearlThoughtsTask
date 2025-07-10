package com.mail.resilientEmail.exception;

public class UnableToSendMailException extends RuntimeException{

    public UnableToSendMailException(String message){
        super(message);
    }
}
