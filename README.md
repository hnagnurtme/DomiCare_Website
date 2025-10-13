# 🧹 DomiCare – Professional Cleaning & Maintenance Platform

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Status](https://img.shields.io/badge/status-in%20development-yellow.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7%2B-green.svg)
![ReactJS](https://img.shields.io/badge/ReactJS-18-blue.svg)
![JDK](https://img.shields.io/badge/JDK-11%2B-orange.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

> **A modern, transparent, and secure platform connecting customers with professional cleaning and maintenance services.**

---

## 🌟 Introduction

**DomiCare** is a web-based platform designed to streamline the booking of professional cleaning and maintenance services.  
Built with **Spring Boot**, **ReactJS**, and **PostgreSQL**, it ensures a seamless, transparent, and high-quality experience for customers, staff, and administrators.

## 🧠 Tech Stack

### **Backend**
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-EB5424?style=flat-square)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-4285F4?style=flat-square&logo=cloudinary&logoColor=white)
![JavaMail](https://img.shields.io/badge/SMTP-0078D4?style=flat-square&logo=gmail&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)

### **Frontend**
![ReactJS](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black)
![Redux](https://img.shields.io/badge/Redux-764ABC?style=flat-square&logo=redux&logoColor=white)
![Material UI](https://img.shields.io/badge/Material--UI-007FFF?style=flat-square&logo=mui&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=flat-square)
![React Router](https://img.shields.io/badge/React%20Router-CA4245?style=flat-square&logo=reactrouter&logoColor=white)
![i18next](https://img.shields.io/badge/i18next-26A69A?style=flat-square)

### **DevOps & Tools**
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=flat-square&logo=sonarqube&logoColor=white)

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
## Deployment

**Website:** [https://domicare.hnagnurtme.id.vn](https://domicare.hnagnurtme.id.vn)

**Sale Test Account:**  
- Email: domicare.company@gmail.com
- Password: SALErole123

---
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



