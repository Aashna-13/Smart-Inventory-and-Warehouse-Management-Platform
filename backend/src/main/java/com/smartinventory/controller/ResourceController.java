package com.smartinventory.controller;

import com.smartinventory.model.*;
import com.smartinventory.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class ResourceController {
 private final CategoryRepository categories; private final ProductRepository products; private final WarehouseRepository warehouses; private final SupplierRepository suppliers;
 public ResourceController(CategoryRepository c,ProductRepository p,WarehouseRepository w,SupplierRepository s){categories=c;products=p;warehouses=w;suppliers=s;}
 private Map<String,Object> category(Category x){return Map.of("id",x.getId(),"name",x.getName(),"description",x.getDescription()==null?"":x.getDescription());}
 private Map<String,Object> product(Product x){Map<String,Object> m=new LinkedHashMap<>();m.put("id",x.getId());m.put("sku",x.getSku());m.put("name",x.getName());m.put("description",x.getDescription());m.put("categoryId",x.getCategory()==null?null:x.getCategory().getId());m.put("categoryName",x.getCategory()==null?"":x.getCategory().getName());m.put("unitPrice",x.getUnitPrice());m.put("reorderPoint",x.getReorderPoint());m.put("reorderQuantity",x.getReorderQuantity());m.put("active",x.isActive());return m;}
 private Map<String,Object> warehouse(Warehouse x){return Map.of("id",x.getId(),"name",x.getName(),"location",x.getLocation(),"capacity",x.getCapacity(),"active",x.isActive());}
 private Map<String,Object> supplier(Supplier x){Map<String,Object> m=new LinkedHashMap<>();m.put("id",x.getId());m.put("name",x.getName());m.put("email",x.getEmail());m.put("phone",x.getPhone());m.put("address",x.getAddress());m.put("active",x.isActive());return m;}
 @GetMapping("/api/categories") public List<Map<String,Object>> getCategories(){return categories.findAll().stream().map(this::category).toList();}
 @PostMapping("/api/categories") public Map<String,Object> createCategory(@RequestBody Map<String,Object> b){Category x=new Category();x.setName(str(b,"name"));x.setDescription(str(b,"description"));return category(categories.save(x));}
 @PutMapping("/api/categories/{id}") public Map<String,Object> updateCategory(@PathVariable Long id,@RequestBody Map<String,Object>b){Category x=categories.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));x.setName(str(b,"name"));x.setDescription(str(b,"description"));return category(categories.save(x));}
 @DeleteMapping("/api/categories/{id}") public void deleteCategory(@PathVariable Long id){categories.delete(categories.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 @GetMapping("/api/products") public List<Map<String,Object>> getProducts(@RequestParam(required=false) String name,@RequestParam(required=false) String sku,@RequestParam(required=false) Long categoryId){return products.findAll().stream().filter(p->name==null||p.getName().toLowerCase().contains(name.toLowerCase())).filter(p->sku==null||p.getSku().toLowerCase().contains(sku.toLowerCase())).filter(p->categoryId==null||(p.getCategory()!=null&&p.getCategory().getId().equals(categoryId))).map(this::product).toList();}
 @GetMapping("/api/products/{id}") public Map<String,Object> getProduct(@PathVariable Long id){return product(products.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 @PostMapping("/api/products") public Map<String,Object> createProduct(@RequestBody Map<String,Object>b){Product x=new Product();applyProduct(x,b);return product(products.save(x));}
 @PutMapping("/api/products/{id}") public Map<String,Object> updateProduct(@PathVariable Long id,@RequestBody Map<String,Object>b){Product x=products.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));applyProduct(x,b);return product(products.save(x));}
 @DeleteMapping("/api/products/{id}") public void deleteProduct(@PathVariable Long id){products.delete(products.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 private void applyProduct(Product x,Map<String,Object>b){x.setSku(str(b,"sku"));x.setName(str(b,"name"));x.setDescription(str(b,"description"));x.setUnitPrice(new BigDecimal(String.valueOf(b.getOrDefault("unitPrice","0"))));x.setReorderPoint(integer(b,"reorderPoint",0));x.setReorderQuantity(integer(b,"reorderQuantity",1));if(b.get("categoryId")!=null)x.setCategory(categories.findById(Long.valueOf(String.valueOf(b.get("categoryId")))).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category not found")));x.setActive(!Boolean.FALSE.equals(b.get("active")));}
 @GetMapping("/api/warehouses") public List<Map<String,Object>> getWarehouses(){return warehouses.findAll().stream().map(this::warehouse).toList();}
 @PostMapping("/api/warehouses") public Map<String,Object> createWarehouse(@RequestBody Map<String,Object>b){Warehouse x=new Warehouse();applyWarehouse(x,b);return warehouse(warehouses.save(x));}
 @PutMapping("/api/warehouses/{id}") public Map<String,Object> updateWarehouse(@PathVariable Long id,@RequestBody Map<String,Object>b){Warehouse x=warehouses.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));applyWarehouse(x,b);return warehouse(warehouses.save(x));}
 @DeleteMapping("/api/warehouses/{id}") public void deleteWarehouse(@PathVariable Long id){warehouses.delete(warehouses.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 private void applyWarehouse(Warehouse x,Map<String,Object>b){x.setName(str(b,"name"));x.setLocation(str(b,"location"));x.setCapacity(Long.parseLong(String.valueOf(b.getOrDefault("capacity","0"))));x.setActive(!Boolean.FALSE.equals(b.get("active")));}
 @GetMapping("/api/suppliers") public List<Map<String,Object>> getSuppliers(){return suppliers.findAll().stream().map(this::supplier).toList();}
 @PostMapping("/api/suppliers") public Map<String,Object> createSupplier(@RequestBody Map<String,Object>b){Supplier x=new Supplier();applySupplier(x,b);return supplier(suppliers.save(x));}
 @PutMapping("/api/suppliers/{id}") public Map<String,Object> updateSupplier(@PathVariable Long id,@RequestBody Map<String,Object>b){Supplier x=suppliers.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));applySupplier(x,b);return supplier(suppliers.save(x));}
 @DeleteMapping("/api/suppliers/{id}") public void deleteSupplier(@PathVariable Long id){suppliers.delete(suppliers.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 private void applySupplier(Supplier x,Map<String,Object>b){x.setName(str(b,"name"));x.setEmail(str(b,"email"));x.setPhone(str(b,"phone"));x.setAddress(str(b,"address"));}
 private String str(Map<String,Object>b,String k){Object v=b.get(k);return v==null?"":String.valueOf(v);}
 private int integer(Map<String,Object>b,String k,int d){return Integer.parseInt(String.valueOf(b.getOrDefault(k,d)));}
}
