package com.smartinventory.model;
import jakarta.persistence.*;
@Entity @Table(name="warehouses")
public class Warehouse {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=120) private String name;
 @Column(nullable=false,length=250) private String location;
 @Column(nullable=false) private long capacity=0;
 @Column(nullable=false) private boolean active=true;
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
 public String getLocation(){return location;} public void setLocation(String v){location=v;}
 public long getCapacity(){return capacity;} public void setCapacity(long v){capacity=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
