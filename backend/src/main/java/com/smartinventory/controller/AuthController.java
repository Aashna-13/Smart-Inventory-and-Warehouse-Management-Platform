package com.smartinventory.controller;
import com.smartinventory.dto.LoginRequest; import com.smartinventory.service.AuthService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service; public AuthController(AuthService s){service=s;}
 @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody LoginRequest req){try{return service.login(req);}catch(IllegalArgumentException e){throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());}}
 @GetMapping("/me") public Map<String,Object> me(Authentication a){return Map.of("username",a.getName());}
}
