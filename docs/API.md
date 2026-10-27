# API quick reference

All API routes are served from `http://localhost:8080`. Login is public; most other routes require `Authorization: Bearer <accessToken>`.

## Authentication
- `POST /api/auth/login` — `{ "usernameOrEmail": "admin", "password": "..." }`
- `GET /api/auth/me`

## CRUD
- `GET/POST /api/categories`; `PUT/DELETE /api/categories/{id}`
- `GET/POST /api/products`; `GET/PUT/DELETE /api/products/{id}`; optional `name`, `sku`, `categoryId` filters
- `GET/POST /api/warehouses`; `PUT/DELETE /api/warehouses/{id}`
- `GET/POST /api/suppliers`; `PUT/DELETE /api/suppliers/{id}`

## Inventory and movements
- `GET /api/inventory`
- `POST /api/inventory/stock-in` — `{ "productId": 1, "warehouseId": 1, "quantity": 10, "notes": "Receipt" }`
- `POST /api/inventory/stock-out` — same shape; cannot reduce stock below zero
- `POST /api/inventory/return` — same shape
- `POST /api/inventory/transfer` — `{ "productId": 1, "sourceWarehouseId": 1, "destinationWarehouseId": 2, "quantity": 2, "notes": "Transfer" }`
- `GET /api/movements`

## Purchase orders
- `GET /api/orders`
- `POST /api/orders` — `{ "orderNumber":"PO-1001", "supplierId":1, "warehouseId":1, "notes":"Restock", "items":[{"productId":1,"quantity":10,"unitPrice":25.00}] }`
- `PUT /api/orders/{id}/status` — `{ "status":"CONFIRMED", "notes":"Confirmed" }`; use `RECEIVED` to add ordered quantities to inventory.
- `DELETE /api/orders/{id}`

## Dashboard
- `GET /api/dashboard/summary`
- `GET /api/dashboard/movement-chart?days=30`
- `GET /api/dashboard/reorder-recommendations?warehouseId=1` (warehouse filter optional)

Note: endpoint details and permissions can evolve as the project is validated. Do not expose the service publicly without a security review.
