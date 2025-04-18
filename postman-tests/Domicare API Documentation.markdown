# Domicare API Documentation

## Overview
The **Domicare API** provides a comprehensive set of endpoints to manage users, roles, permissions, products, categories, bookings, reviews, and file uploads for the Domicare application. This API is designed for a service-oriented platform, likely focused on home care or e-commerce services. The API follows the **OpenAPI 3.0.1** specification and is licensed under **Apache 2.0**.

- **Base URL**: `http://localhost:8080` (development server)
- **Version**: 1.0.0
- **Purpose**: Facilitate user management, service bookings, product browsing, and administrative tasks with role-based access control (RBAC).

## Authentication
The API supports multiple authentication mechanisms:
- **Email/Password Login**: Use `POST /login` to obtain access and refresh tokens.
- **Token Refresh**: Use `POST /refresh-token` to refresh an access token.
- **OAuth2**: Use `GET /auth/callback` to exchange an authorization code.
- **Email Verification**: Required for new users via `GET /verify-email`.
- **Password Reset**: Supported via `POST /reset-password` and `GET /email/reset-password`.

**Note**: Most endpoints require authentication (e.g., Bearer tokens). Public endpoints (e.g., `/api/public/products`) are accessible without authentication.

## Key Endpoints
The API is organized into controllers, each handling specific functionalities. Below is a summary of the main endpoints grouped by controller.

### 1. User Controller
Manages user accounts and roles.

| Method | Endpoint                | Description                              | Request Body                     | Response                     |
|--------|-------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/users`                | Retrieves a paginated list of users      | -                                | `ResultPagingDTO`            |
| PUT    | `/users`                | Updates user details                     | `UpdateUserRequest`              | `UserDTO`                    |
| PUT    | `/users/roles`          | Updates roles for a user                 | `UpdateRoleForUserRequest`       | `UserDTO`                    |
| GET    | `/users/{id}`           | Fetches a user by ID                     | -                                | `UserDTO`                    |
| DELETE | `/users/{id}`           | Deletes a user by ID                     | -                                | -                            |

**Query Parameters for GET /users**:
- `page` (optional, default: 1)
- `size` (optional, default: 20)
- `searchName` (optional)
- `sortBy` (optional, default: `id`)
- `sortDirection` (optional, default: `asc`)
- `spec` (required, `SpecificationUser`)
- `pageable` (required, `Pageable`)

### 2. Role Controller
Manages roles for access control.

| Method | Endpoint                | Description                              | Request Body                     | Response                     |
|--------|-------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/roles`                | Retrieves all roles                      | -                                | Object                       |
| POST   | `/roles`                | Creates a new role                       | `RoleDTO`                        | `RoleDTO`                    |
| PUT    | `/roles`                | Updates a role                           | `RoleDTO`                        | `RoleDTO`                    |
| GET    | `/roles/{id}`           | Fetches a role by ID                     | -                                | Object                       |
| DELETE | `/roles/{id}`           | Deletes a role by ID                     | -                                | -                            |

### 3. Permission Controller
Manages permissions for RBAC.

| Method | Endpoint                | Description                              | Request Body                     | Response                     |
|--------|-------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/permissions`          | Retrieves permissions                    | -                                | Object                       |
| POST   | `/permissions`          | Creates a new permission                 | `Permission`                     | `Permission`                 |
| PUT    | `/permissions`          | Updates a permission                     | `Permission`                     | `Permission`                 |
| GET    | `/permissions/{id}`     | Fetches a permission by ID               | -                                | Object                       |
| DELETE | `/permissions/{id}`     | Deletes a permission by ID               | -                                | -                            |

**Query Parameters for GET /permissions**:
- `search` (optional)
- `pageable` (required, `Pageable`)

### 4. Product Controller
Manages products and their images.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| POST   | `/api/products`              | Creates a new product                    | `AddProductRequest`              | Object                       |
| PUT    | `/api/products`              | Updates a product                        | `UpdateProductRequest`           | Object                       |
| DELETE | `/api/products/{id}`         | Deletes a product by ID                  | -                                | Object                       |
| PUT    | `/api/products/images`       | Uploads a product image                  | `AddProductImageRequest`         | Object                       |
| GET    | `/api/public/products`       | Retrieves a paginated list of products   | -                                | Object                       |
| GET    | `/api/public/products/{id}`  | Fetches a product by ID                  | -                                | Object                       |

**Query Parameters for GET /api/public/products**:
- `categoryId` (optional, default: 0)
- `page` (optional, default: 1)
- `size` (optional, default: 20)
- `searchName` (optional)
- `sortBy` (optional, default: `id`)
- `sortDirection` (optional, default: `asc`)
- `spec` (required, `SpecificationProduct`)
- `pageable` (required, `Pageable`)

### 5. Category Controller
Manages product categories.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| POST   | `/api/categories`            | Creates a new category                   | `AddCategoryRequest`             | Object                       |
| PUT    | `/api/categories`            | Updates a category                       | `UpdateCategoryRequest`          | Object                       |
| DELETE | `/api/categories/{id}`       | Deletes a category by ID                 | -                                | Object                       |
| GET    | `/api/public/categories`     | Retrieves a paginated list of categories | -                                | `ResultPagingDTO`            |
| GET    | `/api/public/categories/{id}`| Fetches a category by ID                 | -                                | Object                       |

**Query Parameters for GET /api/public/categories**:
- `page` (optional, default: 1)
- `size` (optional, default: 20)
- `sortBy` (optional, default: `id`)
- `sortDirection` (optional, default: `asc`)
- `spec` (required, `SpecificationCategory`)
- `pageable` (required, `Pageable`)

### 6. Booking Controller
Manages service bookings.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| POST   | `/api/bookings`              | Creates a new booking                    | `BookingRequest`                 | `BookingDTO`                 |
| PUT    | `/api/bookings`              | Updates a booking                        | `UpdateBookingRequest`           | `BookingDTO`                 |
| PUT    | `/api/bookings/status`       | Updates a booking status                 | `UpdateBookingStatusRequest`     | `BookingDTO`                 |
| GET    | `/api/bookings/{id}`         | Fetches a booking by ID                  | -                                | `BookingDTO`                 |
| DELETE | `/api/bookings/{id}`         | Deletes a booking by ID                  | -                                | -                            |

### 7. Review Controller
Manages product reviews.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/api/reviews`               | Retrieves a paginated list of reviews    | -                                | Object                       |
| POST   | `/api/reviews`               | Creates a new review                     | `ReviewRequest`                  | `ReviewDTO`                  |

