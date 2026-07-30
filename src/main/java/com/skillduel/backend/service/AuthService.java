package com.skillduel.backend.service;

import com.skillduel.backend.dto.AuthResponse;
import com.skillduel.backend.dto.LoginRequest;
import com.skillduel.backend.dto.RegisterRequest;
import com.skillduel.backend.exception.ErrorMessages;
import com.skillduel.backend.model.User;
import com.skillduel.backend.repository.UserRepository;
import com.skillduel.backend.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request){
        User user=new User();
        String encodedPassword=passwordEncoder.encode(request.getPassword());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }

    public AuthResponse login(LoginRequest request){
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new BadCredentialsException(ErrorMessages.INVALID_CREDENTIALS));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BadCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
        }
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }
}
