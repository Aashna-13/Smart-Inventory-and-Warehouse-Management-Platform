package com.smartinventory.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="products")
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=80) private String sku;
 @Column(nullable=false,length=180) private String name;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="category_id") private Category category;
 @Column(length=1000) private String description;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal unitPrice=BigDecimal.ZERO;
 @Column(nullable=false) private int reorderPoint=0;
 @Column(nullable=false) private int reorderQuantity=1;
 @Column(nullable=false) private boolean active=true;
 public Long getId(){return id;} public String getSku(){return sku;} public void setSku(String v){sku=v;}
 public String getName(){return name;} public void setName(String v){name=v;}
 public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
 public int getReorderPoint(){return reorderPoint;} public void setReorderPoint(int v){reorderPoint=v;}
 public int getReorderQuantity(){return reorderQuantity;} public void setReorderQuantity(int v){reorderQuantity=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