**Query Parameters for GET /api/reviews**:
- `page` (optional, default: 1)
- `size` (optional, default: 20)
- `sortBy` (optional)
- `sortDirection` (optional, default: `asc`)
- `spec` (required, `SpecificationReview`)
- `pageable` (required, `Pageable`)

### 8. File Controller
Manages file uploads and retrieval via Cloudinary.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| POST   | `/api/cloudinary/files`      | Uploads a file                           | `{ "file": "binary" }`           | Object                       |
| GET    | `/api/cloudinary/files`      | Fetches a file by name                   | -                                | Object                       |
| GET    | `/api/cloudinary/files/{id}` | Fetches a file by ID                     | -                                | Object                       |
| DELETE | `/api/cloudinary/files/{id}` | Deletes a file by ID                     | -                                | Object                       |
| GET    | `/api/cloudinary/files/all`  | Retrieves all files                      | -                                | Object                       |

**Query Parameters for GET /api/cloudinary/files**:
- `name` (required)

### 9. Authentication Controller
Handles user authentication and registration.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| POST   | `/login`                     | Authenticates a user                     | `LoginRequest`                   | `LoginResponse`              |
| POST   | `/register`                  | Registers a new user                     | `RegisterRequest`                | `RegisterResponse`           |
| POST   | `/refresh-token`             | Refreshes an access token                | `RefreshTokenRequest`            | `RefreshTokenResponse`       |
| POST   | `/reset-password`            | Resets a user’s password                 | `UserDTO`                        | -                            |

### 10. Email Sending Controller
Manages email-related operations.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/email/verify`              | Sends an email verification link         | -                                | Object                       |
| GET    | `/email/reset-password`      | Sends a password reset email             | -                                | Object                       |

**Query Parameters**:
- `email` (required)

### 11. OAuth2 Controller
Handles OAuth2 authentication.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/auth/callback`             | Exchanges an authorization code           | -                                | Object                       |

**Query Parameters**:
- `code` (required)

### 12. View Controller
Handles email verification and password reset views.

| Method | Endpoint                     | Description                              | Request Body                     | Response                     |
|--------|------------------------------|------------------------------------------|----------------------------------|------------------------------|
| GET    | `/verify-email`              | Verifies a user’s email                  | -                                | String                       |
| GET    | `/forgot-password`           | Handles forgot password flow             | -                                | String                       |

**Query Parameters**:
- `token` (required)

## Schemas
The API defines several schemas for request and response bodies. Key schemas include:

- **UserDTO**: Represents a user with fields like `id`, `name`, `email`, `roles`, `gender`, and `dateOfBirth`.
- **RoleDTO**: Defines a role with `id`, `name`, and `description`.
- **Permission**: Represents a permission with `id`, `name`, `apiPath`, `method`, and `module`.
- **ProductDTO**: Describes a product with `id`, `name`, `price`, `discount`, and `reviewDTOs`.
- **CategoryDTO**: Represents a category with `id`, `name`, and associated `products`.
- **BookingDTO**: Defines a booking with `id`, `address`, `totalHours`, `bookingStatus`, and associated `products` and `userDTO`.
- **ReviewDTO**: Represents a review with `id`, `rating`, `comment`, and references to `userDTO` and `productId`.
- **ResultPagingDTO**: Used for paginated responses, containing `meta` (page, size, total) and `data`.

## Example Usage

### Authentication Controller

