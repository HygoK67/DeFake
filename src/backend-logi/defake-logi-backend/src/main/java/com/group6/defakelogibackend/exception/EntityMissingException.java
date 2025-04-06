package com.group6.defakelogibackend.exception;

public class EntityMissingException extends RuntimeException {
    public EntityMissingException(String message) {
        super(message);
    }
}
