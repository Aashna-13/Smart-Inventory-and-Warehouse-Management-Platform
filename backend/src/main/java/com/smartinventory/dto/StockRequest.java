package com.smartinventory.dto;
import jakarta.validation.constraints.Min; import jakarta.validation.constraints.NotNull;
public record StockRequest(@NotNull Long productId,@NotNull Long warehouseId,@Min(1) int quantity,String notes) {}