#### Register a New User
```bash
curl -X POST http://localhost:8080/register \
-H "Content-Type: application/json" \
-d '{
  "email": "user@example.com",
  "password": "Password123",
  "name": "John Doe",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01"
}'
```
**Response**:
```json
{
  "id": 1,
  "email": "user@example.com",
  "emailConfirmed": false,
  "emailConfirmationToken": "abc123",
  "roles": ["USER"],
  "name": "John Doe",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01"
}
```

#### Login
```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/json" \
-d '{
  "email": "user@example.com",
  "password": "Password123"
}'
```
**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "xyz789",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe"
  }
}
```

#### Refresh Token
```bash
curl -X POST http://localhost:8080/refresh-token \
-H "Content-Type: application/json" \
-d '{
  "refreshToken": "xyz789"
}'
```
**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.newToken...",
  "refreshToken": "abc456"
}
```

#### Reset Password
```bash
curl -X POST http://localhost:8080/reset-password \
-H "Content-Type: application/json" \
-d '{
  "email": "user@example.com",
  "resetToken": "rst123",
  "newPassword": "NewPassword456"
}'
```
**Response**:
```json
{
  "message": "Password reset successfully"
}
```

### User Controller

#### Get All Users
```bash
curl -X GET http://localhost:8080/users?page=1&size=10 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "meta": {
    "page": 1,
    "size": 10,
    "total": 25
  },
  "data": [
    {
      "id": 1,
      "email": "user1@example.com",
      "name": "John Doe",
      "gender": "MALE",
      "dateOfBirth": "1990-01-01",
      "roles": ["USER"]
    },
    {
      "id": 2,
      "email": "user2@example.com",
      "name": "Jane Smith",
      "gender": "FEMALE",
      "dateOfBirth": "1992-05-15",
      "roles": ["USER"]
    }
  ]
}
```

#### Get User by ID
```bash
curl -X GET http://localhost:8080/users/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "id": 1,
  "email": "user1@example.com",
  "name": "John Doe",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01",
  "roles": ["USER"]
}
```

#### Update User
```bash
curl -X PUT http://localhost:8080/users \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 1,
  "name": "John Doe Updated",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01",
  "avatar": "https://example.com/avatar.jpg"
}'
```
**Response**:
```json
{
  "id": 1,
  "email": "user1@example.com",
  "name": "John Doe Updated",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01",
  "avatar": "https://example.com/avatar.jpg",
  "roles": ["USER"]
}
```

#### Update User Roles
```bash
curl -X PUT http://localhost:8080/users/roles \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "userId": 1,
  "roleIds": [1, 2]
}'
```
**Response**:
```json
{
  "id": 1,
  "email": "user1@example.com",
  "name": "John Doe",
  "gender": "MALE",
  "dateOfBirth": "1990-01-01",
  "roles": ["USER", "ADMIN"]
}
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/users/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

### Role Controller

#### Get All Roles
```bash
curl -X GET http://localhost:8080/roles \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
[
  {
    "id": 1,
    "name": "USER",
    "description": "Regular user role"
  },
  {
    "id": 2,
    "name": "ADMIN",
    "description": "Administrator role"
  },
  {
    "id": 3,
    "name": "MODERATOR",
    "description": "Moderator role"
  }
]
```

#### Create Role
```bash
curl -X POST http://localhost:8080/roles \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "name": "MANAGER",
  "description": "Manager role"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "MANAGER",
  "description": "Manager role"
}
```

#### Update Role
```bash
curl -X PUT http://localhost:8080/roles \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 4,
  "name": "MANAGER",
  "description": "Updated manager role description"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "MANAGER",
  "description": "Updated manager role description"
}
```

#### Delete Role
```bash
curl -X DELETE http://localhost:8080/roles/4 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

### Permission Controller

#### Get All Permissions
```bash
curl -X GET http://localhost:8080/permissions?search=user \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "meta": {
    "page": 1,
    "size": 20,
    "total": 3
  },
  "data": [
    {
      "id": 1,
      "name": "VIEW_USERS",
      "apiPath": "/users",
      "method": "GET",
      "module": "USER"
    },
    {
      "id": 2,
      "name": "CREATE_USER",
      "apiPath": "/users",
      "method": "POST",
      "module": "USER"
    },
    {
      "id": 3,
      "name": "DELETE_USER",
      "apiPath": "/users/{id}",
      "method": "DELETE",
      "module": "USER"
    }
  ]
}
```

#### Create Permission
```bash
curl -X POST http://localhost:8080/permissions \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "name": "UPDATE_USER",
  "apiPath": "/users",
  "method": "PUT",
  "module": "USER"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "UPDATE_USER",
  "apiPath": "/users",
  "method": "PUT",
  "module": "USER"
}
```

#### Update Permission
```bash
curl -X PUT http://localhost:8080/permissions \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 4,
  "name": "UPDATE_USER_PROFILE",
  "apiPath": "/users",
  "method": "PUT",
  "module": "USER"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "UPDATE_USER_PROFILE",
  "apiPath": "/users",
  "method": "PUT",
  "module": "USER"
}
```

