# 🚀 Mini Release Tracker

A production-style backend application built with **Spring Boot** for managing software release workflows, deployment tracking, and release lifecycle management.

Designed to demonstrate real-world backend development concepts including layered architecture, REST APIs, DTOs, database integration, exception handling, and cloud deployment.

---

## 🌐 Live Demo

🔗 [Live API](https://release-tracker-production.up.railway.app/)

---

## ✨ Features

- Create, update, delete, and fetch software releases
- RESTful API architecture
- Layered backend architecture
- DTO-based request/response handling
- Centralized exception handling
- Enum-based status and priority management
- MySQL database integration
- JPA/Hibernate ORM
- Cloud deployment using Railway
- Clean and scalable code structure

---

## 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java 21 | Core Programming Language |
| Spring Boot 3 | Backend Framework |
| Spring Data JPA | Database ORM |
| MySQL | Relational Database |
| Maven | Dependency Management |
| Railway | Cloud Deployment |
| Lombok | Boilerplate Reduction |

---

## 📂 Project Structure

```bash
src/main/java/com/releasetracker
│
├── controller      # REST Controllers
├── service         # Business Logic
├── repository      # JPA Repositories
├── entity          # Database Entities
├── dto             # Data Transfer Objects
├── exception       # Global Exception Handling
└── config          # Configuration Classes
```

---

## ⚙️ API Endpoints

### 🔹 Create Release

```http
POST /api/releases
```

#### Request Body

```json
{
  "title": "Version 2.0 Launch",
  "description": "Major production deployment",
  "status": "PLANNED",
  "priority": "HIGH"
}
```

---

### 🔹 Get All Releases

```http
GET /api/releases
```

---

### 🔹 Get Release By ID

```http
GET /api/releases/{id}
```

---

### 🔹 Update Release

```http
PUT /api/releases/{id}
```

---

### 🔹 Delete Release

```http
DELETE /api/releases/{id}
```

---

## 🧠 Backend Concepts Practiced

- Dependency Injection
- REST API Design
- DTO Mapping
- JPA Repository Pattern
- Exception Handling
- Layered Architecture
- CRUD Operations
- Cloud Deployment
- Environment Variables
- Database Connectivity

---

## ☁️ Deployment

Application deployed using:

- Railway Cloud Platform
- Railway MySQL Database

---

## ▶️ Run Locally

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/release-tracker.git
cd release-tracker
```

---

### 2️⃣ Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/release_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

---

## 📌 Future Improvements

- JWT Authentication
- Swagger/OpenAPI Documentation
- Role-Based Access Control
- Docker Support
- CI/CD Pipeline
- Analytics Dashboard
- Release Scheduling

---
