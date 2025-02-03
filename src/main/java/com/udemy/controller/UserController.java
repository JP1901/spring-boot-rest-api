package com.udemy.controller;

import com.udemy.dto.LoginRequest;
import com.udemy.dto.LoginResponse;
import com.udemy.dto.UserRequest;
import com.udemy.dto.UserResponse;
import com.udemy.services.UserService;
import com.udemy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    private final JWTUtil jwtUtil = new JWTUtil();

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse loginResponse;
            loginResponse = this.userService.login(loginRequest);

            return ResponseEntity.ok(loginResponse);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el login: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String jwt){
        try{
            if(!this.jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if(!this.jwtUtil.verifyRole(jwt,"SUPER")){
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }


            UserResponse userResponse;
            userResponse = this.userService.create(userRequest);

            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());

        }
    }





}
