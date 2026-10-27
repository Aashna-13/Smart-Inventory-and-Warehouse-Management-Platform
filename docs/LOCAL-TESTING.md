# Local verification checklist

1. Confirm Maven uses Java 17: `mvn -version`.
2. Confirm MySQL 8 is available or start Compose.
3. Set a strong local `JWT_SECRET` and `BOOTSTRAP_ADMIN_PASSWORD`.
4. Run `cd backend && mvn clean verify`.
5. Start with `mvn spring-boot:run`.
6. Open `http://localhost:8080/login.html`.
7. Sign in using bootstrap admin credentials.
8. Create category, product, warehouse and supplier.
9. Add stock, stock out and transfer stock; verify `/api/movements`.
10. Create purchase order, confirm it, receive it and verify inventory increased.
11. Check dashboard and reorder recommendations.
12. Test unauthenticated calls return 401 and restricted writes return 403.
13. Test Compose with `docker compose up --build`.
14. Render Helm templates: `helm template smart-inventory helm/smart-inventory -f helm/smart-inventory/values-dev.yaml`.
