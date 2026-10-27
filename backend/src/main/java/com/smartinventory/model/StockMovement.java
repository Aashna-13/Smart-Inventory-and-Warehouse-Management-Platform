package com.smartinventory.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="stock_movements")
public class StockMovement {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="product_id") private Product product;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="warehouse_id") private Warehouse warehouse;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private MovementType movementType;
 @Column(nullable=false) private int quantity;
 @Column(length=500) private String notes;
 @Column(length=100) private String reference;
 @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="created_by") private AppUser createdBy;
 public Long getId(){return id;} public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
 public Warehouse getWarehouse(){return warehouse;} public void setWarehouse(Warehouse v){warehouse=v;}
 public MovementType getMovementType(){return movementType;} public void setMovementType(MovementType v){movementType=v;}
 public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
 public String getReference(){return reference;} public void setReference(String v){reference=v;}
 public Instant getCreatedAt(){return createdAt;} public AppUser getCreatedBy(){return createdBy;} public void setCreatedBy(AppUser v){createdBy=v;}
}
