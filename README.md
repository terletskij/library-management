# Library Management
REST service for library management using Spring Boot, JPA, Spring Validator, PostgreSQL, JUnit and Mockito.

## 🚀 Roadmap

### 1. Project Configuration
  - [ ] Create project structure
  - [ ] Configure `PostgreSQL` connection
  - [ ] Provide project running via `docker-compose.yml`
### 2. Entity & Validation
  - [ ] Implement `Book` and `Member` entity
  - [ ] Add validation for `Book` attributes: `name` (required, capital letter, min 3 symbols), `author` (required, two capitalized words, e.g. "Paulo Coelho")
  - [ ] Add validation for `Member` attributes: `name` (required)
### 3. Business logic
  - [ ] Add `BookRepository` & `MemberRepository`, both should extend `JpaRepository`
  - [ ] Implement `BookService` for Books `CRUD` operations
  - [ ] Implement `MemberService` for Library Members CRUD operations
### 4. Controller
  - [ ] Implement basic `CRUD` endpoints for both `BookController` and `MemberController` <br>
  #### Implement special endpoints in Book Controller:
  - [ ] Retrieve all books borrowed by a specific `member by name`
  - [ ] Retrieve all borrowed distinct book `names`
  - [ ] Retrieve all borrowed distinct book `names` and `amount` how much copy of this book was borrowed
### 5. Tests
  - [ ] Cover all scenarios with `unit` tests for services
  - [ ] Test duplicate book handling
  - [ ] Test borrow/return logic and limits
  - [ ] Test deletion restrictions
### 6. Documentation
  - [ ] Provide endpoints documentation via `OpenAPI/Swagger`
  - [ ] Add how-to-run project steps in `README`
