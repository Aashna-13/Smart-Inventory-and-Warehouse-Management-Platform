package com.smartinventory.controller;

import com.smartinventory.model.*;
import com.smartinventory.repository.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final ProductRepository products;
    private final WarehouseRepository warehouses;
    private final InventoryItemRepository inventory;
    private final StockMovementRepository movements;
    private final PurchaseOrderRepository orders;

    public DashboardController(ProductRepository p, WarehouseRepository w, InventoryItemRepository i, StockMovementRepository m, PurchaseOrderRepository o) {
        products = p;
        warehouses = w;
        inventory = i;
        movements = m;
        orders = o;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        long low = inventory.findAll().stream().filter(i -> i.getQuantity() > 0 && i.getQuantity() <= i.getProduct().getReorderPoint()).count();
        long zero = inventory.countByQuantity(0);
        long units = inventory.findAll().stream().mapToLong(InventoryItem::getQuantity).sum();
        long pending = orders.countByStatus(OrderStatus.PENDING), confirmed = orders.countByStatus(OrderStatus.CONFIRMED);
        List<Map<String, Object>> distribution = warehouses.findAll().stream().map(w -> {
            long q = inventory.findByWarehouseId(w.getId()).stream().mapToLong(InventoryItem::getQuantity).sum();
            long count = inventory.findByWarehouseId(w.getId()).stream().map(i -> i.getProduct().getId()).distinct().count();
            return Map.<String, Object>of(
                "warehouseId", w.getId(),
                "warehouseName", w.getName(),
                "location", w.getLocation(),
                "capacity", w.getCapacity(),
                "totalUnits", q,
                "productCount", count,
                "utilizationPercentage", w.getCapacity() == 0 ? 0.0 : Math.round(q * 10000.0 / w.getCapacity()) / 100.0
            );
        }).toList();
        Map<String, Object> stat = new LinkedHashMap<>();
        for (OrderStatus s : OrderStatus.values()) stat.put(s.name().toLowerCase() + "Orders", orders.countByStatus(s));
        stat.put("totalOrders", orders.count());
        return Map.ofEntries(
            Map.entry("totalProducts", products.count()),
            Map.entry("totalWarehouses", warehouses.count()),
            Map.entry("totalInventoryUnits", units),
            Map.entry("lowStockCount", low),
            Map.entry("outOfStockCount", zero),
            Map.entry("recordedOutOfStockCount", zero),
            Map.entry("missingInventoryCount", 0),
            Map.entry("pendingOrdersCount", pending),
            Map.entry("confirmedOrdersCount", confirmed),
            Map.entry("activeOrdersCount", pending + confirmed),
            Map.entry("recentMovementsCount", movements.countByCreatedAtAfter(Instant.now().minus(Duration.ofDays(30)))),
            Map.entry("warehouseDistribution", distribution),
            Map.entry("orderStatistics", stat)
        );
    }

    @GetMapping("/movement-chart")
    public Map<String, Object> chart(@RequestParam(defaultValue = "30") int days) {
        if (days < 1 || days > 365) throw new IllegalArgumentException("days must be between 1 and 365");
        LocalDate end = LocalDate.now(), start = end.minusDays(days - 1);
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate d = start.plusDays(i);
            data.add(Map.of(
                "date", d,
                "stockInQuantity", 0,
                "stockOutQuantity", 0,
                "returnQuantity", 0,
                "transferQuantity", 0,
                "totalQuantity", 0,
                "movementCount", 0
            ));
        }
        return Map.of(
            "days", days,
            "startDate", start,
            "endDate", end,
            "totalMovements", 0,
            "totalQuantity", 0,
            "dailyData", data
        );
    }

    @GetMapping("/reorder-recommendations")
    public List<Map<String, Object>> recommendations(@RequestParam(required = false) Long warehouseId) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (InventoryItem i : inventory.findAll()) {
            if (warehouseId != null && !i.getWarehouse().getId().equals(warehouseId)) continue;
            Product p = i.getProduct();
            if (i.getQuantity() <= p.getReorderPoint()) {
                int suggested = i.getQuantity() == 0 ? p.getReorderQuantity() : Math.max(p.getReorderQuantity(), p.getReorderPoint() - i.getQuantity() + p.getReorderQuantity());
                out.add(Map.ofEntries(
                    Map.entry("productId", p.getId()),
                    Map.entry("productName", p.getName()),
                    Map.entry("productSku", p.getSku()),
                    Map.entry("warehouseId", i.getWarehouse().getId()),
                    Map.entry("warehouseName", i.getWarehouse().getName()),
                    Map.entry("currentQuantity", i.getQuantity()),
                    Map.entry("reorderPoint", p.getReorderPoint()),
                    Map.entry("reorderQuantity", p.getReorderQuantity()),
                    Map.entry("suggestedQuantity", suggested),
                    Map.entry("severity", i.getQuantity() == 0 ? "OUT_OF_STOCK" : "LOW_STOCK"),
                    Map.entry("reason", "Rule-based recommendation from current stock and configured reorder policy.")
                ));
            }
        }
        out.sort(Comparator.comparing(x -> String.valueOf(x.get("severity"))));
        return out;
    }
}

