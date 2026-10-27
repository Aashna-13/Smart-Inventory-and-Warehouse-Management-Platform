package com.smartinventory.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="inventory_items",uniqueConstraints=@UniqueConstraint(columnNames={"product_id","warehouse_id"}))
public class InventoryItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="product_id") private Product product;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="warehouse_id") private Warehouse warehouse;
 @Column(nullable=false) private int quantity=0;
 @Column(nullable=false) private Instant updatedAt=Instant.now();
 @Version private long version;
 public Long getId(){return id;} public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
 public Warehouse getWarehouse(){return warehouse;} public void setWarehouse(Warehouse v){warehouse=v;}
 public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;updatedAt=Instant.now();}
 public Instant getUpdatedAt(){return updatedAt;}
}
