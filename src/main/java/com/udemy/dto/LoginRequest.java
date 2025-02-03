package com.udemy.dto;

import lombok.Data;

@Data // getters setters constructores

public class LoginRequest {
    private String username;
    private String password;
}
