# User Management using JPA (Java Presistance API)🚀  
RESTful CRUD service built with **Spring Boot 3 + Spring Data JPA + PostgreSQL**.

<img src="https://github.com/user-attachments/assets/08dbaf74-b1a5-4a34-b453-b5947d7793d0" width="250"/>  
<img src="https://github.com/user-attachments/assets/eb4cd345-85c9-4af0-8c1c-d0fc1a3f866f" width="250"/>  
<img src="https://github.com/user-attachments/assets/cc8e8733-d865-4ec9-9105-4694d3ce07e1" width="250"/>

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

## 🏗 Tech Stack

| Layer | Library/Tool |
|-------|--------------|
| Runtime | Java 24+ |
| Framework | Spring Boot, Spring Data JPA |
| Build | Maven |
| Database | PostgreSQL |
| Validation | Jakarta Validation |
| Dev‑time | Spring DevTools |

---

## ⚡️ Quick Start

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

## 📖 API Reference

| Method   | Endpoint          | Body / Params                                    | Purpose             |
| -------- | ----------------- | ------------------------------------------------ | ------------------- |
| `POST`   | `/api/users`      | `[{ "name": "Jane", "email": "jane@x.com" }, …]` | Bulk‑create users   |
| `GET`    | `/api/users`      | –                                                | List all users      |
| `GET`    | `/api/users/{id}` | –                                                | Fetch one user      |
| `PUT`    | `/api/users/{id}` | `{ "name": "New", "email": "new@x.com" }`        | Update a user       |
| `DELETE` | `/api/users/{id}` | –                                                | Remove a user       |
| `GET`    | `/api/users/page` | `page, size, sort`                               | Paged & sorted list |

> All responses are JSON; validation errors return HTTP `400` with a human‑readable message.

---

## 🗄 Database Schema

```text
users
├─ id          BIGSERIAL PK
├─ name        VARCHAR(100)  NOT NULL
├─ email       VARCHAR(255)  NOT NULL UNIQUE
├─ created_at  TIMESTAMP     DEFAULT now()
└─ updated_at  TIMESTAMP
```

JPA/Hibernate creates (or updates) the table automatically thanks to
`spring.jpa.hibernate.ddl-auto=update`.

---

## 🗂 Project Layout

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

---

## FrontEnd Integration
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

### Author

**Khushi Raj** – *B.Tech CSE*

> Thanks for checking out the project. 


