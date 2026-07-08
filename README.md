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


    ✨ Key Technical Features
Stateless JWT Authentication: Implements custom OncePerRequestFilter filters for cryptographic HMAC-SHA256 token verification in RAM without database hits.

O(1) Token Revocation: Uses a cloud-hosted Redis Key-Value store to blacklist revoked tokens with an automated 30-minute Time-To-Live (TTL) expiration matching token lifespan.

Database Optimization: Optimized MongoDB read latency from ~30ms down to <1ms for repeated session checks and user data fetching.

Cloud DevOps & Containerization: Built using an optimized multi-stage Dockerfile to strip build tools and minimize runtime container size, deployed via continuous CI/CD pipelines to Render.
