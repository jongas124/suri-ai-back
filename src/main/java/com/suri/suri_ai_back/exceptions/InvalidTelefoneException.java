package com.suri.suri_ai_back.exceptions;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTelefoneException extends RuntimeException {
    public InvalidTelefoneException(String message) {
        super(message);
    }
    
}
