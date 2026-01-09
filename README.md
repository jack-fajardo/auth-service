# AuthService

**AuthService** is a Spring Boot microservice that handles **user registration and authentication**. It issues **JWT tokens** for authenticated users, which can be consumed by other microservices (like DocuVault API) to secure endpoints.

---

## Table of Contents

1. [Features](#features)  
2. [Tech Stack](#tech-stack)  
3. [Setup & Running](#setup--running)  
4. [Environment Variables](#environment-variables)  
5. [API Endpoints](#api-endpoints)  
    - [Register](#register)  
    - [Login](#login)  
6. [JWT Flow](#jwt-flow)  
7. [Password Security](#password-security)  
8. [Future Extensions](#future-extensions)  

---

## Features

- **User Registration**: Create new users with unique username and email.  
- **Login**: Authenticate users and issue JWT tokens.  
- **JWT-based authentication**: Secure other microservices using issued tokens.  
- **Secure password hashing**: Uses `BCryptPasswordEncoder` from Spring Security.  
- **Spring Security integration** for authentication, password encoding, and optional roles.  

---

## Tech Stack

- **Java 17**  
- **Spring Boot 3**  
- **Spring Security**  
- **PostgreSQL** (Dockerized)  
- **JWT** (`io.jsonwebtoken`)  
- **Docker & Docker Compose** for containerized development  

---

## Setup & Running

**Prerequisites**

- Docker Desktop should be installed on your machine and Docker Engine should be actively running.

1. **Clone the repository**

```bash
git clone https://github.com/jack-fajardo/auth-service.git
cd auth-service
```

2. **Run with Docker Compose**

```bash
docker-compose up --build
``` 

This will spin up:

- AuthService on `http://localhost:8081`
- PostgreSQL database on port `5433`

3. **Dev Workflow**

- Hot reload is enabled. Changes in `src/` will auto-restart the service.  
- Dependencies are cached — no repeated downloads on rebuilds.  

---

## API Endpoints

### Register

**POST** `/auth/register`

Registers a new user with username, email, and password.

**Request Body (JSON)**:

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "passwordHash": "mypassword123"
}
```

**Response (200 OK)**:

```json
"User registered with id: 1"
```

**Behavior:**

- Validates **unique username** and **email**.  
- Hashes the password using BCrypt before saving.  
- Stores user in `app_user` table in PostgreSQL.  

---

### Login
## 🔐 POST `/auth/login`

Authenticates an existing user using **Spring Security’s authentication pipeline** and issues a **one-time authorization code**.  
This code is later exchanged for a JWT token via `/auth/exchange`.

> ⚠️ This endpoint **does NOT return a JWT directly**.  
> It intentionally returns a **short-lived, single-use code** as part of an OAuth-style flow.

---

### Request Body (JSON)

```json
{
  "username": "john_doe",
  "password": "mypassword123"
}
```

---

### Response — Success (200 OK)

```json
{
  "code": "c6b0e9d1-7e3a-4e8b-b6d9-2f8b9c1a3e4f"
}
```

**The returned `code`:**
- is randomly generated
- expires in ~60 seconds
- can be used only once
- is **not** a JWT

The client must redirect the user to the dashboard and exchange this code via:

```bash
POST /auth/exchange
```

---

### Response — Error (401 / 403)

```json
{
  "message": "Invalid username or password",
  "status": 401,
  "timestamp": "2026-01-09T12:34:56Z"
}
```

**Possible error cases:**
- Invalid username or password
- Account disabled
- Account locked
- Account expired

---

## 🔄 Authentication Flow

### 🧠 How authentication works (important)

Although the controller implementation is minimal, authentication is fully handled by **Spring Security**:

1. The controller creates a `UsernamePasswordAuthenticationToken`
2. `AuthenticationManager.authenticate(...)` is invoked
3. Spring Security internally:
   - loads the user via `UserDetailsService`
   - verifies the password using `PasswordEncoder`
   - checks account status (locked, disabled, expired, etc.)

### If authentication fails:
- an exception is thrown
- execution stops immediately
- a global `@RestControllerAdvice` converts the exception into a JSON error

### If authentication succeeds:
- execution continues
- a one-time authorization code is generated and returned

### The controller itself does **not**:
- manually look up users
- compare passwords
- check account flags
- return error responses

This design keeps controllers clean and ensures consistent, centralized authentication behavior.


## Password Security

- Passwords are **never stored in plaintext**.  
- Uses `BCryptPasswordEncoder` from Spring Security.  
- Automatically salts passwords and provides secure hashing.  

---

This README is designed to:

- Explain **all endpoints clearly**  
- Describe **JWT flow** for other microservices  
- Provide **setup instructions** for Dockerized dev workflow  
- Be useful for both **frontend and backend developers**
