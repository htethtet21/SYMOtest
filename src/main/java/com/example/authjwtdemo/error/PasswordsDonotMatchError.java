package com.example.authjwtdemo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordsDonotMatchError extends ResponseStatusException {
    public PasswordsDonotMatchError() {
        super(HttpStatus.BAD_REQUEST,"Passwords don't Match");
    }
}
