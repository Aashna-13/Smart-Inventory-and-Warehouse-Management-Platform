package com.smartinventory.repository;
import com.smartinventory.model.Warehouse; import org.springframework.data.jpa.repository.JpaRepository;
public interface WarehouseRepository extends JpaRepository<Warehouse,Long>{ boolean existsByNameIgnoreCase(String name); }