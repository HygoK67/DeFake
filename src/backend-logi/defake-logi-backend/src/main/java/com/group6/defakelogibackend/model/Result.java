package com.group6.defakelogibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int code;
    private String message;
    private Object data;

    public static Result error(String msg) {
        return new Result(-1, msg, null);
    }

    public static Result success(Object data) {
        return new Result(0, "OK", data);
    }

    public static Result success() {
        return new Result(0, "OK", null);
    }

}
