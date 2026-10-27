package com.smartinventory.controller;
import com.smartinventory.dto.*; import com.smartinventory.model.*; import com.smartinventory.repository.*; import com.smartinventory.service.InventoryService;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api")
public class InventoryController {
 private final InventoryService service; private final InventoryItemRepository inventory; private final StockMovementRepository movements;
 public InventoryController(InventoryService s,InventoryItemRepository i,StockMovementRepository m){service=s;inventory=i;movements=m;}
 @GetMapping("/inventory") public List<Map<String,Object>> inventory(){return inventory.findAll().stream().map(x->Map.<String,Object>of("id",x.getId(),"productId",x.getProduct().getId(),"productName",x.getProduct().getName(),"sku",x.getProduct().getSku(),"warehouseId",x.getWarehouse().getId(),"warehouseName",x.getWarehouse().getName(),"quantity",x.getQuantity(),"reorderPoint",x.getProduct().getReorderPoint(),"reorderQuantity",x.getProduct().getReorderQuantity(),"lowStock",x.getQuantity()<=x.getProduct().getReorderPoint())).toList();}
 @PostMapping("/inventory/stock-in") public Object stockIn(@Valid @RequestBody StockRequest r){return service.stock(r,MovementType.STOCK_IN);}
 @PostMapping("/inventory/stock-out") public Object stockOut(@Valid @RequestBody StockRequest r){return service.stock(r,MovementType.STOCK_OUT);}
 @PostMapping("/inventory/return") public Object stockReturn(@Valid @RequestBody StockRequest r){return service.stock(r,MovementType.RETURN);}
 @PostMapping("/inventory/transfer") public Object transfer(@Valid @RequestBody TransferRequest r){return service.transfer(r);}
 @GetMapping("/movements") public List<Map<String,Object>> movements(){return movements.findTop200ByOrderByCreatedAtDesc().stream().map(m->{Map<String,Object>x=new LinkedHashMap<>();x.put("id",m.getId());x.put("productId",m.getProduct().getId());x.put("productName",m.getProduct().getName());x.put("sku",m.getProduct().getSku());x.put("warehouseId",m.getWarehouse().getId());x.put("warehouseName",m.getWarehouse().getName());x.put("movementType",m.getMovementType());x.put("quantity",m.getQuantity());x.put("notes",m.getNotes());x.put("reference",m.getReference());x.put("createdAt",m.getCreatedAt());return x;}).toList();}
}
