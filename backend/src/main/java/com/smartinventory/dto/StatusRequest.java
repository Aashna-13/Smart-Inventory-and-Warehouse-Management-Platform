package com.smartinventory.dto;
import com.smartinventory.model.OrderStatus; import jakarta.validation.constraints.NotNull;
public record StatusRequest(@NotNull OrderStatus status,String notes) {}
