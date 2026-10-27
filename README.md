# Smart Inventory and Warehouse Management Platform

A robust, enterprise-ready Java 17 Spring Boot warehouse and inventory management platform featuring a clean responsive frontend, MySQL 8 persistence with Flyway migrations, JWT-secured REST APIs, role-based access control, Docker Compose multi-container setup, Kubernetes manifests for Minikube, and Helm charts for cloud-native deployment.

---

## Key Features

- **Role-Based Access & Security**: Stateless JWT authentication, BCrypt password hashing, and role-based permissions (`ROLE_ADMIN`, `ROLE_STAFF`, `ROLE_USER`).
- **Product & Category Management**: Hierarchical category organization, SKU tracking, unit of measure, minimum stock thresholds, and unit costs.
- **Multi-Warehouse Stock Management**: Multi-location warehouse tracking with rack/aisle/bin storage definitions and per-warehouse inventory levels.
- **Stock Movements & Audit Trail**: Real-time stock transactions (`INBOUND`, `OUTBOUND`, `TRANSFER`, `ADJUSTMENT`) with user audit logging.
- **Automated Reorder Recommendations**: Automatic calculation of reorder points and suggested replenishment quantities based on safety stock and current counts.
- **Purchase Order Workflow**: End-to-end procurement lifecycle from draft creation and supplier assignment to purchase order confirmation and inventory receiving.
- **Analytics & Dashboard KPIs**: Executive and operational dashboard with total inventory valuation, critical stock alerts, inventory turnover, and movement charts.
- **Database Versioning**: Flyway automated migrations with initial schema and realistic development seed data.
- **Cloud-Native Deployment**: Multi-stage Java 17 Dockerfile, Docker Compose, Kubernetes manifests, and configurable Helm charts.

---

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.3.5, Spring Data JPA, Spring Security, Flyway, Maven
- **Database**: MySQL 8.0
- **Frontend**: Vanilla HTML5, CSS3, and JavaScript (served directly from Spring Boot at `http://localhost:8080/`)
- **Containerization & Orchestration**: Multi-stage Dockerfile (Eclipse Temurin 17 JRE runtime), Docker Compose, Kubernetes (Minikube), and Helm 3

---

## Project Structure

```
Smart-Inventory-Warehouse-Platform/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/smartinventory/     # Spring Boot application code
│   │   │   └── resources/
│   │   │       ├── application.properties   # Application configuration
│   │   │       ├── db/migration/            # Flyway SQL migrations (V1__initial_schema.sql)
│   │   │       └── static/                  # Frontend UI (HTML, CSS, JS)
│   │   └── test/                            # Unit & sanity tests
│   ├── .dockerignore                        # Docker ignore rules for backend build
│   ├── .env.example                         # Backend environment template
│   ├── Dockerfile                           # Multi-stage Java 17 Dockerfile
│   └── pom.xml                              # Maven project descriptor
├── docker-compose.yml                       # Docker Compose specification (App + MySQL 8)
├── .dockerignore                            # Root Docker ignore rules
├── .env.example                             # Root environment variable template
├── .gitignore                               # Git ignore rules (protects .env and secrets)
├── docs/                                    # Architecture & API documentation
├── helm/                                    # Helm chart templates
├── k8s/                                     # Kubernetes deployment manifests
└── README.md                                # Project documentation
```

---

## Prerequisites

Before starting, ensure you have the following installed:
- **Docker Desktop** (version 20.10+ or 24.0+) with **Docker Compose** (v2+)
- **Windows PowerShell** 5.1+ or **PowerShell 7+** (for Windows users)
- *(Optional, for running without Docker)*: **JDK 17** and **Apache Maven 3.9+**

---

## 1. Environment Setup

1. Copy `.env.example` to create a local `.env` file at the project root:
   ```powershell
   Copy-Item .env.example .env
   ```
   *(On macOS/Linux: `cp .env.example .env`)*