#### Delete Permission
```bash
curl -X DELETE http://localhost:8080/permissions/4 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

### Product Controller

#### Get Public Products
```bash
curl -X GET "http://localhost:8080/api/public/products?page=1&size=10&categoryId=2&searchName=cleaning"
```
**Response**:
```json
{
  "meta": {
    "page": 1,
    "size": 10,
    "total": 5
  },
  "data": [
    {
      "id": 1,
      "name": "Basic Cleaning Service",
      "description": "Standard home cleaning service",
      "price": 25.0,
      "discount": 0,
      "categoryId": 2,
      "images": ["https://example.com/cleaning1.jpg"],
      "averageRating": 4.5
    },
    {
      "id": 2,
      "name": "Deep Cleaning Service",
      "description": "Thorough deep cleaning for your home",
      "price": 40.0,
      "discount": 5,
      "categoryId": 2,
      "images": ["https://example.com/cleaning2.jpg"],
      "averageRating": 4.8
    }
  ]
}
```

#### Get Product by ID
```bash
curl -X GET http://localhost:8080/api/public/products/1
```
**Response**:
```json
{
  "id": 1,
  "name": "Basic Cleaning Service",
  "description": "Standard home cleaning service",
  "price": 25.0,
  "discount": 0,
  "categoryId": 2,
  "images": ["https://example.com/cleaning1.jpg"],
  "averageRating": 4.5,
  "reviewDTOs": [
    {
      "id": 1,
      "rating": 5,
      "comment": "Great service!",
      "createdAt": "2023-01-15T10:30:00",
      "userDTO": {
        "id": 1,
        "name": "John Doe"
      }
    },
    {
      "id": 2,
      "rating": 4,
      "comment": "Very good service",
      "createdAt": "2023-01-20T14:15:00",
      "userDTO": {
        "id": 2,
        "name": "Jane Smith"
      }
    }
  ]
}
```

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "name": "Premium Cleaning Service",
  "description": "Top-tier cleaning service with special care",
  "price": 60.0,
  "discount": 10,
  "categoryId": 2,
  "images": ["https://example.com/premium-cleaning.jpg"]
}'
```
**Response**:
```json
{
  "id": 3,
  "name": "Premium Cleaning Service",
  "description": "Top-tier cleaning service with special care",
  "price": 60.0,
  "discount": 10,
  "categoryId": 2,
  "images": ["https://example.com/premium-cleaning.jpg"],
  "averageRating": 0
}
```

#### Update Product
```bash
curl -X PUT http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 3,
  "name": "Premium Cleaning Service Plus",
  "description": "Enhanced premium cleaning service",
  "price": 70.0,
  "discount": 15,
  "categoryId": 2
}'
```
**Response**:
```json
{
  "id": 3,
  "name": "Premium Cleaning Service Plus",
  "description": "Enhanced premium cleaning service",
  "price": 70.0,
  "discount": 15,
  "categoryId": 2,
  "images": ["https://example.com/premium-cleaning.jpg"],
  "averageRating": 0
}
```

#### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/3 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

#### Upload Product Image
```bash
curl -X PUT http://localhost:8080/api/products/images \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "productId": 1,
  "imageUrl": "https://example.com/new-cleaning-image.jpg"
}'
```
**Response**:
```json
{
  "id": 1,
  "name": "Basic Cleaning Service",
  "images": [
    "https://example.com/cleaning1.jpg",
    "https://example.com/new-cleaning-image.jpg"
  ]
}
```

### Category Controller

#### Get All Categories
```bash
curl -X GET http://localhost:8080/api/public/categories?page=1&size=10
```
**Response**:
```json
{
  "meta": {
    "page": 1,
    "size": 10,
    "total": 3
  },
  "data": [
    {
      "id": 1,
      "name": "Home Care",
      "description": "Home care services"
    },
    {
      "id": 2,
      "name": "Cleaning",
      "description": "Cleaning services"
    },
    {
      "id": 3,
      "name": "Elderly Care",
      "description": "Services for the elderly"
    }
  ]
}
```

#### Get Category by ID
```bash
curl -X GET http://localhost:8080/api/public/categories/2
```
**Response**:
```json
{
  "id": 2,
  "name": "Cleaning",
  "description": "Cleaning services",
  "products": [
    {
      "id": 1,
      "name": "Basic Cleaning Service",
      "price": 25.0,
      "discount": 0
    },
    {
      "id": 2,
      "name": "Deep Cleaning Service",
      "price": 40.0,
      "discount": 5
    }
  ]
}
```

#### Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "name": "Child Care",
  "description": "Child care services"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "Child Care",
  "description": "Child care services"
}
```

#### Update Category
```bash
curl -X PUT http://localhost:8080/api/categories \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 4,
  "name": "Child & Infant Care",
  "description": "Child and infant care services"
}'
```
**Response**:
```json
{
  "id": 4,
  "name": "Child & Infant Care",
  "description": "Child and infant care services"
}
```

#### Delete Category
```bash
curl -X DELETE http://localhost:8080/api/categories/4 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

### Booking Controller

