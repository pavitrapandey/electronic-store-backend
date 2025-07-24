# 🛒 Electronic Store Backend
A Spring Boot based backend for an electronic store application, built with a modular structure and RESTful APIs.  
This project includes user authentication with JWT, refresh token handling, and interactive API documentation using Swagger (OpenAPI).

🚀 **Successfully deployed on Railway Cloud Platform!**

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
- ✅ **Successfully deployed on Railway Cloud Platform**
- Production URL: `https://electronic-store-backend-production-d2fc.up.railway.app`
- Uses production profile with secure database configuration
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
## 🔗 API Documentation & Resources
- 🌐 **Live API Documentation**: [https://electronic-store-backend-production-d2fc.up.railway.app/swagger-ui/index.html](https://electronic-store-backend-production-d2fc.up.railway.app/swagger-ui/index.html)
- 📖 **GitHub Repository**: [https://github.com/pavitrapandey/electronic-store-backend](https://github.com/pavitrapandey/electronic-store-backend)
- 🛠️ **Local API Documentation**: `http://localhost:9090/swagger-ui.html`
- 📋 **API Docs JSON**: `/v3/api-docs`
---
## 👨‍💻 Author
Made with ❤️ by [Pavitra Pandey](https://github.com/pavitrapandey)
