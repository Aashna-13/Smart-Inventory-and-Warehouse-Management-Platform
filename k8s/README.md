# Kubernetes Deployment Guide (Minikube)

This directory contains the Kubernetes manifests to deploy the **Smart Inventory and Warehouse Management Platform** and its dedicated **MySQL 8.0** database on a local Minikube cluster.

---

## Directory Structure

```text
k8s/
├── namespace.yaml           # Dedicated 'smart-inventory' namespace
├── configmap.yaml           # Non-secret application and DB configuration
├── secret.example.yaml      # Template for database & JWT credentials (safe for VCS)
├── secret.yaml              # Local secret with actual credentials (git-ignored)
├── mysql-pvc.yaml           # PersistentVolumeClaim for durable MySQL storage
├── mysql-deployment.yaml    # MySQL 8.0 Deployment with health probes and resource limits
├── mysql-service.yaml       # ClusterIP Service exposing MySQL on port 3306
├── deployment.yaml          # Spring Boot Deployment with initContainer and Actuator probes
├── service.yaml             # ClusterIP Service for the web app (ports 8080 and 80)
└── README.md                # This operational guide
```

---

## Prerequisites

1. **Minikube** installed and running:
   ```powershell
   minikube status
   ```
   If not running, start it:
   ```powershell
   minikube start --driver=docker
   ```

2. **Kubectl** installed and pointed to Minikube:
   ```powershell
   kubectl cluster-info
   ```

3. **Docker** installed and running.

---

## Step-by-Step Deployment on Windows PowerShell

### Step 1: Build and Load the Application Image into Minikube

From the repository root (`Smart-Inventory-Warehouse-Platform`):

```powershell
# 1. Build the Docker image locally
docker build -t smart-inventory:latest ./backend

# 2. Load the image directly into Minikube's Docker runtime
minikube image load smart-inventory:latest

# 3. Verify the image is available inside Minikube
minikube image ls | Select-String "smart-inventory"
```

---

### Step 2: Prepare the Kubernetes Secret

Copy the example secret template to `secret.yaml`:

```powershell
Copy-Item k8s/secret.example.yaml k8s/secret.yaml
```

Edit `k8s/secret.yaml` with your preferred credentials. For a quick local development setup:
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: smart-inventory-secret
  namespace: smart-inventory
type: Opaque
stringData:
  DB_USERNAME: "smartinv_user"
  DB_PASSWORD: "local_dev_password_123"
  MYSQL_ROOT_PASSWORD: "local_root_password_123"
  JWT_SECRET: "local_dev_jwt_secret_key_at_least_32_bytes_long_12345"
  BOOTSTRAP_ADMIN_PASSWORD: "AdminPassword123!"
```

*(Note: `k8s/secret.yaml` is included in `.gitignore` so real credentials will never be committed to Git).*

---

### Step 3: Deploy the Kubernetes Resources

Apply the manifests in logical sequence:

```powershell
# 1. Create the dedicated namespace
kubectl apply -f k8s/namespace.yaml

# 2. Apply ConfigMap and Secret
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 3. Deploy MySQL Persistent Volume Claim, Service, and Deployment
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-service.yaml
kubectl apply -f k8s/mysql-deployment.yaml

# 4. Wait for MySQL to become ready
kubectl rollout status deployment/mysql -n smart-inventory --timeout=120s

# 5. Deploy the Spring Boot application and Service
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# 6. Wait for the application to complete startup and health probes
kubectl rollout status deployment/smart-inventory -n smart-inventory --timeout=180s
```

---

### Step 4: Verify Deployment and Readiness

Check the status of pods, services, and storage:

```powershell
kubectl get all,pvc -n smart-inventory
```

Expected output:
- `pod/mysql-...` is `1/1 Running`.
- `pod/smart-inventory-...` is `1/1 Running` (after `wait-for-mysql` initContainer finishes).
- `service/mysql` is `ClusterIP` on port `3306`.
- `service/smart-inventory` is `ClusterIP` on ports `8080, 80`.
- `pvc/mysql-pvc` is `Bound`.

Inspect pod logs:
```powershell
# View application logs (Flyway migrations & Spring startup)
kubectl logs -f deployment/smart-inventory -n smart-inventory

# View MySQL database logs
kubectl logs -f deployment/mysql -n smart-inventory
```

---

### Step 5: Access the Application

Since the application uses a secure `ClusterIP` service, forward the port to your local machine:

```powershell
kubectl port-forward service/smart-inventory 8080:8080 -n smart-inventory
```

*(Note: If local port 8080 is already in use by Docker Compose, forward to port 8081 instead:)*
```powershell
kubectl port-forward service/smart-inventory 8081:8080 -n smart-inventory
```

Now open your browser:
- **Dashboard**: [http://localhost:8080/](http://localhost:8080/) *(or `http://localhost:8081/` if using port 8081)*
- **Sign In**: [http://localhost:8080/login.html](http://localhost:8080/login.html)
  - **Username**: `admin`
  - **Password**: The value configured in `BOOTSTRAP_ADMIN_PASSWORD` (e.g. `AdminPassword123!`)
- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

*(Alternatively, use `minikube service smart-inventory -n smart-inventory` to open in browser).*

---

### Step 6: Cleanup Without Deleting Persistent Data

To stop and remove application and database workloads while **keeping all stored database data intact in the PersistentVolumeClaim**:

```powershell
# Delete the deployments and services (data remains in mysql-pvc)
kubectl delete -f k8s/deployment.yaml
kubectl delete -f k8s/service.yaml
kubectl delete -f k8s/mysql-deployment.yaml
kubectl delete -f k8s/mysql-service.yaml
```

### Complete Teardown (Including Persistent Data)

> [!CAUTION]
> Deleting the namespace or PersistentVolumeClaim permanently erases the MySQL database volume!

```powershell
# This deletes everything including the storage volume:
kubectl delete namespace smart-inventory
```