#### Create Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "userId": 1,
  "productIds": [1, 2],
  "address": "123 Main St",
  "totalHours": 2.5,
  "isPeriodic": false,
  "note": "Please arrive by 9 AM"
}'
```
**Response**:
```json
{
  "id": 1,
  "address": "123 Main St",
  "totalHours": 2.5,
  "totalPrice": 50.0,
  "bookingStatus": "PENDING",
  "userDTO": { "id": 1, "name": "John Doe" },
  "products": [{ "id": 1, "name": "Basic Cleaning Service" }]
}
```

#### Get Booking by ID
```bash
curl -X GET http://localhost:8080/api/bookings/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "id": 1,
  "address": "123 Main St",
  "totalHours": 2.5,
  "totalPrice": 50.0,
  "bookingStatus": "PENDING",
  "isPeriodic": false,
  "note": "Please arrive by 9 AM",
  "userDTO": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com"
  },
  "products": [
    {
      "id": 1,
      "name": "Basic Cleaning Service",
      "price": 25.0
    }
  ],
  "createdAt": "2023-02-15T09:30:00"
}
```

#### Update Booking
```bash
curl -X PUT http://localhost:8080/api/bookings \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "id": 1,
  "address": "123 Main St, Apartment 4B",
  "totalHours": 3.0,
  "note": "Please arrive by 10 AM"
}'
```
**Response**:
```json
{
  "id": 1,
  "address": "123 Main St, Apartment 4B",
  "totalHours": 3.0,
  "totalPrice": 60.0,
  "bookingStatus": "PENDING",
  "isPeriodic": false,
  "note": "Please arrive by 10 AM",
  "userDTO": {
    "id": 1,
    "name": "John Doe"
  },
  "products": [
    {
      "id": 1,
      "name": "Basic Cleaning Service"
    }
  ]
}
```

#### Update Booking Status
```bash
curl -X PUT http://localhost:8080/api/bookings/status \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "bookingId": 1,
  "status": "CONFIRMED"
}'
```
**Response**:
```json
{
  "id": 1,
  "address": "123 Main St, Apartment 4B",
  "totalHours": 3.0,
  "totalPrice": 60.0,
  "bookingStatus": "CONFIRMED",
  "userDTO": {
    "id": 1,
    "name": "John Doe"
  },
  "products": [
    {
      "id": 1,
      "name": "Basic Cleaning Service"
    }
  ]
}
```

#### Delete Booking
```bash
curl -X DELETE http://localhost:8080/api/bookings/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**: HTTP 204 No Content

### Review Controller

#### Create Review
```bash
curl -X POST http://localhost:8080/api/reviews \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-d '{
  "productId": 1,
  "userId": 1,
  "rating": 5,
  "comment": "Excellent service! Very professional and thorough."
}'
```
**Response**:
```json
{
  "id": 3,
  "rating": 5,
  "comment": "Excellent service! Very professional and thorough.",
  "createdAt": "2023-03-10T15:45:00",
  "productId": 1,
  "userDTO": {
    "id": 1,
    "name": "John Doe"
  }
}
```

#### Get Reviews
```bash
curl -X GET "http://localhost:8080/api/reviews?page=1&size=10&productId=1" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "meta": {
    "page": 1,
    "size": 10,
    "total": 3
  },
  "data": [
    {
      "id": 1,
      "rating": 5,
      "comment": "Great service!",
      "createdAt": "2023-01-15T10:30:00",
      "productId": 1,
      "userDTO": {
        "id": 1,
        "name": "John Doe"
      }
    },
    {
      "id": 2,
      "rating": 4,
      "comment": "Very good service",
      "createdAt": "2023-01-20T14:15:00",
      "productId": 1,
      "userDTO": {
        "id": 2,
        "name": "Jane Smith"
      }
    },
    {
      "id": 3,
      "rating": 5,
      "comment": "Excellent service! Very professional and thorough.",
      "createdAt": "2023-03-10T15:45:00",
      "productId": 1,
      "userDTO": {
        "id": 1,
        "name": "John Doe"
      }
    }
  ]
}
```

### File Controller

#### Upload File
```bash
curl -X POST http://localhost:8080/api/cloudinary/files \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
-F "file=@/path/to/image.jpg"
```
**Response**:
```json
{
  "id": "cl1a2b3c4d5e6",
  "url": "https://res.cloudinary.com/domicare/image/upload/v1645678901/cl1a2b3c4d5e6.jpg",
  "name": "image.jpg",
  "size": 102400,
  "format": "jpg",
  "createdAt": "2023-05-15T11:30:00"
}
```

#### Get File by Name
```bash
curl -X GET "http://localhost:8080/api/cloudinary/files?name=image.jpg" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "id": "cl1a2b3c4d5e6",
  "url": "https://res.cloudinary.com/domicare/image/upload/v1645678901/cl1a2b3c4d5e6.jpg",
  "name": "image.jpg",
  "size": 102400,
  "format": "jpg",
  "createdAt": "2023-05-15T11:30:00"
}
```

