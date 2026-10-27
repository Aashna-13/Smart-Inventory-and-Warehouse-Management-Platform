package com.smartinventory.service;

import com.smartinventory.dto.StockRequest; import com.smartinventory.dto.TransferRequest;
import com.smartinventory.model.*; import com.smartinventory.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventoryService {
 private final InventoryItemRepository inventory; private final ProductRepository products; private final WarehouseRepository warehouses; private final StockMovementRepository movements;
 public InventoryService(InventoryItemRepository i,ProductRepository p,WarehouseRepository w,StockMovementRepository m){inventory=i;products=p;warehouses=w;movements=m;}
 public List<InventoryItem> all(){return inventory.findAll();}
 @Transactional public InventoryItem stock(StockRequest r,MovementType type){
  if(r.quantity()<1)throw new IllegalArgumentException("Quantity must be at least 1");
  Product p=products.findById(r.productId()).orElseThrow(()->new IllegalArgumentException("Product not found"));
  Warehouse w=warehouses.findById(r.warehouseId()).orElseThrow(()->new IllegalArgumentException("Warehouse not found"));
  InventoryItem item=inventory.findByProductIdAndWarehouseId(p.getId(),w.getId()).orElseGet(()->{InventoryItem x=new InventoryItem();x.setProduct(p);x.setWarehouse(w);x.setQuantity(0);return x;});
  int delta=type==MovementType.STOCK_OUT?-r.quantity():r.quantity();
  if(item.getQuantity()+delta<0)throw new IllegalArgumentException("Insufficient stock");
  item.setQuantity(item.getQuantity()+delta); inventory.save(item);
  StockMovement m=new StockMovement();m.setProduct(p);m.setWarehouse(w);m.setMovementType(type);m.setQuantity(r.quantity());m.setNotes(r.notes());movements.save(m);
  return item;
 }
 @Transactional public InventoryItem transfer(TransferRequest r){
  if(r.sourceWarehouseId().equals(r.destinationWarehouseId()))throw new IllegalArgumentException("Source and destination warehouses must differ");
  StockRequest out=new StockRequest(r.productId(),r.sourceWarehouseId(),r.quantity(),r.notes());
  stock(out,MovementType.STOCK_OUT);
  StockRequest in=new StockRequest(r.productId(),r.destinationWarehouseId(),r.quantity(),r.notes());
  return stock(in,MovementType.TRANSFER);
 }
}
