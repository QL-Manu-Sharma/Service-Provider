# Service-Provider

# Local Services Booking Platform (Backend)

A scalable, modular backend system for a local services booking platform. This project enables users to browse, book, and track local services such as plumbing, electrical, and home cleaning.

## Objective

To design and implement a clean, production-ready backend architecture that demonstrates:
- Clean data modeling and relationships using JPA
- Scalable system design
- Ownership from database to API layer
- Time-slot conflict resolution and business logic
- API-level modularity and security

---

## Key Features

### User Module
- User registration & JWT-based authentication (email otp verification and login)
- Basic profile with contact info and address
- Role-based access control using `User` and `Role` entities

### Provider Module
- Provider profile creation
- Ability to list service types (Plumbing, Electrical, etc.)
- Weekly availability with working and break hours
- Provider status: `AVAILABLE`, `BUSY`, `UNAVAILABLE`

### Services Catalog
- Categorized services (e.g., Plumbing, Electrical)
- Supports fixed/hourly pricing
- Supports optional add-ons with additional pricing

### Booking Management
- Book services with specific time slots
- Prevents overlapping bookings
- Booking lifecycle: `REQUESTED`, `ACCEPTED`, `REJECTED`, `CANCELLED`, `COMPLETED`

### Notifications
- Logs booking status changes (dummy email/SMS)
- Console-based notification simulation

### Rating System
- Users can rate providers post-service
- Average ratings reflected in provider profile

### Admin Tools
- Admin can view all users, providers, and bookings
- Admin can block/unblock accounts

---

## Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Language     | Java 17+                          |
| Framework    | Spring Boot 3+                    |
| DB           | PostgreSQL                        |
| ORM          | Hibernate + JPA                   |
| Security     | Spring Security + JWT             |
| Validation   | Jakarta Bean Validation           |

---

## Architecture Overview

- **Modular Layers**: `controller`, `service`, `repository`, `dto`, `model`, `exception`
- **JPA Mappings**:
  - `@ManyToOne`: Booking → User, Provider
  - `@OneToMany`: Provider → Schedule
  - `@ManyToMany`: User ↔ Roles
  - `@OneToOne`: ProviderProfile ↔ User
- **Entities**: `User`, `Role`, `Booking`, `ServiceCategory`, `ProviderProfile`, etc.
- **Authentication**: JWT (access + refresh tokens)

---

## Folder Structure

```
src/
├── main/
│ ├── java/com/example/
│ │ ├── controller/
│ │ ├── service/
│ │ ├── model/
│ │ ├── repository/
│ │ ├── dto/
│ │ ├── config/
│ │ └── exception/
│ └── resources/
│ ├── application.yml
│ └── schema.sql

```





### Setup Instructions

### Prerequisites
- Java 17+
- Maven
- PostgreSQL running locally

### Local Setup

```bash
# Clone the repository
git clone https://github.com/your-username/local-services-booking.git
cd local-services-booking

# Configure DB credentials in application.yml

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
