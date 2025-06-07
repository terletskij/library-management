# 📚 Library Management
REST service for library management using Spring Boot, JPA, Spring Validator, PostgreSQL, JUnit and Mockito.


---

## 📑 Table of Contents

- [Tech Stack / Requirements](#-tech-stack--requirements)
- [Getting Started](#-getting-started)
- [API Features](#-api-features)
- [Running Tests](#-running-tests)
- [Developer Notes](#-developer-notes)

---

## 🛠️ Tech Stack / Requirements

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker / Docker Compose**
- **JUnit 5 / Mockito**
- **Swagger (OpenAPI)**

---

## 🚀 Getting Started

### 1. 📥 Clone the Repository

```
git clone https://github.com/terletskij/library-management.git
cd library-management
```
### 2. ⚙️ Set Up Environment Variables
Create a `.env` file in the root of project
```
DATABASE_USER=user-name
DATABASE_PASSWORD=user-password
DATABASE_NAME=librarydb

MAX_BORROWED_BOOKS=10
```
> [!NOTE]
> You can customize MAX_BORROWED_BOOKS to set how many books a member is allowed to borrow at one time.

### 3. 🐳 Run with Docker
```
docker-compose up --build
```
This will:
- Start a PostgreSQL container
- Build and start the Spring Boot application

## 🌐 Access the API
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

OpenAPI Docs: `http://localhost:8080/v3/api-docs`

## 📘 API Features
- CRUD for Books (/api/v1/books)

- CRUD for Members (/api/v1/members)

- Borrow and return books (/api/v1/borrow)

- Retrieve borrowed books by member ID or name

- Get list of all currently borrowed book titles

- Get borrowed book titles with count

- Validation for input data (name formatting, capital letters)

- Unit testing for services

## 🧪 Running Tests
You can run all unit tests using:
```
./mvnw test
```

## 📝 Developer Notes
### 🧰 Manual (non-Docker) Startup
If you want to run the application manually:

Make sure `PostgreSQL` is running

Configure `application.properties` with your DB credentials

Start the app with:
```
./mvnw spring-boot:run
```


## 🚀 Roadmap

### 1. Project Configuration
  - [x] Create project structure
  - [x] Configure `PostgreSQL` connection
  - [x] Provide project running via `docker-compose.yml`
### 2. Entity & Validation
  - [x] Implement `Book` and `Member` entity
  - [x] Add validation for `Book` attributes: `name` (required, capital letter, min 3 symbols), `author` (required, two capitalized words, e.g. "Paulo Coelho")
  - [x] Add validation for `Member` attributes: `name` (required)
### 3. Business logic
  - [x] Add `BookRepository` & `MemberRepository`, both should extend `JpaRepository`
  - [x] Implement `BookService` for Books `CRUD` operations
  - [x] Implement `BorrowService` for borrow management
  - [x] Implement `MemberService` for Library Members CRUD operations
### 4. Controller
  - [x] Implement basic `CRUD` endpoints for both `BookController` and `MemberController` <br>
  #### Implement special endpoints in Book Controller:
  - [x] Retrieve all books borrowed by a specific `member by name`
  - [x] Retrieve all borrowed distinct book `names`
  - [x] Retrieve all borrowed distinct book `names` and `amount` how much copy of this book was borrowed
### 5. Tests
  - [x] Cover all scenarios with `unit` tests for services
  - [x] Test duplicate book handling
  - [x] Test borrow/return logic and limits
  - [x] Test deletion restrictions
### 6. Documentation
  - [x] Provide endpoints documentation via `OpenAPI/Swagger`
  - [x] Add how-to-run project steps in `README`
