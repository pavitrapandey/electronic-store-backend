# 🛒 Electronic Store Backend

A Spring Boot based backend for an electronic store application, built with a modular structure and RESTful APIs. This project covers user management, category handling, and full-fledged product operations.

## 🚀 Features

### ✅ User Module
- Create user with:
  - Name, Email, Password, Gender, About, ImageName
- Fully tested and validated

### 🗂️ Category Module
- CRUD operations on categories
- Upload and serve category **cover images**

### 📦 Product Module
- Add/Update/Delete/Get Product
- Get All Live Products
- Get/Delete Products Between Price Range
- Create/Update/Delete Products by Category

### 🛒 Cart and Cart item Module
- Add/Update the Product CartItem
- Delete the CartItem
- Get The Cart By User

### 💳Order Module
- Create/Get Order
- Update Order Status
- Remove Order

### 🔒 Additional Enhancements
- Input Validation
- Custom Exception Handling
- Pagination and Sorting

## 🛠️ Tech Stack
- Java 17
- Spring Boot
- Spring Data Jpa/Hibernate
- Lombok
- Maven
- Mysql
- Postman


## 📂 Image Uploads
- Images are stored and served using static resource mapping.
- `imageName` is used to identify and retrieve the file.

## 📞 API Testing
- Use Postman or Swagger (if integrated) to test the endpoints.
- All APIs follow RESTful standards.

## 🔗 GitHub Repository
[🔗 Click here to view the repository](https://github.com/pavitrapandey/electronic-store-backend)

---

## 🤝 Contributing
Pull requests are welcome! Feel free to fork and raise issues or suggest improvements.

---

## 📌 Upcoming Features
- Security
- Deployment on Cloud

---

## 👨‍💻 Author
Made with ❤️ by [Pavitra Pandey](https://github.com/pavitrapandey)
