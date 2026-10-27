-- Migration: V2__seed_data.sql
-- Description: Populate initial realistic development seed data for categories, warehouses, suppliers, products, inventory, and purchase orders.

-- 1. Categories
INSERT INTO categories (id, name, description) VALUES
(1, 'Electronics', 'Barcode scanners, label printers, RFID readers, and electronic warehouse devices'),
(2, 'Storage & Racking', 'Industrial shelving, heavy-duty storage bins, and pallet racking components'),
(3, 'Packaging & Shipping', 'Shipping boxes, packaging tape, stretch film, and barcode labels'),
(4, 'Material Handling', 'Hydraulic pallet jacks, platform trucks, utility carts, and hand trucks'),
(5, 'Safety & Maintenance', 'High-visibility safety vests, protective gloves, and facility maintenance tools');

-- 2. Warehouses
INSERT INTO warehouses (id, name, location, capacity, active) VALUES
(1, 'Central Distribution Center', 'Building 4, Sector 18, Industrial Area', 50000, TRUE),
(2, 'North Logistics Hub', 'Plot 12, Northern Ring Road', 25000, TRUE),
(3, 'East Port Warehouse', 'Bay 3, Port Gateway East', 15000, TRUE),
(4, 'South Regional Storehouse', 'Unit 7, Southern Highway Zone', 20000, TRUE);

-- 3. Suppliers
INSERT INTO suppliers (id, name, email, phone, address, active) VALUES
(1, 'Apex Global Logistics', 'sales@apexlogistics.local', '+1-555-0192', '100 Industrial Pkwy, Suite A', TRUE),
(2, 'OmniTech Components', 'orders@omnitech.local', '+1-555-0144', '250 Silicon Way, Tech Park', TRUE),
(3, 'PrimePack Solutions', 'contact@primepack.local', '+1-555-0178', '45 Cargo Boulevard', TRUE),
(4, 'Titan Material Handling', 'support@titanhandling.local', '+1-555-0165', '800 Heavy Equipment Way, Zone 3', TRUE);

-- 4. Products
INSERT INTO products (id, sku, name, category_id, description, unit_price, reorder_point, reorder_quantity, active) VALUES
(1, 'SKU-ELEC-001', 'Wireless Barcode Scanner', 1, 'Handheld 2D/QR laser scanner with USB wireless dock', 89.99, 10, 20, TRUE),
(2, 'SKU-ELEC-002', 'Thermal Label Printer', 1, 'High-speed industrial thermal shipping label printer', 249.50, 5, 10, TRUE),
(3, 'SKU-STOR-001', 'Heavy Duty Storage Bin (60L)', 2, 'Stackable industrial polypropylene tote bin', 24.00, 30, 50, TRUE),
(4, 'SKU-PACK-001', 'Direct Thermal Shipping Labels (500/roll)', 3, '4x6 inch fanfold shipping barcode labels', 12.50, 25, 50, TRUE),
(5, 'SKU-PACK-002', 'Heavy Duty Packing Tape (6 Rolls)', 3, 'Industrial clear acrylic packing tape 48mm x 66m', 18.00, 20, 40, TRUE),
(6, 'SKU-HAND-001', 'Hydraulic Hand Pallet Jack', 4, 'Manual 2500kg capacity hydraulic pallet truck', 320.00, 2, 5, TRUE),
(7, 'SKU-SAFE-001', 'High-Visibility Safety Vest (Pack of 10)', 5, 'ANSI class 2 reflective safety vests for warehouse personnel', 35.00, 8, 15, TRUE);

-- 5. Inventory Items
INSERT INTO inventory_items (id, product_id, warehouse_id, quantity, updated_at, version) VALUES
(1, 1, 1, 45, CURRENT_TIMESTAMP, 0),
(2, 2, 1, 12, CURRENT_TIMESTAMP, 0),
(3, 3, 1, 120, CURRENT_TIMESTAMP, 0),
(4, 4, 1, 80, CURRENT_TIMESTAMP, 0),
(5, 5, 1, 60, CURRENT_TIMESTAMP, 0),
(6, 6, 1, 4, CURRENT_TIMESTAMP, 0),
(7, 7, 1, 25, CURRENT_TIMESTAMP, 0),
(8, 1, 2, 8, CURRENT_TIMESTAMP, 0),   -- Low stock (reorder_point is 10)
(9, 2, 2, 0, CURRENT_TIMESTAMP, 0),   -- Out of stock (reorder_point is 5)
(10, 3, 2, 20, CURRENT_TIMESTAMP, 0), -- Low stock (reorder_point is 30)
(11, 4, 2, 50, CURRENT_TIMESTAMP, 0),
(12, 1, 3, 15, CURRENT_TIMESTAMP, 0),
(13, 3, 3, 0, CURRENT_TIMESTAMP, 0);  -- Out of stock (reorder_point is 30)

-- 6. Stock Movements
INSERT INTO stock_movements (id, product_id, warehouse_id, movement_type, quantity, notes, reference, created_at, created_by) VALUES
(1, 1, 1, 'STOCK_IN', 50, 'Initial receipt from supplier', 'PO-2026-0001', CURRENT_TIMESTAMP, NULL),
(2, 1, 1, 'STOCK_OUT', 5, 'Dispatched to retail outlet', 'SO-2026-0010', CURRENT_TIMESTAMP, NULL),
(3, 2, 1, 'STOCK_IN', 15, 'Initial stock receiving', 'PO-2026-0002', CURRENT_TIMESTAMP, NULL),
(4, 3, 1, 'STOCK_IN', 150, 'Bulk pallet storage delivery', 'PO-2026-0003', CURRENT_TIMESTAMP, NULL),
(5, 3, 1, 'STOCK_OUT', 30, 'Internal warehouse fulfillment', 'WO-2026-0004', CURRENT_TIMESTAMP, NULL),
(6, 1, 2, 'TRANSFER', 8, 'Stock transfer from Central DC', 'TR-2026-0001', CURRENT_TIMESTAMP, NULL);

-- 7. Purchase Orders
INSERT INTO purchase_orders (id, order_number, supplier_id, warehouse_id, status, notes, created_at) VALUES
(1, 'PO-2026-0001', 2, 1, 'CONFIRMED', 'Restock barcode scanners', CURRENT_TIMESTAMP),
(2, 'PO-2026-0002', 2, 2, 'PENDING', 'Urgent replacement for label printers', CURRENT_TIMESTAMP),
(3, 'PO-2026-0003', 3, 1, 'RECEIVED', 'Packaging supplies initial shipment', CURRENT_TIMESTAMP);

-- 8. Purchase Order Items
INSERT INTO purchase_order_items (id, purchase_order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 20, 85.00),
(2, 2, 2, 5, 240.00),
(3, 3, 4, 50, 11.50),
(4, 3, 5, 40, 16.50);
