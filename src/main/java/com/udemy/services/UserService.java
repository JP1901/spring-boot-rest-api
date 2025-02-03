package com.udemy.services;

import com.udemy.dao.UserRepository;
import com.udemy.dto.LoginRequest;
import com.udemy.dto.LoginResponse;
import com.udemy.dto.UserRequest;
import com.udemy.dto.UserResponse;
import com.udemy.model.User;
import com.udemy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new Exception("Credenciales inválidas."));
        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            return new LoginResponse(this.jwtUtil.generateToken(user.getRole()),"Login exitoso.");
        }else{
            throw new Exception("Credenciales inválidas.");
        }
    }

    public UserResponse create(UserRequest userRequest) throws Exception {
        this.userRepository.findByUsername(userRequest.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Username ya registrado.");
        });

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setStatus(true);

        this.userRepository.save(user);

        return new UserResponse(user.getUsername(),user.getRole(),"Usuario creado.");

    }



}
