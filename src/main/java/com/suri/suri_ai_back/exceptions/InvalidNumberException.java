package com.suri.suri_ai_back.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidNumberException extends RuntimeException {
    public InvalidNumberException(String message) {
        super(message);
    }   
    
}
