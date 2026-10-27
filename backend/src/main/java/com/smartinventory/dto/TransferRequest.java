package com.smartinventory.dto;
import jakarta.validation.constraints.Min; import jakarta.validation.constraints.NotNull;
public record TransferRequest(@NotNull Long productId,@NotNull Long sourceWarehouseId,@NotNull Long destinationWarehouseId,@Min(1) int quantity,String notes) {}
