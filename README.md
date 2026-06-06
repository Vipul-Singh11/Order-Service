# 📦 Order Service

Order Service is a core microservice of the **Stock Trading Simulation Platform**.  
It handles buy/sell order placement, validates inputs, stores orders in MySQL, and prepares the system for integration with the Matching Engine.

---

## 🚀 Features

- 📝 Place BUY and SELL orders
- ✅ Input validation (quantity, price, required fields)
- 🗄 Persistent storage in MySQL
- 📊 Order status tracking (PENDING, EXECUTED)
- 🔍 Fetch all orders / fetch by ID
- ⚠ Global exception handling (validation + not found)
- 🧱 Clean layered architecture
- 📡 Event-ready design for Matching Engine integration

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL (Docker)
- Lombok
- Jakarta Validation

---

## 📂 Project Structure

    com.stock.orderservice
    │
    ├── controller         # REST APIs
    ├── service            # Business logic
    │   └── impl
    ├── repository         # JPA Repository
    ├── entity             # Order entity & enums
    ├── dto                # Request/Response DTOs
    ├── exception          # Global exception handling
    └── OrderServiceApplication.java

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

    git clone <your-repo-url>
    cd order-service

---

### 2️⃣ Start MySQL (Docker)

    docker run --name order-mysql \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=order_db \
    -p 3307:3306 \
    -d mysql:8

---

### 3️⃣ Configure application.yml

    server:
      port: 8083

    spring:
      datasource:
        url: jdbc:mysql://localhost:3307/order_db
        username: root
        password: root

      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true

    logging:
      level:
        org.springframework: INFO

---

### 4️⃣ Run the Application

    mvn spring-boot:run

---

## 🔌 API Endpoints

### 📝 Place Order

    POST /api/orders

#### Request Body:
    {
      "userId": 1,
      "stockSymbol": "AAPL",
      "quantity": 10,
      "price": 150.50,
      "orderType": "BUY"
    }

---

### 📊 Get All Orders

    GET /api/orders

---

### 🔍 Get Order by ID

    GET /api/orders/{id}

Example:

    GET /api/orders/1

---

## 🧠 Key Concepts Implemented

- DTO validation using @Valid, @NotNull, @Positive
- Global exception handling using @RestControllerAdvice
- Clean separation of layers (Controller → Service → Repository)
- Entity mapping with JPA
- Preparation for event-driven architecture

---

## 🧪 Testing

### Test Order Creation:

    POST http://localhost:8083/api/orders

---

### Test Get All Orders:

    GET http://localhost:8083/api/orders

---

### Test Validation:

Send invalid data:

    {
      "userId": null,
      "stockSymbol": "",
      "quantity": -5,
      "price": -100,
      "orderType": null
    }

Expected:
- 400 Bad Request
- Validation error response

---

### Test Not Found:

    GET http://localhost:8083/api/orders/999

Expected:

    {
      "message": "Order not found with id: 999"
    }

---

## 🐳 Docker Notes

- MySQL runs on port 3307
- Ensure container is running before starting service

---

## 📌 Future Improvements

- Integration with Matching Engine (event-driven)
- Redis Pub/Sub for order events
- Order cancellation support
- Pagination & filtering APIs
- Kafka-based messaging

---

## 👨‍💻 Author

Vipul Singh
