package com.smartinventory.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity @Table(name="purchase_orders")
public class PurchaseOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=80) private String orderNumber;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="supplier_id") private Supplier supplier;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="warehouse_id") private Warehouse warehouse;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private OrderStatus status=OrderStatus.PENDING;
 @Column(length=500) private String notes;
 @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
 @OneToMany(mappedBy="purchaseOrder",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) private List<PurchaseOrderItem> items=new ArrayList<>();
 public Long getId(){return id;} public String getOrderNumber(){return orderNumber;} public void setOrderNumber(String v){orderNumber=v;}
 public Supplier getSupplier(){return supplier;} public void setSupplier(Supplier v){supplier=v;}
 public Warehouse getWarehouse(){return warehouse;} public void setWarehouse(Warehouse v){warehouse=v;}
 public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus v){status=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
 public Instant getCreatedAt(){return createdAt;} public List<PurchaseOrderItem> getItems(){return items;}
 public void addItem(PurchaseOrderItem item){items.add(item);item.setPurchaseOrder(this);}
 public BigDecimal getTotal(){return items.stream().map(i->i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);}
}
