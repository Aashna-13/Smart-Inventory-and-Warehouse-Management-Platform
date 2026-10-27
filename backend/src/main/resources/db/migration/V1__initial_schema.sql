CREATE TABLE app_users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(80) NOT NULL UNIQUE, email VARCHAR(160) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL, role VARCHAR(30) NOT NULL, enabled BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE categories (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE, description VARCHAR(500));
CREATE TABLE products (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, sku VARCHAR(80) NOT NULL UNIQUE, name VARCHAR(180) NOT NULL,
 category_id BIGINT NULL, description VARCHAR(1000), unit_price DECIMAL(12,2) NOT NULL DEFAULT 0,
 reorder_point INT NOT NULL DEFAULT 0, reorder_quantity INT NOT NULL DEFAULT 1, active BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_product_category FOREIGN KEY(category_id) REFERENCES categories(id)
);
CREATE TABLE warehouses (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(120) NOT NULL UNIQUE, location VARCHAR(250) NOT NULL, capacity BIGINT NOT NULL DEFAULT 0, active BOOLEAN NOT NULL DEFAULT TRUE);
CREATE TABLE inventory_items (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, product_id BIGINT NOT NULL, warehouse_id BIGINT NOT NULL, quantity INT NOT NULL DEFAULT 0,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, version BIGINT NOT NULL DEFAULT 0,
 UNIQUE KEY uq_inventory_product_warehouse(product_id,warehouse_id),
 CONSTRAINT fk_inventory_product FOREIGN KEY(product_id) REFERENCES products(id),
 CONSTRAINT fk_inventory_warehouse FOREIGN KEY(warehouse_id) REFERENCES warehouses(id)
);
CREATE TABLE stock_movements (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, product_id BIGINT NOT NULL, warehouse_id BIGINT NOT NULL, movement_type VARCHAR(30) NOT NULL,
 quantity INT NOT NULL, notes VARCHAR(500), reference VARCHAR(100), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, created_by BIGINT NULL,
 CONSTRAINT fk_movement_product FOREIGN KEY(product_id) REFERENCES products(id),
 CONSTRAINT fk_movement_warehouse FOREIGN KEY(warehouse_id) REFERENCES warehouses(id),
 CONSTRAINT fk_movement_user FOREIGN KEY(created_by) REFERENCES app_users(id)
);
CREATE TABLE suppliers (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(150) NOT NULL UNIQUE, email VARCHAR(160), phone VARCHAR(40), address VARCHAR(500), active BOOLEAN NOT NULL DEFAULT TRUE);
CREATE TABLE purchase_orders (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, order_number VARCHAR(80) NOT NULL UNIQUE, supplier_id BIGINT NOT NULL, warehouse_id BIGINT NOT NULL,
 status VARCHAR(30) NOT NULL, notes VARCHAR(500), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_order_supplier FOREIGN KEY(supplier_id) REFERENCES suppliers(id),
 CONSTRAINT fk_order_warehouse FOREIGN KEY(warehouse_id) REFERENCES warehouses(id)
);
CREATE TABLE purchase_order_items (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, purchase_order_id BIGINT NOT NULL, product_id BIGINT NOT NULL, quantity INT NOT NULL,
 unit_price DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_item_order FOREIGN KEY(purchase_order_id) REFERENCES purchase_orders(id) ON DELETE CASCADE,
 CONSTRAINT fk_item_product FOREIGN KEY(product_id) REFERENCES products(id)
);
