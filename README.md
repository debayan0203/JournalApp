# 🚀 Enterprise Journal REST API

[![Java 17](https://img.shields.io/badge/Java-17-%23ED8B00.svg?style=flat&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MongoDB Atlas](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=flat&logo=mongodb&logoColor=white)](https://www.mongodb.com/cloud/atlas)
[![Redis Cache](https://img.shields.io/badge/Redis-Blacklist_Cache-DC382D?style=flat&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage_Build-2496ED?style=flat&logo=docker&logoColor=white)](https://www.docker.com/)
[![Live on Render](https://img.shields.io/badge/Cloud_Status-Live_on_Render-46E3B7?style=flat&logo=render&logoColor=white)](https://journalapp-kuw2.onrender.com/journal/public/health-check)

A production-grade, decoupled RESTful API built to demonstrate advanced cloud-native backend architecture, stateless security, and distributed caching.

---

## 🏗️ System Architecture & Auth Lifecycle

The application enforces strict **Role-Based Access Control (RBAC)** using custom Spring Security filter chains. To solve the vulnerability of stateless JWTs remaining valid after logout, this project integrates a **distributed Redis caching layer** to act as a real-time token revocation blacklist.

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant API as Spring Security Filter
    participant Redis as Redis Cache (Blacklist)
    participant DB as MongoDB Atlas

    Note over Client, DB: 1. Authentication Flow
    Client->>API: POST /public/login (Credentials)
    API->>DB: Validate User Details
    DB-->>API: User Verified
    API-->>Client: Return Signed JWT Token

    Note over Client, DB: 2. Protected Request Flow
    Client->>API: GET /journal/user (Header: Bearer <JWT>)
    API->>Redis: Check if Token is Blacklisted?
    Redis-->>API: Token NOT in Blacklist
    API-->>Client: 200 OK (Protected Data Returned)

    Note over Client, DB: 3. Secure Revocation (Logout) Flow
    Client->>API: POST /public/logout (Header: Bearer <JWT>)
    API->>Redis: Store Token with 1800s TTL (30-Min Expiry)
    Redis-->>API: Token Successfully Revoked
    API-->>Client: 200 OK (Logged out successfully)
```

---

## ✨ Key Technical Features

* **Stateless JWT Authentication:** Implements custom `OncePerRequestFilter` filters for cryptographic HMAC-SHA256 token verification in RAM without database hits.
* **O(1) Token Revocation:** Uses a cloud-hosted **Redis Key-Value store** to blacklist revoked tokens with an automated 30-minute Time-To-Live (TTL) expiration matching token lifespan.
* **Database Optimization:** Optimized MongoDB read latency from **~30ms down to <1ms** for repeated session checks and user data fetching.
* **Cloud DevOps & Containerization:** Built using an optimized **multi-stage Dockerfile** to strip build tools and minimize runtime container size, deployed via continuous CI/CD pipelines to **Render**.

---

## 🌐 Live Cloud Demo & How to Test

The backend is live on Render! You can test it immediately using **Postman**, **Thunder Client**, or **[Hoppscotch.io](https://hopscotch.io/)**.

> **⚠️ Note on Free Tier Hosting:** The cloud server sleeps after 15 minutes of inactivity. **The initial request may take ~45–60 seconds to wake the container up.** Subsequent requests will process instantly in `<1ms`!

### 1. Instant Health Check
Verify the server is running by clicking this direct browser link:  
👉 **[https://journalapp-kuw2.onrender.com/journal/public/health-check](https://journalapp-kuw2.onrender.com/journal/public/health-check)** *(Returns `"Ok"` when the server is active).*

---

## 🔌 API Endpoints Reference

### Public Endpoints (No Authentication Required)
| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/public/health-check` | Returns server health status | None |
| `POST` | `/public/signup` | Registers a new user account | `{ "userName", "password", "email" }` |
| `POST` | `/public/login` | Authenticates user & returns JWT | `{ "userName", "password" }` |

### Protected Endpoints (Requires `Authorization: Bearer <JWT>` Header)
| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/journal/user` | Fetches all journal entries for logged-in user | None |
| `POST` | `/journal/user` | Creates a new journal entry | `{ "title", "content" }` |
| `DELETE`| `/journal/user/{id}` | Deletes a specific journal entry | None |
| `POST` | `/public/logout` | Blacklists active token in Redis (Revokes session) | None |

---

## 💻 Local Development & Quickstart

### Prerequisites
* **Java Development Kit (JDK) 17+**
* **Apache Maven** (or use the included `./mvnw` wrapper)
* **Docker & Docker Compose** (Optional, for spinning up local Redis/MongoDB containers)

### 1. Clone the Repository
```bash
git clone [https://github.com/debayan0203/JournalApp.git](https://github.com/debayan0203/JournalApp.git)
cd JournalApp
```

### 2. Configure Environment Variables
Create a file named `application-dev.yml` inside `src/main/resources/` (or set these as system environment variables) with your database credentials:

```yaml
server:
  port: 8080

spring:
  data:
    mongodb:
      uri: mongodb+srv://<username>:<password>@cluster.mongodb.net/journaldb
    redis:
      host: <your-redis-host>
      port: 6379
      password: <your-redis-password>

jwt:
  secret: your-256-bit-hex-secret-key-for-hmac-sha256-signing
```

### 3. Run the Application
You can launch the API using the included Maven wrapper without needing to install Maven globally:

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows (Command Prompt / PowerShell)
mvnw.cmd spring-boot:run
```
Once started, the API will be listening at `http://localhost:8080`. You can hit `http://localhost:8080/public/health-check` to verify it is running!

---

## 🔐 Environment Configuration

This project strictly follows the **12-Factor App methodology** by externalizing all secrets and database credentials. Never commit hardcoded passwords or JWT secrets to version control.

| Variable Name | Description | Example / Default |
| :--- | :--- | :--- |
| `PORT` | Port the Spring Boot server binds to | `8080` |
| `SPRING_DATA_MONGODB_URI` | MongoDB Atlas connection string | `mongodb+srv://user:pass@cluster.mongodb.net/db` |
| `SPRING_DATA_REDIS_HOST` | Hostname of your cloud/local Redis cache | `srv-redis-cluster.internal` or `localhost` |
| `SPRING_DATA_REDIS_PORT` | Port number for Redis instance | `6379` |
| `SPRING_DATA_REDIS_PASSWORD`| Authentication password for Redis | `super-secret-redis-password` |
| `JWT_SECRET` | 256-bit Hex key used for HMAC-SHA256 signing | `9a4f2c8d3b7a1e6f4d5c8b2a0f3e7d1c...` |

---

## 📁 Project Structure

The project follows a modular, layered Spring Boot architecture to ensure separation of concerns between web controllers, business logic, security filters, and data persistence:

```text
src/main/java/com/debayan/JournalApp/
├── Api/
│   ├── Request/       # Data Transfer Objects (DTOs) for incoming payloads
│   └── Response/      # Standardized API response wrappers
├── controller/        # REST Controllers (/public, /journal/user endpoints)
├── entity/            # MongoDB Document models (@Document, @Id)
├── repository/        # Spring Data MongoRepository & Redis cache interfaces
├── service/           # Business logic, Redis TTL blacklisting, UserDetailsService
└── utils/             # JwtUtil (Token generation, claim extraction, HMAC validation)
```

---
