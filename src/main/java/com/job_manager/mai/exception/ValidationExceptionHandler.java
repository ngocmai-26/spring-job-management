package com.job_manager.mai.exception;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.util.ApiResponseHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleValidExceptions(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            Map<String,String> errors = new HashMap<>();
            for (ObjectError error : ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()) {
                String errorMessage = error.getDefaultMessage();
                String columnError = ((FieldError) error).getField();
                errors.put(columnError,errorMessage);
            }
            return ApiResponseHelper.invalid(errors);
        }
        if (ex instanceof BadCredentialsException) {
            return ApiResponseHelper.unAuthorized();
        }
        if (ex instanceof AccessDeniedException) {
            return ApiResponseHelper.accessDenied();
        }
        if (ex instanceof SignatureException) {
            return ApiResponseHelper.signature();
        }
        if (ex instanceof ExpiredJwtException) {
            return ApiResponseHelper.authTokenExpired();
        }
        if (ex instanceof NoHandlerFoundException) {
            return ApiResponseHelper.notFound(Messages.NOT_FOUND_RESOURCE);
        }
        return ApiResponseHelper.fallback(ex);
    }
}
