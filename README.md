# 🛒 Electronic Store Backend

A Spring Boot based backend for an electronic store application, built with a modular structure and RESTful APIs.  
This project includes user authentication with JWT, refresh token handling, and interactive API documentation using Swagger (OpenAPI).

---

## 🚀 Features

### ✅ User Module
- Register user with:
  - Name, Email, Password, Gender, About, ImageName
- Login with JWT token
- Profile image upload and management
- Input validation and secure password handling

### 🔐 Authentication & Security
- Integrated **Spring Security**
- **JWT Token-based Authentication**
- **Refresh Token** mechanism for access token renewal
- Role-based access control (RBAC)

### 🗂️ Category & Product Management
- Complete CRUD operations for categories and products
- Image upload support for both categories and products
- Product filtering by price range and category
- Live/Non-live product status management

### 🛒 Shopping Features
- Cart management with add/remove/update items
- Order processing and status tracking
- Multiple items per order support
- Order history and status updates

### 🔧 Technical Features
- Environment-based configuration (dev/prod profiles)
- Docker containerization with docker-compose
- MySQL database integration
- Swagger API documentation
- Custom exception handling
- Pagination and sorting support

---

## 🛠️ Tech Stack
- Java 21
- Spring Boot 3.5.3
- Spring Security with JWT
- Spring Data JPA/Hibernate
- MySQL 8.0
- Docker & Docker Compose
- Maven
- Swagger/OpenAPI 3.0

---

## 🚀 Getting Started

### Local Development
1. Clone the repository
2. Configure MySQL database (default port: 3306)
3. Run using dev profile: `spring.profiles.active=dev`
4. Access API at `http://localhost:9090`

### Docker Deployment
```bash
docker-compose up -d
```
- Application: `http://localhost:8081`
- MySQL: Port 3308

### Production Deployment
- Configured for Railway deployment
- Uses production profile with secure database configuration
- Swagger UI: `/swagger-ui.html`
- API Docs: `/v3/api-docs`

---

## 📁 Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/electronic/store/
│   │       ├── config/
│   │       ├── controllers/
│   │       ├── entities/
│   │       ├── repositories/
│   │       └── services/
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
```

---

## 🔗 Additional Resources
- [GitHub Repository](https://github.com/pavitrapandey/electronic-store-backend)
- [API Documentation](http://localhost:9090/swagger-ui.html)

---

## 👨‍💻 Author
Made with ❤️ by [Pavitra Pandey](https://github.com/pavitrapandey)
