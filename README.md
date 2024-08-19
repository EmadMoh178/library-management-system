# Library Management System

## Project Overview

The Library Management System is a RESTful API application designed to manage books, patrons, and borrowing records for a library. The system enables users to perform CRUD operations on books and patrons, and facilitates the borrowing and returning of books by patrons.

## Features

- **Caching**: Utilizes Spring’s caching mechanisms to improve system performance by caching frequently accessed data such as book details and patron information.
- **Aspect-Oriented Programming (AOP)**: Implements logging using AOP to track method calls and performance metrics of operations.
- **JWT Authorization**: Protects endpoints with JWT-based authorization to ensure secure access to resources. Users must authenticate to receive a token which is required for accessing protected endpoints.
- **Transaction Management**: Uses Spring’s `@Transactional` annotation to manage transactions and ensure data integrity during critical operations.

## Demo

Check out the demo!

https://github.com/user-attachments/assets/5d1b4534-b138-4e6f-98ac-bb03d5afced8

## Project Setup

### Prerequisites

- Java 17 or later
- Maven
- PostgreSQL

### Clone the Repository

```bash
git clone https://github.com/EmadMoh178/library-management-system.git
cd library-management-system
```

### Create `.env` File

Create a file named `.env` at the root of the project and add your PostgreSQL credentials:

```
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_jwt_secret_key
```

### Database Setup

1. **Access PostgreSQL**:

   ```bash
   sudo -u postgres psql
   ```

2. **Create the Database**:

   ```sql
   CREATE DATABASE library_management;
   ```

3. **Create a New User and Grant Privileges**:

   ```sql
   CREATE USER your_postgres_username WITH PASSWORD 'your_postgres_password';
   GRANT ALL PRIVILEGES ON DATABASE library_management TO your_postgres_username;
   \c library_management;
   GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_postgres_username;
   GRANT CREATE ON SCHEMA public TO your_postgres_username;
   ```

Replace `your_postgres_username` and `your_postgres_password` with your preferred credentials.

### Build and Run

1. **Build the Project**:

   ```bash
   mvn clean install
   ```

2. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

All endpoints are prefixed with `http://localhost:8080/api`.

### User Management

- **POST** `/auth/signup`

  Register a new user.

  Request Body:

  ```json
  {
    "email": "user@example.com",
    "password": "userpassword",
    "name": "User Name"
  }
  ```

- **POST** `/auth/login`

  Authenticate and obtain a JWT token.

  Request Body:

  ```json
  {
    "email": "user@example.com",
    "password": "userpassword"
  }
  ```

- **POST** `/auth/logout`

  Log out a user. This endpoint requires a JWT token to be sent as a bearer token in the request header.

### Authorization Note

**Important**: All endpoints other than the ones related to user authentication (`/auth/signup`, `/auth/login`, and `/auth/logout`) require a JWT token for access. To interact with these protected endpoints, you must first register and log in to receive a JWT token. Include this token in the `Authorization` header as a Bearer token when making requests to these endpoints.


### Book Management

- **GET** `/books`

  Retrieve a list of all books.

- **GET** `/books/{id}`

  Retrieve details of a specific book by ID.

- **POST** `/books`

  Add a new book to the library.

  Request Body:

  ```json
  {
    "title": "Sample Book Title",
    "author": "John Doe",
    "publicationYear": 2024,
    "isbn": "1234567890123"
  }
  ```

- **PUT** `/books/{id}`

  Update an existing book's information.

  Request Body:

  ```json
  {
    "title": "Updated Book Title",
    "author": "Jane Doe",
    "publicationYear": 2025,
    "isbn": "0987654321098"
  }
  ```

- **DELETE** `/books/{id}`

  Remove a book from the library.

### Patron Management

- **GET** `/patrons`

  Retrieve a list of all patrons.

- **GET** `/patrons/{id}`

  Retrieve details of a specific patron by ID.

- **POST** `/patrons`

  Add a new patron to the system.

  Request Body:

  ```json
  {
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "phoneNumber": "+1234567890",
    "membershipDate": "2024-08-10"
  }
  ```

- **PUT** `/patrons/{id}`

  Update an existing patron's information.

  Request Body:

  ```json
  {
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phoneNumber": "+0987654321",
    "membershipDate": "2024-09-15"
  }
  ```

- **DELETE** `/patrons/{id}`

  Remove a patron from the system.

### Borrowing

- **POST** `/borrow/{bookId}/patron/{patronId}`

  Allow a patron to borrow a book.

- **PUT** `/return/{bookId}/patron/{patronId}`

  Record the return of a borrowed book by a patron.

## Testing

You can use tools like Postman to test the API endpoints.

## Troubleshooting

- Ensure PostgreSQL is running and accessible.
- Verify that the `.env` file contains the correct credentials.
