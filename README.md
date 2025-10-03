# DomiCare - Professional Cleaning & Maintenance Platform

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg) ![Status](https://img.shields.io/badge/status-in%20development-yellow.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg) ![JDK](https://img.shields.io/badge/JDK-11+-orange.svg)

> **A modern, transparent, and secure platform connecting customers with professional cleaning and maintenance services.**
## 🌟 Introduction

**DomiCare** is a web-based platform designed to streamline the booking of professional cleaning and maintenance services. Built with modern technologies like Spring Boot and ReactJS, it ensures a seamless, transparent, and high-quality experience for customers, staff, and administrators.

## 🚀 Key Features

### 👤 Customers
- **Account Management**:
  - Register/login via email or phone.
  - Password recovery via email.
  - Update personal information and view booking history.
- **Service Browsing**:
  - Explore services with images, prices, and descriptions.
  - Search, filter, and sort by keywords, categories, price, ratings, or popularity.
- **Booking**:
  - Select services, specify address, notes, and preferred time.
- **Service Reviews**:
  - Rate and comment on completed services.
  - View service rankings based on customer feedback.

### 🕵️ Guest Users
- Browse and search services.
- Register/login or request consultation.

### 💼 Sales Staff
- Manage bookings and customer consultations.
- Update personal information.
- View consulted bookings and generate revenue reports.
- Receive real-time notifications.

### 🛠️ Technical Staff
- Manage schedules and update task progress.
- Confirm task completion or report issues.

### 🛡️ Administrators
- Manage service categories, customer accounts, and staff roles.
- Create, edit, or delete discounts and promotions.
- Handle customer complaints and support requests.
- Monitor consultations and system performance.

## Backend API Documentation

## Backend API Documentation

- OpenAPI Spec (JSON): [https://domicare-web-v1-5.onrender.com/v3/api-docs](https://domicare-web-v1-5.onrender.com/v3/api-docs)  
- Swagger UI: [https://domicare-web-v1-5.onrender.com/swagger-ui/index.html](https://domicare-web-v1-5.onrender.com/swagger-ui/index.html)

### Swagger UI Preview
![Swagger UI Screenshot](https://github.com/hnagnurtme/DomiCare_Website/blob/master/domicare-web-v1-5.onrender.com_swagger-ui_index.html%20(1).png)


## ⚡ Quick Start

```bash
# Backend
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Frontend
cd frontend
npm install && npm start
```

> See the [Installation Guide](#installation-guide) for detailed setup instructions.

---

## 🏗️ System Architecture

DomiCare adopts a **microservices architecture** for scalability and maintainability, comprising:

### Backend (Spring Boot)
- **API Layer**: RESTful endpoints for request/response handling.
- **Service Layer**: Core business logic.
- **Repository Layer**: Database interactions.
- **Security Layer**: Authentication and authorization.
- **Email Service**: Automated email notifications.
- **File Storage Service**: Image and file management.

### Frontend (ReactJS)
- **Public Portal**: Customer-facing interface.
- **Admin Portal**: System management interface.
- **Sales Portal**: Sales staff dashboard.
- **Technical Portal**: Technical staff interface.

### Infrastructure
- **Database**: PostgreSQL (cloud-hosted).
- **File Storage**: Cloudinary.
- **Authentication**: JWT + OAuth2.
- **Caching**: Redis (optional).
- **CI/CD**: GitHub Actions or Jenkins.

---

## 💻 Technology Stack

### Backend
- **Spring Boot**: Core framework for Java development.
- **Spring Security**: Authentication and authorization.
- **Spring Data JPA**: Database interaction with ORM.
- **JWT & OAuth2**: Stateless authentication and third-party login (Google, Facebook).
- **PostgreSQL**: Relational database.
- **Hibernate**: ORM framework.
- **Swagger/OpenAPI**: API documentation.
- **Maven**: Dependency and build management.
- **JUnit & Mockito**: Unit testing.
- **JavaMail**: Email notifications.
- **Cloudinary**: Cloud-based image storage.
- **Lombok**: Boilerplate code reduction.

### Frontend
- **ReactJS**: UI development framework.
- **Redux**: State management.
- **Axios**: HTTP client for API calls.
- **React Router**: Client-side routing.
- **Material-UI**: UI component library.
- **Formik**: Form management.
- **React Query**: Data fetching and caching.
- **i18next**: Multilingual support.
- **Jest & React Testing Library**: Testing.
- **ESLint & Prettier**: Code quality tools.

### DevOps & Tools
- **Docker**: Containerization.
- **Git**: Version control.
- **Postman**: API testing.
- **SonarQube**: Code quality analysis.
- **Prometheus & Grafana**: Monitoring (optional).
- **ELK Stack**: Logging (optional).

---
## 🛠️ Installation Guide

### Prerequisites
- JDK 11+
- Maven 3.6+
- Node.js 14+ and npm 6+
- PostgreSQL 12+
- Git

### Backend Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/domicare.git
   cd domicare/backend
   ```

2. **Configure Database**
   Create a PostgreSQL database:
   ```sql
   CREATE DATABASE domicare;
   ```

3. **Set Up `application-local.properties`**
   Create `src/main/resources/application-local.properties`:
   ```properties
   # Database
   spring.datasource.url=jdbc:postgresql://localhost:5432/domicare
   spring.datasource.username=your-db-username
   spring.datasource.password=your-db-password
   spring.datasource.driver-class-name=org.postgresql.Driver

   # JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

   # JWT
   jwt.secret=your-jwt-secret-key
   jwt.expiration=86400000

   # Email
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-email-app-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true

   # OAuth2
   spring.security.oauth2.client.registration.google.client-id=your-google-client-id
   spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
   spring.security.oauth2.client.registration.google.scope=profile,email

   # Cloudinary
   cloudinary.cloud-name=your-cloud-name
   cloudinary.api-key=your-api-key
   cloudinary.api-secret=your-api-secret

   # Server
   server.port=8080
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```
   Or use the JAR file:
   ```bash
   java -jar target/domicare-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
   ```

## 📸 Screenshots

| **Registration** | **Service Catalog** | **Service Details** |
|------------------|---------------------|---------------------|
| ![Registration](https://github.com/user-attachments/assets/d442271a-35fc-4138-a9a4-9fd3646609ca) | ![Service Catalog](https://github.com/user-attachments/assets/4dbe0b06-b39e-4abd-a753-f3b7dbfc0242) | ![Service Details](https://github.com/user-attachments/assets/067ebd72-a9bf-45da-893b-c826262aa8c7) |

| **Booking History** | **Sales Dashboard** | **Admin Analytics** |
|---------------------|---------------------|---------------------|
| ![Booking History](https://github.com/user-attachments/assets/4378f121-c8e8-4917-978b-07055e9556ee) | ![Sales Dashboard](https://github.com/user-attachments/assets/f67d3206-797d-40a9-a915-b8ed756eddec) | ![Admin Analytics](https://github.com/user-attachments/assets/49ce3d2c-6f2c-41a6-a440-823bfa3ca94b) |

| **Email Verification** | **Staff Management** | **Service Management** |
|------------------------|----------------------|------------------------|
| ![Email Verification](https://github.com/user-attachments/assets/2c415aca-68f1-4197-b4f8-906e3a60b3f7) | ![Staff Management](https://github.com/user-attachments/assets/cf7a3507-5a46-43cf-9f25-1e718f9818a5) | ![Service Management](https://github.com/user-attachments/assets/b6aff725-d7a3-4840-912d-1dfe7d884505) |

---



