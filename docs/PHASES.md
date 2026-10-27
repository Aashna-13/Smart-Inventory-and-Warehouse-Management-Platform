# Phase map

## Phase 1 — Foundation
Spring Boot monolith, Java 17, Maven, MySQL 8, Flyway, base schema, environment configuration.

## Phase 2 — Authentication and authorization
JWT login, BCrypt password hashing, stateless Spring Security, ADMIN / WAREHOUSE_MANAGER / STAFF roles, bootstrap administrator.

## Phase 3 — Products and categories
Category/product CRUD, SKU and reorder settings, search and filtering.

## Phase 4 — Warehouses and inventory
Warehouse CRUD, stock in/out, returns, transfers, current balances and movement audit.

## Phase 5 — Suppliers and purchase orders
Supplier management, purchase-order creation/status transitions and stock receipt.

## Phase 6 — Analytics
Dashboard KPIs, warehouse utilization, movement chart endpoint, explainable reorder recommendations.

## Phase 7 — Frontend
Responsive vanilla JS frontend served by Spring Boot static resources; login, dashboard, products, categories, warehouses, inventory, movements, suppliers and orders.

## Phase 8 — Containerization
Dockerfile, Compose service for MySQL and application, health check and environment-based configuration.

## Phase 9 — Kubernetes and Helm
Baseline Kubernetes deployment/service/config, Helm chart and dev/test/prod values. Separate releases per environment.

## Phase 10 — CI/CD and security
GitHub Actions Maven tests/build, Docker image build and Trivy image scan; SonarQube project configuration.

## Known implementation boundaries
This bundle is a cohesive starting implementation, not a claim that all features have been independently integration-tested on your PC. Before academic submission, verify Maven build, database migrations, CRUD operations, stock receiving, security, dashboard aggregation, Helm template rendering and container startup. Add integration tests for your required rubric and review role permissions before real use.
