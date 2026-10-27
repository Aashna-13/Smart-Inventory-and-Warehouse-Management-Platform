package com.smartinventory.service;

import com.smartinventory.model.*; import com.smartinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;
@Configuration
public class BootstrapAdmin {
 @Bean CommandLineRunner ensureAdmin(UserRepository users,PasswordEncoder encoder,
   @Value("${app.bootstrap.admin-username:admin}") String username,
   @Value("${app.bootstrap.admin-email:admin@example.local}") String email,
   @Value("${app.bootstrap.admin-password:}") String configuredPassword) {
  return args->{ if(users.count()==0){String password=configuredPassword;
   if(password==null||password.isBlank()){byte[] b=new byte[18];new SecureRandom().nextBytes(b);password=java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(b);System.out.println("BOOTSTRAP ADMIN PASSWORD (save it now): "+password);}
   AppUser u=new AppUser();u.setUsername(username);u.setEmail(email);u.setPasswordHash(encoder.encode(password));u.setRole(Role.ADMIN);users.save(u);
  }};
 }
}
