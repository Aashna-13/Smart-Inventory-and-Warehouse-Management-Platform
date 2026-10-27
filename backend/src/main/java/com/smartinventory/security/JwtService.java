package com.smartinventory.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
 private final SecretKey key; private final long expirationMs;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expirationMs){
  if(secret.getBytes(StandardCharsets.UTF_8).length<32) throw new IllegalArgumentException("app.jwt.secret must contain at least 32 bytes");
  this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expirationMs=expirationMs;
 }
 public String create(String username,String role){long now=System.currentTimeMillis(); return Jwts.builder().subject(username).claim("role",role).issuedAt(new Date(now)).expiration(new Date(now+expirationMs)).signWith(key).compact();}
 public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
