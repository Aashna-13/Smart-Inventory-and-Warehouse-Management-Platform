package com.smartinventory.repository;
import com.smartinventory.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long>{ long countByStatus(OrderStatus status); List<PurchaseOrder> findByStatus(OrderStatus status); }