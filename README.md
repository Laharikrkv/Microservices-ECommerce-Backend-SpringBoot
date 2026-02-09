# Microservices E-Commerce Backend

A comprehensive backend solution for E-Commerce applications using Spring Boot.

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Services Overview](#services-overview)
- [Core Features](#core-features)
- [Database Design](#database-design)
- [Prerequisites](#prerequisites)
- [Local Setup & Running the Application](#local-setup--running-the-application)
- [API Documentation](#api-documentation)
- [Observability & Monitoring](#observability--monitoring)
- [What This Project Demonstrates](#what-this-project-demonstrates)
- [Future Enhancements](#future-enhancements)
- [Author & Contact Information](#author--contact-information)

## 🏗 Architecture Overview

![Architecture Diagram](link-to-architecture-diagram)

This project implements a microservices architecture where each service is independently deployable and scalable. Services communicate through synchronous REST calls (via Feign clients) and asynchronous event-driven messaging (via Kafka).

**Key Architectural Patterns:**
- Service Discovery (Eureka)
- API Gateway Pattern
- Circuit Breaker Pattern (Resilience4j)
- Event-Driven Architecture (Kafka)
- Distributed Tracing (Zipkin)
- Centralized Authentication (JWT)

## 🛠 Tech Stack

- **Spring Boot** - Core framework for building microservices
- **Spring Cloud** - Microservices infrastructure (Eureka, Gateway, OpenFeign)
- **Java** - Primary programming language
- **Redis** - In-memory cache and rate limiting
- **Kafka** - Event streaming and asynchronous messaging
- **MySQL** - Relational database for persistent storage
- **Docker** - Containerization and orchestration
- **Zipkin** - Distributed tracing and monitoring
- **JWT** - Secure token-based authentication

## 🔧 Services Overview

### Eureka Server
Service registry to manage and discover services in the ecosystem. All microservices register themselves with Eureka, allowing dynamic discovery and load balancing.

**Port:** 8761

### API Gateway
A singular entry point for clients to interact with the microservices. Handles routing, rate limiting, and cross-cutting concerns.

**Port:** 8083

**Features:**
- Intelligent request routing
- Redis-based rate limiting
- Request/response logging
- Authentication validation

### Auth Service
Handles user authentication and JWT token issuance. Manages user registration, login, and role-based access control.

**Port:** 8000

**Responsibilities:**
- User registration and login
- JWT token generation and validation
- Role-based access control (RBAC)
- Password encryption

### Product Service
Manages product information and inventory. Handles product CRUD operations, inventory tracking, and product search.

**Port:** 8080

**Responsibilities:**
- Product CRUD operations
- Inventory tracking
- Product search and filtering
- Category management

### Cart Service
Handles shopping cart operations for users. Manages add/remove items, quantity updates, and cart persistence.

**Port:** 8081

**Responsibilities:**
- Add/remove items from cart
- Update item quantities
- Cart persistence
- Cart expiration handling

### Order Service
Manages order processing and tracking. Processes customer orders from creation to completion and publishes order events to Kafka.

**Port:** 8082

**Responsibilities:**
- Order creation and processing
- Order status tracking
- Payment integration
- Publishes order events to Kafka

### Email Service
Dispatches transactional emails to users. Consumes Kafka events and sends order confirmations, shipping notifications, and account verification emails.

**Port:** 8084

**Responsibilities:**
- Order confirmation emails
- Shipping notifications
- Account verification emails
- Event-driven email dispatch

## ✨ Core Features

### JWT-based Authentication and Role-Based Authorization Control
Secure API access using JSON Web Tokens and method-level security. Implements stateless authentication with role-based access control at method level using Spring Security annotations.

### API Gateway with Routing and Redis Rate Limiting
Efficiently routes requests with rate limiting to prevent abuse. Centralized gateway that implements Redis-backed rate limiting to ensure fair usage and protect backend services.

### Feign Clients and Eureka
Simplifies communication between microservices. Uses Spring Cloud OpenFeign for declarative REST client creation with built-in load balancing and automatic service discovery.

### Event-driven Kafka Architecture
Enables asynchronous message processing for better performance. Decouples services, improves scalability, and ensures eventual consistency across the system.

### Resilience4j Circuit Breaker
Implements circuit breaker patterns for service reliability. Prevents cascading failures with automatic fallback mechanisms when downstream services are unavailable.

### Zipkin Distributed Tracing
Tracks request flows across services for monitoring. Provides end-to-end request tracing across all microservices, enabling performance monitoring and troubleshooting.

## 💾 Database Design

Each microservice maintains its own database following the database-per-service pattern, ensuring loose coupling and independent scalability.

**Database Structure:**
- **Auth DB:** User credentials, roles, permissions
- **Product DB:** Product catalog, inventory, categories
- **Cart DB:** User carts, cart items
- **Order DB:** Orders, order items, order history

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+** for dependency management
- **Docker** and **Docker Compose** for containerization
- **Git** for version control

## 🚀 Local Setup & Running the Application

### 1. Clone the Repository

```bash
git clone https://github.com/Laharikrkv/Microservices-ECommerce-Backend-SpringBoot.git
```

### 2. Navigate to the Project Directory

```bash
cd Microservices-ECommerce-Backend-SpringBoot
```

### 3. Start the Services Using Docker

```bash
docker-compose up
```

This will start all services along with their dependencies (MySQL, Redis, Kafka, Zipkin).

### 4. Access the Application

Access the application via the API Gateway:

- **API Gateway:** http://localhost:8083
- **Eureka Dashboard:** http://localhost:8761/eureka/
- **Zipkin Dashboard:** http://localhost:9411/api/v2/spans

### 5. Verify Service Health

Check if all services are registered with Eureka:

```bash
curl http://localhost:8761/eureka/apps
```

## 📚 API Documentation

### Authentication Endpoints

```http
POST /api/auth/register - Register new user
POST /api/auth/login - User login (returns JWT token)
POST /api/auth/refresh - Refresh JWT token
GET /api/auth/validate - Validate JWT token
```

### Product Endpoints

```http
GET /api/products - Get all products
GET /api/products/{id} - Get product by ID
POST /api/products - Create new product (Admin only)
PUT /api/products/{id} - Update product (Admin only)
DELETE /api/products/{id} - Delete product (Admin only)
```

### Cart Endpoints

```http
GET /api/cart - Get user's cart
POST /api/cart/items - Add item to cart
PUT /api/cart/items/{id} - Update cart item quantity
DELETE /api/cart/items/{id} - Remove item from cart
DELETE /api/cart - Clear cart
```

### Order Endpoints

```http
GET /api/orders - Get user's orders
GET /api/orders/{id} - Get order by ID
POST /api/orders - Create new order
PUT /api/orders/{id}/status - Update order status (Admin only)
```

**Authentication:** Most endpoints require JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## 📊 Observability & Monitoring

### Zipkin
Integrated for tracing requests and monitoring service interactions. Access Zipkin UI at http://localhost:9411 to:

- Trace requests across multiple services
- Identify performance bottlenecks
- Monitor service dependencies
- Analyze latency patterns

### Service Health Monitoring

Each service exposes Spring Boot Actuator endpoints:

```http
GET /actuator/health - Service health status
GET /actuator/metrics - Service metrics
GET /actuator/info - Service information
```

## 🎯 What This Project Demonstrates

This project showcases a fully functional microservices architecture using Spring Boot focused on scalability, security, and maintainability for e-commerce applications.

**Key Demonstrations:**

- **Scalability:** Each service can be independently scaled based on demand
- **Fault Tolerance:** Circuit breakers prevent cascading failures
- **Security:** JWT-based authentication with role-based authorization
- **Performance:** Redis caching and rate limiting for optimal response times
- **Maintainability:** Clean architecture with separation of concerns
- **Observability:** Comprehensive monitoring and tracing capabilities
- **Asynchronous Processing:** Event-driven architecture using Kafka for improved throughput
- **Containerization:** Docker support for consistent deployment across environments

## 🔮 Future Enhancements

- **Load Balancing**: Enhance performance by distributing workloads across services.
- **Prometheus**: Monitoring and alerting toolkit to help improve visibility.
- **Grafana**: Visualization tool for monitoring metrics.
- **Logging**: Implementation of structured logging for better debugging.
- **Testing**: Writing unit tests using JUnit and also use Mockito for test-driven development.
- **Additional Enhancements**: Various other improvements are identified based on feedback and evolving needs.

### Planned Technical Improvements

- **API Versioning:** Implement versioning strategy for backward compatibility
- **Database Migration:** Add Flyway or Liquibase for version-controlled schema management
- **Configuration Management:** Migrate to Spring Cloud Config Server
- **Payment Gateway Integration:** Add support for multiple payment providers
- **Search Functionality:** Integrate Elasticsearch for advanced product search
- **Caching Strategy:** Implement multi-level caching (L1: in-memory, L2: Redis)
- **Centralized Logging:** Implement ELK stack (Elasticsearch, Logstash, Kibana)

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure your code follows the existing style and includes appropriate tests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author & Contact Information

**Author:** Laharikrkv

- GitHub: [@Laharikrkv](https://github.com/Laharikrkv)
- Project Link: [https://github.com/Laharikrkv/Microservices-ECommerce-Backend-SpringBoot](https://github.com/Laharikrkv/Microservices-ECommerce-Backend-SpringBoot)

For questions, suggestions, or collaboration opportunities, feel free to open an issue or reach out through GitHub.

---

⭐ If you find this project helpful, please consider giving it a star!
