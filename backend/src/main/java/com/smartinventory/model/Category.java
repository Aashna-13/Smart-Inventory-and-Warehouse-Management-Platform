package com.smartinventory.model;

import jakarta.persistence.*;
@Entity @Table(name="categories")
public class Category {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=100) private String name;
 @Column(length=500) private String description;
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
}
