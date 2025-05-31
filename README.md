# E-Commerce Order Processing
This is a Spring Boot-based backend service for an e-commerce platform supporting customer management, product inventory, order processing, and payment handling.

## 🚀 Features
- Customer and product management
- Order creation and processing
- Payment integration
- Email notification on order placement
- Asynchronous event handling
- REST API with Swagger documentation
- Integration and unit tests

## 🛠️ Tech Stack
- Java 17
- Spring Boot
- JPA (Hibernate)
-  / H2 (for tests)
- Flyway data migration
- Docker
- Swagger (OpenAPI)
- JUnit & Mockito

---

## 🧪 Running Tests

```bash
./mvnw clean test
```

Spring is configured to roll back transactions after each test for isolation.

## ⚙️ Configuration

**`application.yml` (Test / Dev Profile)**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  server:
    port: 8049
```

## 📄 API Documentation

Visit: [http://localhost:8049/swagger-ui/index.html](http://localhost:8049/swagger-ui/index.html)

---

## 🐳 Docker Setup

Run this project in a Docker container.

### 🧱 Build the Docker image

```bash
./mvnw clean package -DskipTests
docker build -t order-service .
```

### 🚀 Run the Docker container

```bash
docker run -p 8049:8049 order-service
```

Once started, access the application at:

```
http://localhost:8049
```

Swagger UI:
```
http://localhost:8049/swagger-ui/index.html
```

---

## 📌 Interview Submission Checklist

- [x] Full source code pushed
- [x] Order creation, payment flow tested
- [x] Async events tested
- [x] Tests implemented and passing
- [x] REST API documented with Swagger
- [x] Application runs on port 8049

---