#### Get File by ID
```bash
curl -X GET http://localhost:8080/api/cloudinary/files/cl1a2b3c4d5e6 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "id": "cl1a2b3c4d5e6",
  "url": "https://res.cloudinary.com/domicare/image/upload/v1645678901/cl1a2b3c4d5e6.jpg",
  "name": "image.jpg",
  "size": 102400,
  "format": "jpg",
  "createdAt": "2023-05-15T11:30:00"
}
```

#### Get All Files
```bash
curl -X GET http://localhost:8080/api/cloudinary/files/all \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
[
  {
    "id": "cl1a2b3c4d5e6",
    "url": "https://res.cloudinary.com/domicare/image/upload/v1645678901/cl1a2b3c4d5e6.jpg",
    "name": "image.jpg",
    "size": 102400,
    "format": "jpg",
    "createdAt": "2023-05-15T11:30:00"
  },
  {
    "id": "cl9z8y7x6w5v",
    "url": "https://res.cloudinary.com/domicare/image/upload/v1645678902/cl9z8y7x6w5v.png",
    "name": "logo.png",
    "size": 45678,
    "format": "png",
    "createdAt": "2023-05-14T10:15:00"
  }
]
```

#### Delete File
```bash
curl -X DELETE http://localhost:8080/api/cloudinary/files/cl1a2b3c4d5e6 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```
**Response**:
```json
{
  "message": "File deleted successfully"
}
```

### Email Sending Controller

#### Send Verification Email
```bash
curl -X GET "http://localhost:8080/email/verify?email=user@example.com"
```
**Response**:
```json
{
  "message": "Verification email sent successfully to user@example.com"
}
```

#### Send Reset Password Email
```bash
curl -X GET "http://localhost:8080/email/reset-password?email=user@example.com"
```
**Response**:
```json
{
  "message": "Password reset email sent successfully to user@example.com"
}
```

### OAuth2 Controller

#### OAuth2 Callback
```bash
curl -X GET "http://localhost:8080/auth/callback?code=abc123xyz"
```
**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "oauth2-refresh-token-123",
  "user": {
    "id": 5,
    "email": "oauth.user@gmail.com",
    "name": "OAuth User",
    "avatar": "https://example.com/oauth-avatar.jpg"
  }
}
```

### View Controller

#### Verify Email
```bash
curl -X GET "http://localhost:8080/verify-email?token=email-verification-token-123"
```
**Response**: HTML page with confirmation message

#### Forgot Password
```bash
curl -X GET "http://localhost:8080/forgot-password?token=password-reset-token-456"
```
**Response**: HTML page with password reset form

## Notes
- **Pagination**: Use `page`, `size`, `sortBy`, and `sortDirection` for paginated endpoints.
- **File Uploads**: The `/api/cloudinary/files` POST endpoint expects `multipart/form-data` for file uploads (current schema may need correction).
- **Security**: Ensure proper authentication and authorization for private endpoints. Public endpoints should be used cautiously to avoid data exposure.
- **Error Handling**: The API implements centralized error handling through a GlobalExceptionHandler. See the "Error Handling" section below for details on response structures.

## Error Handling

The Domicare API implements a centralized error handling mechanism through a `GlobalExceptionHandler`. When errors occur, the API returns a standardized JSON response to help clients properly handle and display error messages.

### Error Response Structure

All error responses follow a consistent JSON format:

```json
{
  "status": 422,                     // Numeric error code specific to the API
  "error": "EMAIL_ALREADY_EXISTS",   // String error identifier
  "message": "Email already exists"  // Human-readable error message
}
```

### HTTP Status Codes

The API uses the following HTTP status codes for errors:

| Status Code | Description                                          |
|-------------|------------------------------------------------------|
| 400         | Bad Request - Invalid input, validation errors       |
| 401         | Unauthorized - Authentication required               |
| 403         | Forbidden - Insufficient permissions                 |
| 404         | Not Found - Resource doesn't exist                   |
| 417         | Expectation Failed - For file upload size issues     |
| 422         | Unprocessable Entity - Business logic errors         |
| 500         | Internal Server Error - Unexpected server-side issues |

### Common Error Types

#### Authentication Errors

| Error Code | Message                                    | HTTP Status |
|------------|--------------------------------------------|-------------|
| 119        | `BAD_CREDENTIALS` - Invalid password       | 422         |
| 116        | `INVALID_REFRESH_TOKEN` - Token expired    | 422         |
| 118        | `EMAIL_NOT_COMFIRMED` - Email not verified | 422         |
| 102        | `INVALID_AUTHENTICATED` - Invalid token    | 422         |

#### Resource Not Found Errors

| Error Code | Message                                    | HTTP Status |
|------------|--------------------------------------------|-------------|
| 112        | `USER_NOT_FOUND` - User doesn't exist      | 422         |
| 109        | `FILE_NOT_FOUND` - File doesn't exist      | 422         |
| 211        | `NOT_FOUND_PRODUCT_ID` - Product not found | 422         |
| 301        | `BOOKING_NOT_FOUND` - Booking not found    | 422         |

#### Duplicate Resource Errors

| Error Code | Message                                       | HTTP Status |
|------------|-----------------------------------------------|-------------|
| 108        | `EMAIL_ALREADY_EXISTS` - Email already in use | 422         |
| 121        | `PRODUCT_NAME_ALREADY_EXISTS` - Duplicate name| 422         |
| 202        | `CATEGORY_ALREADY_EXISTS` - Duplicate category| 422         |
| 501        | `ALREADY_REVIEWED` - User already reviewed    | 422         |

#### Validation Errors

For request validation failures (via `@Valid` annotation), the API returns:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Email must be a valid email address"
}
```