2. Open `.env` and configure your credentials.

### Strong JWT Secret Generation
The application requires `JWT_SECRET` to be at least 32 bytes (256 bits) long. Generate a cryptographically secure random secret:

- **Windows PowerShell**:
  ```powershell
  [Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 } | ForEach-Object { [byte]$_ }))
  ```
- **macOS / Linux**:
  ```bash
  openssl rand -base64 32
  ```

Paste the generated string into your `.env` file under `JWT_SECRET`.

### Environment Variables Reference

| Variable | Description | Default in Compose |
|---|---|---|
| `DB_HOST` | MySQL hostname | `mysql` |
| `DB_PORT` | MySQL port | `3306` |
| `DB_NAME` | Database schema name | `smart_inventory` |
| `DB_USERNAME` | Application database user | `smartinv_user` |
| `DB_PASSWORD` | Application database password | `change-me-local-only` |
| `MYSQL_ROOT_PASSWORD` | MySQL root administrator password | `root-change-me` |
| `JWT_SECRET` | HMAC-SHA secret (>= 32 bytes) | *Required for security* |
| `JWT_EXPIRATION_MS` | JWT validity duration in milliseconds | `3600000` (1 hour) |
| `BOOTSTRAP_ADMIN_USERNAME` | Initial admin username | `admin` |
| `BOOTSTRAP_ADMIN_EMAIL` | Initial admin email | `admin@example.local` |
| `BOOTSTRAP_ADMIN_PASSWORD` | Initial admin password | `ChangeThisLocalPassword123!` |

> [!IMPORTANT]
> The `.env` file contains sensitive credentials and is explicitly ignored by Git (`.gitignore`). Never commit real passwords or production keys to version control.

---

## 2. Build and Startup (Docker Compose)

Start the MySQL database and backend services using Docker Compose:

```powershell
docker compose up -d --build
```

### What happens during startup:
1. Docker Compose builds the backend using the multi-stage `backend/Dockerfile` (compiles code and runs tests with Maven on Java 17, then creates a lightweight Eclipse Temurin 17 JRE runtime image).
2. The `mysql` container starts with MySQL 8.0 and initializes the `smart_inventory` database and `smartinv_user`.
3. The MySQL health check (`mysqladmin ping`) runs periodically until MySQL is ready.
4. The `app` service waits for MySQL to become healthy (`condition: service_healthy`) before launching.
5. On startup, Flyway automatically runs database migrations from `db/migration/V1__initial_schema.sql`.
6. `BootstrapAdmin` seeds the initial administrator account.
7. The application serves the UI and REST API at `http://localhost:8080/`.

---

## 3. Verifying Logs and Health

### Container Health and Status
Check the status of running containers:
```powershell
docker compose ps
```
Both services should show `Up` (with `mysql` marked as `healthy`).

### Application Health Check
Spring Boot Actuator exposes a health endpoint on port 8080:
```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```
Expected output:
```json
{"status":"UP"}
```

### Viewing Logs
- View combined logs in real time:
  ```powershell
  docker compose logs -f
  ```
- View backend logs only:
  ```powershell
  docker compose logs -f app
  ```
- View MySQL database logs only:
  ```powershell
  docker compose logs -f mysql
  ```

---

## 4. Accessing the Application

