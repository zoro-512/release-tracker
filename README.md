# 🚀 Mini Release Tracker

A Spring Boot backend application for managing software release records and deployment status tracking.

---

## 📋 Tech Stack

| Layer       | Technology                  |
|-------------|-----------------------------|
| Language    | Java 17                     |
| Framework   | Spring Boot 3.2.5           |
| Persistence | Spring Data JPA / Hibernate |
| Database    | MySQL 8.x                   |
| Build Tool  | Maven                       |
| API Testing | Postman                     |

---

## 🏗️ Project Structure

```
src/main/java/com/releasetracker/
├── MiniReleaseTrackerApplication.java   ← Entry point
│
├── controller/
│   └── ReleaseController.java           ← REST endpoints
│
├── service/
│   ├── ReleaseService.java              ← Service interface
│   └── impl/
│       └── ReleaseServiceImpl.java      ← Business logic
│
├── repository/
│   └── ReleaseRepository.java           ← Spring Data JPA
│
├── entity/
│   └── Release.java                     ← JPA Entity (@Table: releases)
│
├── dto/
│   ├── ReleaseRequestDTO.java           ← Create/Update request body
│   ├── ReleaseResponseDTO.java          ← API response body
│   └── StatusUpdateDTO.java             ← PATCH status body
│
├── enums/
│   ├── DeploymentEnvironment.java       ← DEV | QA | PROD
│   └── DeploymentStatus.java            ← PENDING | DEPLOYED | FAILED | ROLLBACK
│
└── exception/
    ├── ReleaseNotFoundException.java    ← 404 custom exception
    ├── ErrorResponse.java               ← Uniform error body
    └── GlobalExceptionHandler.java      ← @RestControllerAdvice
```

---

## ⚙️ Setup & Configuration

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.x running locally

### 2. Create the Database

Run the provided SQL script in MySQL:

```bash
mysql -u root -p < src/main/resources/db-init.sql
```

Or manually:

```sql
CREATE DATABASE IF NOT EXISTS release_tracker_db;
```

### 3. Configure Credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/release_tracker_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Build & Run

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

The server starts at **http://localhost:8080**

---

## 🌐 REST API Endpoints

### Base URL: `http://localhost:8080/api/releases`

| Method   | Endpoint                  | Description                      |
|----------|---------------------------|----------------------------------|
| `POST`   | `/api/releases`           | Create a new release             |
| `GET`    | `/api/releases`           | Get all releases                 |
| `GET`    | `/api/releases?project=X` | Filter by project name           |
| `GET`    | `/api/releases?status=X`  | Filter by status                 |
| `GET`    | `/api/releases?environment=X` | Filter by environment        |
| `GET`    | `/api/releases/{id}`      | Get release by ID                |
| `PUT`    | `/api/releases/{id}`      | Full update of a release         |
| `PATCH`  | `/api/releases/{id}/status` | Update deployment status only  |
| `DELETE` | `/api/releases/{id}`      | Delete a release                 |

---

## 📦 Sample Request / Response

### POST `/api/releases`

**Request Body:**
```json
{
  "projectName": "InventoryService",
  "version": "v1.2.0",
  "environment": "QA",
  "releaseDate": "2024-02-10",
  "status": "PENDING",
  "description": "Feature: stock alert system"
}
```

**Response (201 Created):**
```json
{
  "id": 6,
  "projectName": "InventoryService",
  "version": "v1.2.0",
  "environment": "QA",
  "releaseDate": "2024-02-10",
  "status": "PENDING",
  "description": "Feature: stock alert system",
  "createdAt": "2024-01-25T10:30:00",
  "updatedAt": "2024-01-25T10:30:00"
}
```

### PATCH `/api/releases/{id}/status`

**Request Body:**
```json
{ "status": "DEPLOYED" }
```

### Error Response (404):
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Release not found with ID: 99",
  "timestamp": "2024-01-25T10:30:00"
}
```

---

## 📊 Enums Reference

### DeploymentEnvironment
| Value | Description        |
|-------|--------------------|
| `DEV` | Development server |
| `QA`  | Testing/QA server  |
| `PROD`| Production server  |

### DeploymentStatus
| Value      | Description                    |
|------------|--------------------------------|
| `PENDING`  | Release is scheduled           |
| `DEPLOYED` | Successfully deployed          |
| `FAILED`   | Deployment failed              |
| `ROLLBACK` | Rolled back to previous version|

---

## 🧪 Testing with Postman

Import `Mini-Release-Tracker.postman_collection.json` into Postman.
Set the base URL variable to `http://localhost:8080`.

---

## 🔒 Exception Handling

| Scenario                  | HTTP Status | Response               |
|---------------------------|-------------|------------------------|
| Release not found         | 404         | `ReleaseNotFoundException` |
| Invalid request body      | 400         | Field-level error list |
| Invalid enum value        | 400         | Type mismatch message  |
| Unexpected server error   | 500         | Generic error message  |
