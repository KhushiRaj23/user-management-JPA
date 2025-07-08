# User Management using JPA (Java Presistance API)🚀  
RESTful CRUD service built with **Spring Boot 3 + Spring Data JPA + PostgreSQL**.

<img src="https://github.com/user-attachments/assets/08dbaf74-b1a5-4a34-b453-b5947d7793d0" width="250"/>  
<img src="https://github.com/user-attachments/assets/eb4cd345-85c9-4af0-8c1c-d0fc1a3f866f" width="250"/>  
<img src="https://github.com/user-attachments/assets/cc8e8733-d865-4ec9-9105-4694d3ce07e1" width="250"/>

---
## 📑 Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Quick Start](#quick-start)
4. [API Reference](#api-reference)
5. [Database Schema](#database-schema)
6. [Project Layout](#project-layout)
7. [Testing](#testing)
8. [FrontEnd Integration](#frontend-integration)
9. [Lombok](#lombok)
10. [Service — Authentication & Authorization](#service)
11. [JWT Authentication](#jwt-authentication)
12. [Author](#author)


---

## ✨ Features
| Capability | Details |
|------------|---------|
| **Bulk create** | `POST /api/users` accepts a JSON _array_ and persists all rows in one call (internally uses `saveAll`). |
| **CRUD endpoints** | Create, read, update, delete individual users. |
| **Pagination & sorting** | `GET /api/users/page?page=0&size=10&sort=name,asc` leverages Spring Pageable. |
| **Validation** | Jakarta Bean Validation enforces name length, email format, etc. |
| **Timestamps** | `created_at` & `updated_at` columns are auto‑populated with `@CreationTimestamp` / `@UpdateTimestamp`. |
| **Search helpers** | Repository methods like `findByEmail` & `findByNameContainingIgnoreCase`. |

---

## 🏗 Tech-Stack

| Layer | Library/Tool |
|-------|--------------|
| Runtime | Java 24+ |
| Framework | Spring Boot, Spring Data JPA |
| Build | Maven |
| Database | PostgreSQL |
| Validation | Jakarta Validation |
| Dev‑time | Spring DevTools |

---

## ⚡Quick-Start

```bash
# 1 Clone
git clone https://github.com/KhushiRaj23/user-management-JPA.git
cd user-management-JPA

# 2 Configure DB
# Either edit src/main/resources/application.properties
# or export variables before running (preferred):
export DB_URL=jdbc:postgresql://localhost:5432/userdb
export DB_USER=postgres
export DB_PASS=12345

# 3 Boot the app
./mvnw spring-boot:run          # hot‑reload dev mode
# or
./mvnw clean package
java -jar target/usermanagementapi-0.0.1-SNAPSHOT.jar
````

The API starts on **[http://localhost:8080](http://localhost:8080)** by default.

---

## 📖 API-Reference

| Method   | Endpoint          | Body / Params                                    | Purpose             |
| -------- | ----------------- | ------------------------------------------------ | ------------------- |
| `POST`   | `/api/users`      | `[{ "name": "Jane", "email": "jane@x.com" }, …]` | Bulk‑create users   |
| `GET`    | `/api/users`      | –                                                | List all users      |
| `GET`    | `/api/users/{id}` | –                                                | Fetch one user      |
| `PUT`    | `/api/users/{id}` | `{ "name": "New", "email": "new@x.com" }`        | Update a user       |
| `DELETE` | `/api/users/{id}` | –                                                | Remove a user       |
| `GET`    | `/api/users/page` | `page, size, sort`                               | Paged & sorted list |

> All responses are JSON; validation errors return HTTP `400` with a human‑readable message.

[Check User Controller](src/main/java/com/user/usermanagementapi/controller/UserController.java)

---

## 🗄 Database-Schema

```text
users
├─ id          BIGSERI(AL PK
├─ name        VARCHAR(100)  NOT NULL
├─ email       VARCHAR(255)  NOT NULL UNIQUE
├─ created_at  TIMESTAMP     DEFAULT now()
└─ updated_at  TIMESTAMP
```

JPA/Hibernate creates (or updates) the table automatically thanks to
`spring.jpa.hibernate.ddl-auto=update`.

[Check users entity](src/main/java/com/user/usermanagementapi/model/User.java)

---

## 🗂 Project-Layout

```
src
└── main
    ├── java
    │   └── com.user.usermanagementapi
    │       ├── UsermanagementapiApplication.java   # SpringBoot main class
    │       ├── controller
    │       │   └── UserController.java            # REST endpoints
    │       ├── model
    │       │   └── User.java                      # JPA entity
    │       └── repository
    │           └── UserRepository.java            # JpaRepository helpers
    └── resources
        └── application.properties
```
---

## 🧪Testing

| Layer under test | Technology | Notes |
|------------------|------------|-------|
| Web + Service    | **JUnit 5** (`spring-boot-starter-test`) | Full Spring context bootstrapped per class |
| HTTP layer       | **MockMvc** | Simulates real requests without opening a port |
| JSON handling    | **Jackson (ObjectMapper)** | Serialises/deserialises request/response bodies |
| Database         | **Spring Data JPA** with the same profile used in dev | Each test starts with a clean table (`@BeforeEach` → `repository.deleteAll()`) |

### 👩🏻‍💻UserControllerTest (Testing branch)

### File Structure:

```
user-management-api/                     ← project root
│
├── pom.xml                               ← Maven build file
│
├── src/
│   ├── main/
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── user/
│       │           └── usermanagementapi/
│       │                └── UserControllerTest.java   ← **<‑‑ the file in question**
│       │
```



| Test class | Scenario | Endpoint exercised |
|------------|----------|--------------------|
| `UserControllerTest` | **Get one user (found)** | `GET /api/users/{id}` |
|                     | **Bulk create users** | `POST /api/users` |
|                     | **Delete user (found)** | `DELETE /api/users/{id}` |
|                     | **Delete user (not found)** | `DELETE /api/users/{id}` (non‑existent) |

> All of the above endpoints are part of the public API.

![Screenshot 2025-07-04 165642](https://github.com/user-attachments/assets/460bb1eb-1f71-4c2a-b5ad-9330d086e09a)

[Check UserControllerTesting](src/test/java/com/user/usermanagementapi/UserControllerTest.java)

---

## FrontEnd-Integration
[Check this frontendreadme.md](https://github.com/KhushiRaj23/user-management-JPA/blob/frontEnd-Integration/FRONTENDREADME.md)

## 🪄Lombok 
Lombok achieves reducing boilerplate code by generating the code automatically during compilation based on annotations you add to your java classes.

- Add this in your pom.xml
```
<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<version>1.18.38</version>
		<scope>provided</scope>
	</dependency>
```
- add Lombok plugin (if using intelliJ)
- check User.java for usages
  | Note: Java 24 may not support Lombok try using java 17 


## 👨‍🔧Service
### Service Layer — Authentication & Authorization

| Component            | Purpose                                        | Key Points                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| -------------------- | ---------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`SecurityConfig`** | Central Spring‑Security configuration.         | \* Registers a custom `UserDetailsService`.<br>\* Uses **BCrypt** to hash passwords.<br>\* Declares a `DaoAuthenticationProvider` for DB‑backed login.<br>\* Enables CORS for any origin and disables CSRF (pure REST).<br>\* Exposes a stateless **HTTP Basic** filter chain:<br>  \* `POST/DELETE /api/users/**` → `ROLE_ADMIN` only<br>  \* `GET   /api/users/**` → `ROLE_USER` or `ROLE_ADMIN`<br>  \* `/api/users/register`, `/api/users/page` are public. |
| **`UserDetail`**     | Custom implementation of `UserDetailsService`. | \* Looks up a user by **email** and maps it to Spring‑Security’s `User` object.<br>\* Converts every role string in `user_roles` to a `SimpleGrantedAuthority`.                                                                                                                                                                                                                                                                                                 |
| **`User` Entity**    | JPA model with Lombok to remove boilerplate.   | \* Fields: `id`, `name`, `email`, `password`, timestamps.<br>\* Roles stored as an eager `@ElementCollection` (`user_roles` table).<br>\* Passwords are saved **already hashed** (`BCryptPasswordEncoder`).                                                                                                                                                                                                                                                     |

> **Result:** the API is protected end‑to‑end: credentials are securely hashed, every request is authorised by role, and no server‑side session is kept (perfect for frontend/SPAs or mobile clients).

![create Users](https://github.com/user-attachments/assets/c158e704-fb45-4b0a-904d-191b6a397158)

[Check SecurityConfig](src/main/java/com/user/usermanagementapi/service/SecurityConfig.java)<br></br>
[Check UserDetail](src/main/java/com/user/usermanagementapi/service/UserDetail.java)

---

## 🔐 JWT Authentication

This project secures all protected REST endpoints with **JSON Web Tokens (JWT)**.
A short overview of how it works in this codebase:

| Layer               | File                     | Purpose                                                                                                       |
| ------------------- | ------------------------ | ------------------------------------------------------------------------------------------------------------- |
| **Token creation**  | `JwtService.java`        | Generates signed HS‑256 tokens containing the user’s *username* and a `roles` claim.                          |
| **Request filter**  | `JwtAuthFilter.java`     | Intercepts every request, extracts the “Bearer \<token>” header, validates it, and loads user details.        |
| **Security config** | `SecurityConfig.java`    | Wires the filter into Spring Security, disables sessions (stateless), and opens only the *register* endpoint. |
| **Unit tests**      | `JwtServiceTesting.java` | Verifies token generation, extraction, positive validation, and tamper detection.                             |

---

### 1  Configure your secret key

Add a **256‑bit base‑64 key** and an expiry time (ms) in `application.properties`:

```properties
# 32 raw bytes → 44‑char base64; generate once with `openssl rand -base64 32`
jwt.secret     = P7S1M6Q7B4NzKyKYa9FX3qEbAAk+WlCwTOhHfvF+Qjw=
jwt.expiration = 14400000      # 4 hours
```

> **Prod tip:** set `JWT_SECRET` as an environment variable instead of hard‑coding it.

---

### 2  How a token is built

```java
String token = Jwts.builder()
    .subject(username)                             // → Claims::getSubject
    .claims(Map.of("roles", rolesSet))             // custom claim
    .issuedAt(now)
    .expiration(now.plus(expiration))
    .signWith(secretKey, SignatureAlgorithm.HS256) // HS‑256
    .compact();
```

*See `JwtService#createToken(…)` for the full code.*

---

### 3  Using the token

1. **Acquire** – after a successful login (or registration) the backend returns:

   ```json
   { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
   ```

2. **Send on every call** – add an HTTP header:

   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

3. **Result** – `JwtAuthFilter` verifies the signature and populates
   `SecurityContextHolder`. Downstream controllers can use the usual
   `@PreAuthorize("hasRole('ROLE_ADMIN')")`, etc.

---

### 4  Running the unit tests

```bash
# Maven
mvn clean test
```

The suite (`JwtServiceTesting`) covers:

* **Token generated** ‑ not blank.
* **Username extraction** ‑ round trip equals original username.
* **Happy path validation** ‑ fresh token ✓.
* **Negative path validation** ‑ tampered token ✗.

![JwtService Testing](https://github.com/user-attachments/assets/f08d44a8-df6a-4ab2-8170-fc4497c21118)

[Check JwtService](src/main/java/com/user/usermanagementapi/service/JwtService.java) <br></br>
[check JwtServiceTest](src/test/java/com/user/usermanagementapi/JwtServiceTesting.java)

---

### Author

**Khushi Raj** – *B.Tech CSE*

> Thanks for checking out the project. 


