package com.smartinventory.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="app_users")
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=80) private String username;
    @Column(nullable=false, unique=true, length=160) private String email;
    @Column(nullable=false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private Role role = Role.STAFF;
    @Column(nullable=false) private boolean enabled = true;
    @Column(nullable=false, updatable=false) private Instant createdAt = Instant.now();
    public Long getId(){return id;} public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;}
    public Instant getCreatedAt(){return createdAt;}
}
