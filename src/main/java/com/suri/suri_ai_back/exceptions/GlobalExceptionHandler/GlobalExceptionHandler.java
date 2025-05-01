package com.suri.suri_ai_back.exceptions.GlobalExceptionHandler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.suri.suri_ai_back.exceptions.DuplicatedValueException;
import com.suri.suri_ai_back.exceptions.InvalidCPFException;
import com.suri.suri_ai_back.exceptions.InvalidEmailException;
import com.suri.suri_ai_back.exceptions.InvalidNumberException;
import com.suri.suri_ai_back.exceptions.InvalidPasswordException;
import com.suri.suri_ai_back.exceptions.InvalidTelefoneException;
import com.suri.suri_ai_back.exceptions.ObjectNotFoundException;
import com.suri.suri_ai_back.exceptions.PermissionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    @ExceptionHandler(InvalidCPFException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidCPFException(
            InvalidCPFException invalidCPFException,
            WebRequest request) {
        final String errorMessage = invalidCPFException.getMessage();
        log.error(errorMessage, invalidCPFException);
        return buildErrorResponse(
                invalidCPFException,
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidEmailException(
            InvalidEmailException invalidEmailException,
            WebRequest request) {
        final String errorMessage = invalidEmailException.getMessage();
        log.error(errorMessage, invalidEmailException);
        return buildErrorResponse(
                invalidEmailException,
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(InvalidNumberException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleInvalidNumberException(
            InvalidNumberException invalidNumberException,
            WebRequest request) {
        final String errorMessage = invalidNumberException.getMessage();
        log.error(errorMessage, invalidNumberException);
        return buildErrorResponse(
                invalidNumberException,
                errorMessage,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidPasswordException(
            InvalidPasswordException invalidPasswordException,
            WebRequest request) {
        final String errorMessage = invalidPasswordException.getMessage();
        log.error(errorMessage, invalidPasswordException);
        return buildErrorResponse(
                invalidPasswordException,
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(InvalidTelefoneException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidTelefoneException(
            InvalidTelefoneException invalidTelefoneException,
            WebRequest request) {
        final String errorMessage = invalidTelefoneException.getMessage();
        log.error(errorMessage, invalidTelefoneException);
        return buildErrorResponse(
                invalidTelefoneException,
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {
        final String errorMessage = objectNotFoundException.getMessage();
        log.error(errorMessage, objectNotFoundException);
        return buildErrorResponse(
                objectNotFoundException,
                errorMessage,
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(DuplicatedValueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicatedValueException(
            DuplicatedValueException duplicatedValueException,
            WebRequest request) {
        final String errorMessage = duplicatedValueException.getMessage();
        log.error(errorMessage, duplicatedValueException);
        return buildErrorResponse(
                duplicatedValueException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handlePermissionException(
            PermissionException permissionException,
            WebRequest request) {
        final String errorMessage = permissionException.getMessage();
        log.error(errorMessage, permissionException);
        return buildErrorResponse(
                permissionException,
                errorMessage,
                HttpStatus.FORBIDDEN,
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
