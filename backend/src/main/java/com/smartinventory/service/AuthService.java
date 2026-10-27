package com.smartinventory.service;

import com.smartinventory.dto.LoginRequest;
import com.smartinventory.model.AppUser;
import com.smartinventory.repository.UserRepository;
import com.smartinventory.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 public Map<String,Object> login(LoginRequest req){
  AppUser u=users.findByUsernameIgnoreCase(req.usernameOrEmail()).or(()->users.findByEmailIgnoreCase(req.usernameOrEmail())).orElseThrow(()->new IllegalArgumentException("Invalid username/email or password"));
  if(!u.isEnabled()||!encoder.matches(req.password(),u.getPasswordHash()))throw new IllegalArgumentException("Invalid username/email or password");
  return Map.of("accessToken",jwt.create(u.getUsername(),u.getRole().name()),"tokenType","Bearer","username",u.getUsername(),"role",u.getRole().name());
 }
}
