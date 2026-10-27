package com.smartinventory.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="purchase_order_items")
public class PurchaseOrderItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="purchase_order_id") private PurchaseOrder purchaseOrder;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="product_id") private Product product;
 @Column(nullable=false) private int quantity;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal unitPrice;
 public Long getId(){return id;} public PurchaseOrder getPurchaseOrder(){return purchaseOrder;} public void setPurchaseOrder(PurchaseOrder v){purchaseOrder=v;}
 public Product getProduct(){return product;} public void setProduct(Product v){product=v;} public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
 public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
}