When multiple validation errors occur, all errors are returned in the message:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "[Email must be a valid email address, Password must be at least 8 characters]"
}
```

#### File Upload Errors

For file upload issues:

```json
{
  "message": "File quá lớn!"
}
```

### Error Handling Best Practices

1. **Always check HTTP status codes** first to determine the error category
2. **Parse the error response** to extract the specific `status`, `error`, and `message`
3. **Display user-friendly messages** based on the error code
4. **Log unexpected errors** (500) for debugging purposes
5. **Implement appropriate retry logic** for network errors
6. **Handle expired authentication tokens** by refreshing or redirecting to login

## Endpoint-Specific Error Handling

### Authentication and User Management

#### Login (`POST /login`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 117 | `EMAIL_NOT_FOUND` | 422 | Email address doesn't exist in the system |
| 118 | `EMAIL_NOT_COMFIRMED` | 422 | Email hasn't been verified yet |
| 119 | `BAD_CREDENTIALS` | 422 | Password is incorrect |
| 400 | `Validation Error` | 400 | Email format is invalid or required fields are missing |

#### Registration (`POST /register`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 108 | `EMAIL_ALREADY_EXISTS` | 422 | Email is already registered |
| 400 | `Validation Error` | 400 | Password doesn't meet requirements (must have at least one uppercase, lowercase, and number) or email format is invalid |

#### Token Refresh (`POST /refresh-token`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 116 | `INVALID_REFRESH_TOKEN` | 422 | Refresh token is expired or invalid |
| 400 | `Validation Error` | 400 | Refresh token is missing |

#### Password Reset (`POST /reset-password`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 117 | `EMAIL_NOT_FOUND` | 422 | Email not found in the system |
| 400 | `Validation Error` | 400 | Required fields are missing |

### User Controller Endpoints

#### Get Users (`GET /users`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to view user list |

#### Get User by ID (`GET /users/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 112 | `USER_NOT_FOUND` | 422 | User with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to view other users |

#### Update User (`PUT /users`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 112 | `USER_NOT_FOUND` | 422 | User with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update user information |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |

#### Update User Roles (`PUT /users/roles`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 112 | `USER_NOT_FOUND` | 422 | User with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update roles |
| 114 | `ADMIN_UNAUTHORIZED` | 403 | Only super admins can update admin roles |
| 400 | `Validation Error` | 400 | Missing user ID or role IDs |

#### Delete User (`DELETE /users/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 112 | `USER_NOT_FOUND` | 422 | User with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete users |
| 114 | `UNAUTHORIZED_ADMIN_DELETE_OTHER_ADMINS` | 403 | Regular admins cannot delete other admins |

### Role Controller Endpoints

#### Get Roles (`GET /roles`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to view roles |

#### Create Role (`POST /roles`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to create roles |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 103 | `ROLE_ALREADY_EXISTS` | 422 | Role with the same name already exists |

#### Update Role (`PUT /roles`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 113 | `NOT_FOUND_ROLE` | 422 | Role with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update roles |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |

#### Delete Role (`DELETE /roles/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 113 | `NOT_FOUND_ROLE` | 422 | Role with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete roles |

### Product Controller Endpoints

#### Get Products (`GET /api/public/products`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 400 | `Validation Error` | 400 | Invalid pagination parameters |

#### Get Product by ID (`GET /api/public/products/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | Product with the specified ID doesn't exist |

#### Create Product (`POST /api/products`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to create products |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 121 | `PRODUCT_NAME_ALREADY_EXISTS` | 422 | Product with the same name already exists |
| 201 | `CATEGORY_NOT_FOUND` | 422 | Specified category doesn't exist |

#### Update Product (`PUT /api/products`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | Product with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update products |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 121 | `PRODUCT_NAME_ALREADY_EXISTS` | 422 | Another product with the same name already exists |
| 201 | `CATEGORY_NOT_FOUND` | 422 | Specified category doesn't exist |

#### Delete Product (`DELETE /api/products/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | Product with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete products |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |

#### Upload Product Image (`PUT /api/products/images`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | Product with the specified ID doesn't exist |
| 109 | `FILE_NOT_FOUND` | 422 | Referenced file doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update product images |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |

### Category Controller Endpoints

#### Get Categories (`GET /api/public/categories`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 400 | `Validation Error` | 400 | Invalid pagination parameters |

#### Get Category by ID (`GET /api/public/categories/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 201 | `CATEGORY_NOT_FOUND` | 422 | Category with the specified ID doesn't exist |

