package com.justsayozz.shop.Models.API;

/**
 * Created by justs on 12.04.2017.
 */
public class SignInResponse {
    public boolean isAuthenticated;
    public String message;

    public SignInResponse(boolean isAuthenticated, String message) {
        this.isAuthenticated = isAuthenticated;
        this.message = message;
    }
}
