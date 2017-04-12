package com.justsayozz.shop.Models;

/**
 * Created by justs on 12.04.2017.
 */
public class DatabaseResult<T> {
    public T data;
    public String message;

    public DatabaseResult() {

    }

    public DatabaseResult(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