#### Create Category (`POST /api/categories`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to create categories |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 202 | `CATEGORY_ALREADY_EXISTS` | 422 | Category with the same name already exists |

#### Update Category (`PUT /api/categories`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 201 | `CATEGORY_NOT_FOUND` | 422 | Category with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update categories |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 202 | `CATEGORY_ALREADY_EXISTS` | 422 | Category with the same name already exists |

#### Delete Category (`DELETE /api/categories/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 201 | `CATEGORY_NOT_FOUND` | 422 | Category with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete categories |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |

### Booking Controller Endpoints

#### Create Booking (`POST /api/bookings`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 112 | `USER_NOT_FOUND` | 422 | Specified user doesn't exist |
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | One or more products don't exist |

#### Get Booking by ID (`GET /api/bookings/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 301 | `BOOKING_NOT_FOUND` | 422 | Booking with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to view the booking |

#### Update Booking (`PUT /api/bookings`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 301 | `BOOKING_NOT_FOUND` | 422 | Booking with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update the booking |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |

#### Update Booking Status (`PUT /api/bookings/status`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 301 | `BOOKING_NOT_FOUND` | 422 | Booking with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to update the booking status |
| 302 | `BOOKING_STATUS_INVALID` | 422 | Invalid booking status provided |
| 400 | `Validation Error` | 400 | Required fields are missing |

#### Delete Booking (`DELETE /api/bookings/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 301 | `BOOKING_NOT_FOUND` | 422 | Booking with the specified ID doesn't exist |
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete the booking |

### Review Controller Endpoints

#### Get Reviews (`GET /api/reviews`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Invalid pagination parameters |

#### Create Review (`POST /api/reviews`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 400 | `Validation Error` | 400 | Required fields are missing or invalid |
| 112 | `USER_NOT_FOUND` | 422 | Specified user doesn't exist |
| 211 | `NOT_FOUND_PRODUCT_ID` | 422 | Specified product doesn't exist |
| 501 | `ALREADY_REVIEWED` | 422 | User has already reviewed this product |

### File Controller Endpoints

#### Upload File (`POST /api/cloudinary/files`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 417 | `File quá lớn!` | 417 | File exceeds the maximum allowed size |
| 400 | `Validation Error` | 400 | No file provided or invalid file format |

#### Get File by Name (`GET /api/cloudinary/files?name=`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 109 | `FILE_NOT_FOUND` | 422 | File with the specified name doesn't exist |
| 400 | `Validation Error` | 400 | Name parameter is missing |

#### Get File by ID (`GET /api/cloudinary/files/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 109 | `FILE_NOT_FOUND` | 422 | File with the specified ID doesn't exist |

#### Delete File (`DELETE /api/cloudinary/files/{id}`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 102 | `INVALID_AUTHENTICATED` | 401 | Invalid or expired authentication token |
| 109 | `FILE_NOT_FOUND` | 422 | File with the specified ID doesn't exist |
| 113 | `UNAUTHORIZED` | 403 | User doesn't have permission to delete files |

### Email Sending Controller Endpoints

#### Send Verification Email (`GET /email/verify`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 117 | `EMAIL_NOT_FOUND` | 422 | Email not found in the system |
| 400 | `Validation Error` | 400 | Email parameter is missing |
| 500 | `INTERNAL_SERVER_ERROR` | 500 | Email sending failed (server configuration issue) |

#### Send Reset Password Email (`GET /email/reset-password`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 117 | `EMAIL_NOT_FOUND` | 422 | Email not found in the system |
| 400 | `Validation Error` | 400 | Email parameter is missing |
| 500 | `INTERNAL_SERVER_ERROR` | 500 | Email sending failed (server configuration issue) |

### OAuth2 Controller Endpoints

#### OAuth2 Callback (`GET /auth/callback`)

| Error Code | Error Message | HTTP Status | Description |
|------------|---------------|-------------|-------------|
| 400 | `Validation Error` | 400 | Code parameter is missing or invalid |
| 500 | `INTERNAL_SERVER_ERROR` | 500 | OAuth authentication failed |

### Error Handling Best Practices for Client Applications

1. **Authenticate properly**: Ensure your authentication token is sent correctly with all requests
2. **Validate input data**: Perform client-side validation before sending data to the server
3. **Handle common errors**:
   - 401/403 errors: Prompt user to log in again or inform about insufficient permissions
   - 422 errors: Display specific error messages from the API response
   - 400 errors: Show validation errors to help users correct their input
   - 500 errors: Display a generic error message and consider logging the issue
4. **Refresh tokens**: Automatically refresh tokens when they expire
5. **Pagination handling**: Validate page and size parameters when making paginated requests
6. **File upload size**: Check file size before uploading to avoid `417` errors
7. **Email verification**: Guide users to verify their email after registration


## Test with Postman
    - Import **domicare-tests.postman_collection.json** to Postman and test
    [text](domicare-tests.postman_collection.json)

## Contact
For further details or support, refer to the Domicare application documentation or contact the development team.