# 📱 Device Manager API

A RESTful API to manage devices using Java 21, Spring Boot, PostgreSQL, and Docker.

---

## 🚀 Features

- Create, retrieve, update, and delete devices
- Filter devices by brand or state
- Enforces domain rules:
  - `creationTime` cannot be updated
  - `name` and `brand` cannot be changed if device is `IN_USE`
  - Devices in `IN_USE` state cannot be deleted
- OpenAPI (Swagger) documentation
- Containerized setup with Docker

---

## 🧰 Tech Stack

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL
- Springdoc OpenAPI
- Docker & Docker Compose

---
## 🐳 How to Run 
./mvnw clean package


## 🐳 How to Run with Docker

Run the following command in the project root:

```bash
docker-compose up --build

🌐 API Access
Once the application is running:

API Base URL: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui/index.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

📦 Example JSON Payload
{
  "name": "iPhone 15",
  "brand": "Apple",
  "state": "AVAILABLE"
}

📬 Endpoints
Method	Endpoint	              Description
POST	  /devices	              Create a new device
PATCH	  /devices/{id}	          Partially update device
GET	    /devices	              List all devices
GET	    /devices/{id}	          Get device by ID
GET	    /devices/brand/{brand}	Filter by brand
GET	    /devices/state/{state}	Filter by state
DELETE	/devices/{id}	Delete a device


🧪 Running Tests
To execute unit/integration tests:
./mvnw test