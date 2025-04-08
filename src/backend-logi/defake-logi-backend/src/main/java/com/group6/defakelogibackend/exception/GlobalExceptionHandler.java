package com.group6.defakelogibackend.exception;

import com.group6.defakelogibackend.model.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationFailedException.class)
    public Result handleException(AuthenticationFailedException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public Result handleException(EntityDuplicateException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(FieldMissingException.class)
    public Result handleException(FieldMissingException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(EntityMissingException.class)
    public Result handleException(EntityMissingException exception) {
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(FileHandleException.class)
    public Result handleException(FileHandleException exception) {
        return Result.error(exception.getMessage());
    }
}
