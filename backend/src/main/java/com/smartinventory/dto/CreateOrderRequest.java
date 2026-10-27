package com.smartinventory.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.NotNull; import jakarta.validation.constraints.Size; import java.util.List;
public record CreateOrderRequest(@NotBlank String orderNumber,@NotNull Long supplierId,@NotNull Long warehouseId,String notes,@Size(min=1) List<OrderItemRequest> items) {}
