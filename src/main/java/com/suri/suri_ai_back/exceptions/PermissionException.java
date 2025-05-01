package com.suri.suri_ai_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PermissionException extends RuntimeException{
    public PermissionException(String message) {
        super(message);
    }
}
