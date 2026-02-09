
# Microservices E-Commerce Backend  
A comprehensive backend solution for E-Commerce applications using Spring Boot.

# Architecture Overview  
![Architecture Diagram](link-to-architecture-diagram)

# Tech Stack  
- Spring Boot  
- Spring Cloud  
- Java  
- Redis  
- Kafka  
- MySQL  
- Docker  
- Zipkin
- JWT 

# Services Overview  
- **Eureka Server**: Service registry to manage and discover services in the ecosystem.  
- **API Gateway**: A singular entry point for clients to interact with the microservices.  
- **Auth Service**: Handles user authentication and JWT token issuance.  
- **Product Service**: Manages product information and inventory.  
- **Cart Service**: Handles shopping cart operations for users.  
- **Order Service**: Manages order processing and tracking.  
- **Email Service**: Dispatches transactional emails to users.  

# Core Features  
- **JWT-based Authentication and Role-Based Authorization Control**: Secure API access using JSON Web Tokens and method-level security.  
- **API Gateway with routing and Redis rate limiting**: Efficiently routes requests with rate limiting to prevent abuse.  
- **Feign Clients and Eureka**: Simplifies communication between microservices.  
- **Event-driven Kafka architecture**: Enables asynchronous message processing for better performance.  
- **Resilience4j Circuit Breaker**: Implements circuit breaker patterns for service reliability.  
- **Zipkin distributed tracing**: Tracks request flows across services for monitoring.

# Database Design  
![Database Schema](link-to-database-schema)

# Local Setup & Running the Application  
1. Clone the repository: `git clone https://github.com/Laharikrkv/Microservices-ECommerce-Backend-SpringBoot.git`  
2. Navigate to the project directory.  
3. Start the services using Docker: `docker-compose up`  
4. Access the application via the API Gateway.

# Observability & Monitoring  
- **Zipkin**: Integrated for tracing requests and monitoring service interactions.

# What This Project Demonstrates  
This project showcases a fully functional microservices architecture using Spring Boot focused on scalability, security, and maintainability for e-commerce applications.

# Future Enhancements  
- **Load Balancing**: Enhance performance by distributing workloads across services.
- **Prometheus**: Monitoring and alerting toolkit to help improve visibility.
- **Grafana**: Visualization tool for monitoring metrics.
- **Logging**: Implementation of structured logging for better debugging.
- **Testing**: Writing unit tests using Junit and also Use Mockito for test-driven development.
- **Additional Enhancements**: Various other improvements are identified based on feedback and evolving needs.

# Author & Contact Information  
**Author**: Laharikrkv  
**Contact**: <laharik157@example.com>  