- **Web UI**: Open your browser at [http://localhost:8080/](http://localhost:8080/)
- **Sign In**: Navigate to [http://localhost:8080/login.html](http://localhost:8080/login.html)
  - **Username**: `admin` (or the value set in `BOOTSTRAP_ADMIN_USERNAME`)
  - **Password**: The password configured in `BOOTSTRAP_ADMIN_PASSWORD` (e.g. `ChangeThisLocalPassword123!` or `AdminPassword123!`)

---

## 5. Stopping Containers and Managing Data

### Temporary Stop
To stop running containers without removing them:
```powershell
docker compose stop
```
To restart them later:
```powershell
docker compose start
```

### Teardown (Data Preserved)
To stop and remove containers and internal networks while **preserving your database data**:
```powershell
docker compose down
```
All database tables, products, inventory records, and user accounts remain intact in the persistent named volume `mysql_data`.

### Complete Database Reset
> [!CAUTION]
> Running `docker compose down -v` permanently destroys the `mysql_data` named volume. All stored data—including user accounts, categories, products, inventory stock, purchase orders, and movement histories—will be completely and unrecoverably deleted!

To perform a complete factory reset:
```powershell
docker compose down -v
```
When you run `docker compose up -d --build` again, MySQL and Flyway will recreate the schema from scratch.

---

## 6. Running Backend Directly (Without Docker)

If you prefer to run the backend natively outside Docker:

1. Start a local MySQL 8 server on port 3306 and ensure database `smart_inventory` exists.
2. In PowerShell, set environment variables and launch Spring Boot:
   ```powershell
   cd backend
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"  # Adjust to your JDK 17 path
   $env:Path = "$env:JAVA_HOME\bin;$env:Path"
   mvn clean spring-boot:run
   ```
3. Access the application at `http://localhost:8080/`.

---

## 7. Kubernetes Deployment (Minikube)

For deploying the complete platform on a local Minikube Kubernetes cluster:

1. Ensure Minikube is started (`minikube start --driver=docker`).
2. Refer to the comprehensive [Kubernetes Guide](k8s/README.md) for manifest details, secrets, PVC storage, and step-by-step PowerShell commands.
3. Quick deployment:
   ```powershell
   # Build and load image into Minikube
   docker build -t smart-inventory:latest ./backend
   minikube image load smart-inventory:latest

   # Prepare secret and apply manifests
   Copy-Item k8s/secret.example.yaml k8s/secret.yaml
   kubectl apply -f k8s/namespace.yaml
   kubectl apply -f k8s/configmap.yaml
   kubectl apply -f k8s/secret.yaml
   kubectl apply -f k8s/mysql-pvc.yaml
   kubectl apply -f k8s/mysql-service.yaml
   kubectl apply -f k8s/mysql-deployment.yaml
   kubectl rollout status deployment/mysql -n smart-inventory --timeout=120s
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   kubectl rollout status deployment/smart-inventory -n smart-inventory --timeout=180s

   # Forward port to host
   kubectl port-forward service/smart-inventory 8081:8080 -n smart-inventory
   ```

---

## 8. Helm Deployment

The repository includes ready-to-use Helm chart definitions under `helm/smart-inventory/` supporting multiple environments (`dev`, `test`, `prod`).

### 1. Template Validation & Linting
Validate chart syntax and render templates using environment-specific values:
```powershell
# Lint chart
helm lint helm/smart-inventory

# Render development templates
helm template smart-inventory helm/smart-inventory -f helm/smart-inventory/values-dev.yaml

# Render production templates
helm template smart-inventory helm/smart-inventory -f helm/smart-inventory/values-prod.yaml
```

### 2. Deploy with Helm
```powershell
# Install or upgrade release in smart-inventory namespace
helm upgrade --install smart-inventory helm/smart-inventory `
  --namespace smart-inventory `
  --create-namespace `
  -f helm/smart-inventory/values-dev.yaml
```

---

## 9. Verification & Testing Checklist

To verify your environment and code before deployment or commits:

1. **Verify Java 17 toolchain**:
   ```powershell
   java -version
   mvn -version
   ```
2. **Execute backend test suite**:
   ```powershell
   cd backend
   mvn clean verify
   cd ..
   ```
3. **Verify Docker Compose build**:
   ```powershell
   docker compose build
   ```
4. **Health Check verification**:
   ```powershell
   docker compose up -d
   Invoke-RestMethod http://localhost:8080/actuator/health
   # Expected: {"status":"UP"}
   ```
5. **Lint Helm templates**:
   ```powershell
   helm lint helm/smart-inventory
   ```
