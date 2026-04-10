# 🚨 IncidentFlow — Event-Driven Incident Management System

Production-oriented backend system inspired by **Site Reliability Engineering (SRE)** practices.

Designed to model **service outages, incident lifecycle, and system reliability** using a clean architecture approach and asynchronous event-driven communication.

---

## 🧠 Overview

IncidentFlow simulates how real systems behave under failure conditions:

* Services can go **UP / DOWN**
* Incidents are automatically created and resolved
* Events are published asynchronously
* Failures are handled with retries, idempotency, and observability

This project focuses not just on functionality, but on **resilience and reliability**.

---

## ⚙️ Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Web (REST APIs)**
* **Spring Data JPA (PostgreSQL / H2)**
* **Spring Data Redis (Pub/Sub)**
* **Docker & Docker Compose**
* **Clean Architecture (Domain / Application / Infrastructure / Interfaces)**

---

## 🧱 Architecture

The system follows a **clean architecture** approach:

* `domain/` → core business logic (entities, rules, events)
* `application/` → use cases and orchestration
* `infrastructure/` → persistence, Redis, external integrations
* `interfaces/` → REST API layer

👉 Controllers contain no business logic
👉 Domain is independent of frameworks

---

## 🔄 Event-Driven Design

The system uses **Redis Pub/Sub** for asynchronous communication.

### Event Flow

1. Service goes `DOWN`
2. `ServiceDownEvent` is published
3. Incident is automatically created
4. Event is consumed asynchronously by listeners
5. Logging + monitoring reactions are triggered

---

## 🔥 Reliability Features (Key Highlight)

This system simulates **production-grade resilience**:

### ✔ Retry Strategy

* 3 retries with exponential backoff (1s, 2s, 4s)

### ✔ Idempotency

* Each event has a unique `event_id`
* Duplicate processing is prevented

### ✔ Failure Handling

* Failed events are not lost
* Stored in a **failed event store**
* Logged with full context

### ✔ Structured Logging

Includes:

* `event_id`
* `event_type`
* `status` (processed / retry / failed)
* `retry_count`

---

## 🐳 Running the Project

### 1. Clone repository

```bash
git clone <repo-url>
cd incident_flow
```

---

### 2. Build the project

```bash
mvn clean package
```

---

### 3. Run with Docker

```bash
docker-compose up --build
```

---

### 4. Run locally (dev mode)

```bash
mvn spring-boot:run
```

👉 Uses **H2 in-memory DB** by default

---

## 🌐 API Endpoints

### Services

* `POST /services`
* `GET /services`
* `PATCH /services/{id}/status`

### Incidents

* `POST /incidents`
* `GET /incidents`
* `PATCH /incidents/{id}/status`

### Health

* `GET /health`

---

## 🔍 Example Use Case

1. Create a service
2. Set status to `DOWN`
3. System automatically:

   * creates incident
   * publishes event
   * triggers async processing
4. Set status to `UP`

   * resolves incident
   * emits resolution events

---

## 📦 Docker Setup

The system includes:

* `app` → Spring Boot backend
* `redis` → event broker

👉 Fully reproducible environment

---

## 🚀 Future Improvements

* Persist failed events in database
* Replace Redis Pub/Sub with Kafka or Redis Streams
* Add metrics (SLI / error rate)
* Add authentication (JWT)

---

## 💬 Why this project?

This project was built to demonstrate:

* Backend architecture design
* Event-driven systems
* Reliability and failure handling
* Production-oriented thinking

---

## 👤 Author

Juan Rubio
Backend Developer — Python & Java
Focused on APIs, automation, and distributed systems

---
