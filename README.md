# 🛒 Electronic Store Backend

A Spring Boot based backend for an electronic store application, built with a modular structure and RESTful APIs.  
This project includes user authentication with JWT and secure token handling, along with features like user management, category handling, and full-fledged product operations.

---

## 🚀 Features

### ✅ User Module
- Register user with:
  - Name, Email, Password, Gender, About, ImageName
- Login with JWT token
- Fully tested and validated

### 🔐 Authentication & Security
- Integrated **Spring Security**
- **JWT Token-based Authentication**
- **Refresh Token** mechanism for access token renewal
- Secure endpoints with role-based access control (RBAC)

### 🗂️ Category Module
- CRUD operations on categories
- Upload and serve category **cover images**

### 📦 Product Module
- Add/Update/Delete/Get Product
- Get All Live Products
- Get/Delete Products Between Price Range
- Create/Update/Delete Products by Category

### 🛒 Cart and CartItem Module
- Add/Update Product to CartItem
- Delete CartItem
- Get Cart by User

### 💳 Order Module
- Create/Get Order
- Update Order Status
- Remove Order

### 🔧 Additional Enhancements
- Input Validation
- Custom Exception Handling
- Pagination and Sorting

---

## 🛠️ Tech Stack
- Java 17
- Spring Boot
- Spring Security
- JWT (Access + Refresh Tokens)
- Spring Data JPA/Hibernate
- Lombok
- Maven
- MySQL
- Postman

---

## 📂 Image Uploads
- Images are stored and served using static resource mapping.
- `imageName` is used to identify and retrieve the file.

---

## 📞 API Testing
- Use **Postman** or **Swagger UI** (if integrated) to test the endpoints.
- Authentication-protected endpoints require Bearer Token in headers.
- All APIs follow RESTful standards.

---

## 🔗 GitHub Repository
[🔗 Click here to view the repository](https://github.com/pavitrapandey/electronic-store-backend)

---

## 🤝 Contributing
Pull requests are welcome!  
Feel free to fork, raise issues, or suggest improvements.

---

## 📌 Upcoming Features
-**Api Documentation** (using Swagger)
- **Cloud Deployment** (AWS/GCP)

---

## 👨‍💻 Author
Made with ❤️ by [Pavitra Pandey](https://github.com/pavitrapandey)

