# Spring Boot JWT & Redis Authentication Template

This project is a Spring Boot-based authentication template that uses JWT (JSON Web Token) for authentication and Redis for token management.

## ✨ Features

-   User Registration
-   User Login
-   JWT (Access Token & Refresh Token) issuance
-   Token reissue using Refresh Token
-   Authentication using Spring Security

## 🛠️ Technologies Used

-   Java 17
-   Spring Boot 3.x
-   Spring Security
-   JWT (jjwt-api, jjwt-impl, jjwt-jackson)
-   Redis (Spring Data Redis)
-   H2 Database (or other configured database)
-   Gradle

## 🚀 Getting Started

### Prerequisites

-   Java 17 or higher
-   Gradle
-   Redis server

### Installation & Execution

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd login
    ```

2.  **Configure the application:**
    Open `src/main/resources/application.yaml` and configure the database (JPA), Redis, and JWT secret key settings.

    ```yaml
    spring:
      # ... (datasource, jpa settings)
      data:
        redis:
          host: localhost
          port: 6379
      # ...
    
    jwt:
      secret: <your-jwt-secret-key>
      # ... (token expiration settings)
    ```

3.  **Build the project:**
    ```bash
    ./gradlew build
    ```

4.  **Run the application:**
    ```bash
    java -jar build/libs/login-0.0.1-SNAPSHOT.jar
    ```

    The application will start on `http://localhost:8080`.

## 📖 API Endpoints

All endpoints are prefixed with `/api/auth`.

### 1. User Registration

-   **URL:** `POST /register`
-   **Description:** Registers a new user.
-   **Request Body:**
    ```json
    {
        "loginId": "user123",
        "password": "password123",
        "username": "Test User",
        "email": "user@example.com",
        "role": "USER"
    }
    ```
-   **Success Response (200 OK):**
    ```json
    {
        "loginId": "user123",
        "username": "Test User"
    }
    ```

### 2. User Login

-   **URL:** `POST /login`
-   **Description:** Authenticates a user and returns JWT tokens.
-   **Request Body:**
    ```json
    {
        "loginId": "user123",
        "password": "password123"
    }
    ```
-   **Success Response (200 OK):**
    ```json
    {
        "loginId": "user123",
        "username": "Test User",
        "token": "<access_token>",
        "expireTime": 3600000,
        "refreshToken": "<refresh_token>",
        "role": "USER"
    }
    ```

### 3. Token Reissue

-   **URL:** `POST /reissue`
-   **Description:** Reissues an Access Token using a Refresh Token.
-   **Request Body:**
    ```json
    {
        "refreshToken": "<your_refresh_token>"
    }
    ```
-   **Success Response (200 OK):**
    ```json
    {
        "accessToken": "<new_access_token>",
        "refreshToken": "<new_refresh_token>"
    }
    ```

### 4. Authenticated Endpoint Example

-   **URL:** `GET /home`
-   **Description:** An example endpoint that requires authentication.
-   **Headers:**
    -   `Authorization`: `Bearer <access_token>`
-   **Success Response (200 OK):**
    ```
    Login Success
    ```

---
