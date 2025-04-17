# DomiCare API Test Documentation

This document provides a comprehensive guide for testing all endpoints in the DomiCare API using Postman. It includes test setups, test cases, and expected results.

## Table of Contents
1. [Environment Setup](#environment-setup)
2. [Authentication Tests](#authentication-tests)
3. [User Management Tests](#user-management-tests)
4. [Role and Permission Tests](#role-and-permission-tests)
5. [Product Management Tests](#product-management-tests)
6. [Category Management Tests](#category-management-tests)
7. [File Management Tests](#file-management-tests)
8. [Review Tests](#review-tests)
9. [Email Tests](#email-tests)

## Environment Setup

Set up the following environment variables in Postman:

```
BASE_URL: http://localhost:8080
ACCESS_TOKEN: (will be populated after login)
REFRESH_TOKEN: (will be populated after login)
USER_ID: (will be populated after login or creating a user)
PRODUCT_ID: (will be populated after creating a product)
CATEGORY_ID: (will be populated after creating a category)
IMAGE_ID: (will be populated after uploading an image)
```

## Authentication Tests

### Register User

**Endpoint:** `POST {{BASE_URL}}/register`

**Payload:**
```json
{
  "email": "testuser@example.com",
  "password": "Password123"
}
```

**Test Script:**
```javascript
pm.test("User registration successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData).to.have.property("email");
    pm.expect(jsonData.email).to.eql("testuser@example.com");
    pm.expect(jsonData).to.have.property("emailConfirmationToken");
});

pm.test("Email confirmation token is present", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.emailConfirmationToken).to.be.a('string');
});
```

### User Login

**Endpoint:** `POST {{BASE_URL}}/login`

**Payload:**
```json
{
  "email": "testuser@example.com",
  "password": "Password123"
}
```

**Test Script:**
```javascript
pm.test("User login successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("accessToken");
    pm.expect(jsonData).to.have.property("refreshToken");
    pm.expect(jsonData).to.have.property("user");
    
    // Save tokens to environment
    pm.environment.set("ACCESS_TOKEN", jsonData.accessToken);
    pm.environment.set("REFRESH_TOKEN", jsonData.refreshToken);
    pm.environment.set("USER_ID", jsonData.user.id);
});

pm.test("Response has correct user details", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.user.email).to.eql("testuser@example.com");
    pm.expect(jsonData.user).to.have.property("roles");
});
```

### Login with Invalid Credentials

**Endpoint:** `POST {{BASE_URL}}/login`

**Payload:**
```json
{
  "email": "testuser@example.com",
  "password": "wrongpassword"
}
```

**Test Script:**
```javascript
pm.test("Login with invalid credentials should fail", function() {
    pm.response.to.have.status(401);
});
```

### Refresh Token

**Endpoint:** `POST {{BASE_URL}}/refresh-token`

**Payload:**
```json
{
  "refreshToken": "{{REFRESH_TOKEN}}"
}
```

**Test Script:**
```javascript
pm.test("Token refresh successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("accessToken");
    pm.expect(jsonData).to.have.property("email");
    
    // Update access token
    pm.environment.set("ACCESS_TOKEN", jsonData.accessToken);
});
```

### Password Reset Email

**Endpoint:** `GET {{BASE_URL}}/email/reset-password?email=testuser@example.com`

**Test Script:**
```javascript
pm.test("Reset password email sent successfully", function() {
    pm.response.to.have.status(200);
});
```

### Submit Password Reset

**Endpoint:** `POST {{BASE_URL}}/reset-password`

**Payload:**
```json
{
  "id": "{{USER_ID}}",
  "email": "testuser@example.com",
  "password": "NewPassword456"
}
```

**Test Script:**
```javascript
pm.test("Password reset successful", function() {
    pm.response.to.have.status(200);
});
```

## User Management Tests

### Get All Users

**Endpoint:** `GET {{BASE_URL}}/users`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Query Parameters:**
```
page=1
size=10
```

**Test Script:**
```javascript
pm.test("Get users successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("meta");
    pm.expect(jsonData).to.have.property("data");
    pm.expect(jsonData.meta).to.have.property("page");
    pm.expect(jsonData.meta).to.have.property("size");
    pm.expect(jsonData.meta).to.have.property("total");
});
```

### Get User by ID

**Endpoint:** `GET {{BASE_URL}}/users/{{USER_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get user by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("USER_ID")));
    pm.expect(jsonData).to.have.property("email");
});
```

### Update User

**Endpoint:** `PUT {{BASE_URL}}/users`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "id": "{{USER_ID}}",
  "name": "Updated Test User",
  "email": "testuser@example.com",
  "address": "123 Test Street",
  "phone": "1234567890"
}
```

**Test Script:**
```javascript
pm.test("Update user successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("USER_ID")));
    pm.expect(jsonData.name).to.eql("Updated Test User");
    pm.expect(jsonData.address).to.eql("123 Test Street");
    pm.expect(jsonData.phone).to.eql("1234567890");
});
```

### Update User Avatar

**Endpoint:** `PUT {{BASE_URL}}/users/avatar`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "userId": "{{USER_ID}}",
  "imageId": "{{IMAGE_ID}}"
}
```

**Test Script:**
```javascript
pm.test("Update avatar successful", function() {
    pm.response.to.have.status(200);
});
```

### Update User Roles

**Endpoint:** `PUT {{BASE_URL}}/users/roles`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "userId": "{{USER_ID}}",
  "roleIds": [1]
}
```

**Test Script:**
```javascript
pm.test("Update user roles successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("roles");
    pm.expect(jsonData.roles).to.be.an("array").that.is.not.empty;
});
```

### Delete User

**Endpoint:** `DELETE {{BASE_URL}}/users/{{USER_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete user successful", function() {
    pm.response.to.have.status(200);
});
```

## Role and Permission Tests

### Get All Roles

**Endpoint:** `GET {{BASE_URL}}/roles`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get roles successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array').that.is.not.empty;
});
```

### Create Role

**Endpoint:** `POST {{BASE_URL}}/roles`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "name": "TEST_ROLE",
  "description": "Test role created by automated tests",
  "active": true
}
```

**Test Script:**
```javascript
pm.test("Create role successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.name).to.eql("TEST_ROLE");
    
    // Save role ID for later tests
    pm.environment.set("ROLE_ID", jsonData.id);
});
```

### Get Role by ID

**Endpoint:** `GET {{BASE_URL}}/roles/{{ROLE_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get role by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("ROLE_ID")));
    pm.expect(jsonData.name).to.eql("TEST_ROLE");
});
```

### Update Role

**Endpoint:** `PUT {{BASE_URL}}/roles`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "id": "{{ROLE_ID}}",
  "name": "UPDATED_TEST_ROLE",
  "description": "Updated test role description",
  "active": true
}
```

**Test Script:**
```javascript
pm.test("Update role successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("ROLE_ID")));
    pm.expect(jsonData.name).to.eql("UPDATED_TEST_ROLE");
});
```

### Get All Permissions

**Endpoint:** `GET {{BASE_URL}}/permissions`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Query Parameters:**
```
page=1
size=10
```

**Test Script:**
```javascript
pm.test("Get permissions successful", function() {
    pm.response.to.have.status(200);
});
```

### Create Permission

**Endpoint:** `POST {{BASE_URL}}/permissions`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "name": "TEST_PERMISSION",
  "apiPath": "/api/test",
  "method": "GET",
  "module": "TEST_MODULE"
}
```

**Test Script:**
```javascript
pm.test("Create permission successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.name).to.eql("TEST_PERMISSION");
    
    // Save permission ID for later tests
    pm.environment.set("PERMISSION_ID", jsonData.id);
});
```

### Get Permission by ID

**Endpoint:** `GET {{BASE_URL}}/permissions/{{PERMISSION_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get permission by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("PERMISSION_ID")));
    pm.expect(jsonData.name).to.eql("TEST_PERMISSION");
});
```

### Update Permission

**Endpoint:** `PUT {{BASE_URL}}/permissions`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "id": "{{PERMISSION_ID}}",
  "name": "UPDATED_TEST_PERMISSION",
  "apiPath": "/api/test/updated",
  "method": "POST",
  "module": "UPDATED_TEST_MODULE"
}
```

**Test Script:**
```javascript
pm.test("Update permission successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("PERMISSION_ID")));
    pm.expect(jsonData.name).to.eql("UPDATED_TEST_PERMISSION");
});
```

### Delete Role

**Endpoint:** `DELETE {{BASE_URL}}/roles/{{ROLE_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete role successful", function() {
    pm.response.to.have.status(200);
});
```

### Delete Permission

**Endpoint:** `DELETE {{BASE_URL}}/permissions/{{PERMISSION_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete permission successful", function() {
    pm.response.to.have.status(200);
});
```

## Product Management Tests

### Create Product

**Endpoint:** `POST {{BASE_URL}}/api/products`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "categoryId": "{{CATEGORY_ID}}",
  "product": {
    "name": "Test Product",
    "description": "This is a test product",
    "price": 99.99,
    "discount": 10.0
  },
  "mainImageId": "{{IMAGE_ID}}",
  "landingImageIds": []
}
```

**Test Script:**
```javascript
pm.test("Create product successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.name).to.eql("Test Product");
    
    // Save product ID for later tests
    pm.environment.set("PRODUCT_ID", jsonData.id);
});

pm.test("Product price and discount calculations are correct", function() {
    var jsonData = pm.response.json();
    const price = jsonData.price;
    const discount = jsonData.discount;
    const expectedPriceAfterDiscount = price * (1 - discount/100);
    
    pm.expect(jsonData.priceAfterDiscount).to.be.closeTo(expectedPriceAfterDiscount, 0.01);
});
```

### Get All Products

**Endpoint:** `GET {{BASE_URL}}/api/products`

**Query Parameters:**
```
page=1
size=10
```

**Test Script:**
```javascript
pm.test("Get products successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("meta");
    pm.expect(jsonData).to.have.property("data");
});

pm.test("Products pagination works correctly", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.meta.page).to.eql(1);
    pm.expect(jsonData.meta.size).to.eql(10);
});
```

### Get Product by ID

**Endpoint:** `GET {{BASE_URL}}/api/products/{{PRODUCT_ID}}`

**Test Script:**
```javascript
pm.test("Get product by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("PRODUCT_ID")));
    pm.expect(jsonData.name).to.eql("Test Product");
});
```

### Update Product

**Endpoint:** `PUT {{BASE_URL}}/api/products`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "oldProductId": "{{PRODUCT_ID}}",
  "oldCategoryId": "{{CATEGORY_ID}}",
  "updateProduct": {
    "id": "{{PRODUCT_ID}}",
    "name": "Updated Test Product",
    "description": "This is an updated test product",
    "price": 89.99,
    "discount": 15.0,
    "categoryID": "{{CATEGORY_ID}}"
  }
}
```

**Test Script:**
```javascript
pm.test("Update product successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("PRODUCT_ID")));
    pm.expect(jsonData.name).to.eql("Updated Test Product");
});
```

### Upload Product Image

**Endpoint:** `PUT {{BASE_URL}}/api/products/images`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "productId": "{{PRODUCT_ID}}",
  "imageId": "{{IMAGE_ID}}",
  "isMainImage": true
}
```

**Test Script:**
```javascript
pm.test("Upload product image successful", function() {
    pm.response.to.have.status(200);
});
```

### Delete Product

**Endpoint:** `DELETE {{BASE_URL}}/api/products/{{PRODUCT_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete product successful", function() {
    pm.response.to.have.status(200);
});
```

## Category Management Tests

### Create Category

**Endpoint:** `POST {{BASE_URL}}/api/categories`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "category": {
    "name": "Test Category",
    "description": "This is a test category"
  },
  "imageId": "{{IMAGE_ID}}"
}
```

**Test Script:**
```javascript
pm.test("Create category successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.name).to.eql("Test Category");
    
    // Save category ID for later tests
    pm.environment.set("CATEGORY_ID", jsonData.id);
});
```

### Get All Categories

**Endpoint:** `GET {{BASE_URL}}/api/categories`

**Query Parameters:**
```
page=1
size=10
sortBy=id
sortDirection=asc
```

**Test Script:**
```javascript
pm.test("Get categories successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("meta");
    pm.expect(jsonData).to.have.property("data");
});
```

### Get Category by ID

**Endpoint:** `GET {{BASE_URL}}/api/categories/{{CATEGORY_ID}}`

**Test Script:**
```javascript
pm.test("Get category by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("CATEGORY_ID")));
    pm.expect(jsonData.name).to.eql("Test Category");
});
```

### Update Category

**Endpoint:** `PUT {{BASE_URL}}/api/categories`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "categoryId": "{{CATEGORY_ID}}",
  "name": "Updated Test Category",
  "description": "This is an updated test category"
}
```

**Test Script:**
```javascript
pm.test("Update category successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("CATEGORY_ID")));
    pm.expect(jsonData.name).to.eql("Updated Test Category");
});
```

### Delete Category

**Endpoint:** `DELETE {{BASE_URL}}/api/categories/{{CATEGORY_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete category successful", function() {
    pm.response.to.have.status(200);
});
```

## File Management Tests

### Upload File

**Endpoint:** `POST {{BASE_URL}}/api/cloudinary/files`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
Content-Type: multipart/form-data
```

**Form Data:**
```
file: [select a test image file]
```

**Test Script:**
```javascript
pm.test("File upload successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    
    // Save file ID for later tests
    pm.environment.set("IMAGE_ID", jsonData.id);
});
```

### Get All Files

**Endpoint:** `GET {{BASE_URL}}/api/cloudinary/files/all`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get all files successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});
```

### Get File by ID

**Endpoint:** `GET {{BASE_URL}}/api/cloudinary/files/{{IMAGE_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get file by ID successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.id).to.eql(parseInt(pm.environment.get("IMAGE_ID")));
});
```

### Get File by Name

**Endpoint:** `GET {{BASE_URL}}/api/cloudinary/files?name=test_image.jpg`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get file by name successful", function() {
    pm.response.to.have.status(200);
});
```

### Delete File

**Endpoint:** `DELETE {{BASE_URL}}/api/cloudinary/files/{{IMAGE_ID}}`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Delete file successful", function() {
    pm.response.to.have.status(200);
});
```

## Review Tests

### Create Review

**Endpoint:** `POST {{BASE_URL}}/api/reviews`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Payload:**
```json
{
  "productId": "{{PRODUCT_ID}}",
  "rating": 4,
  "comment": "This is a test review comment"
}
```

**Test Script:**
```javascript
pm.test("Create review successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property("id");
    pm.expect(jsonData.rating).to.eql(4);
    pm.expect(jsonData.productId).to.eql(parseInt(pm.environment.get("PRODUCT_ID")));
    
    // Save review ID for later tests
    pm.environment.set("REVIEW_ID", jsonData.id);
});
```

### Get All Reviews

**Endpoint:** `GET {{BASE_URL}}/api/reviews`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Get all reviews successful", function() {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});
```

## Email Tests

### Send Email Verification

**Endpoint:** `GET {{BASE_URL}}/email/verify?email=testuser@example.com`

**Test Script:**
```javascript
pm.test("Send email verification successful", function() {
    pm.response.to.have.status(200);
});
```

### Negative Test Cases

#### Invalid Login (Empty Password)

**Endpoint:** `POST {{BASE_URL}}/login`

**Payload:**
```json
{
  "email": "testuser@example.com",
  "password": ""
}
```

**Test Script:**
```javascript
pm.test("Login with empty password should fail", function() {
    pm.response.to.have.status(400);
});
```

#### Invalid Registration (Invalid Email)

**Endpoint:** `POST {{BASE_URL}}/register`

**Payload:**
```json
{
  "email": "invalid-email",
  "password": "Password123"
}
```

**Test Script:**
```javascript
pm.test("Registration with invalid email should fail", function() {
    pm.response.to.have.status(400);
});
```

#### Create Product Without Authentication

**Endpoint:** `POST {{BASE_URL}}/api/products`

**Payload:**
```json
{
  "categoryId": 1,
  "product": {
    "name": "Unauthorized Test Product",
    "description": "This product should not be created",
    "price": 99.99
  },
  "mainImageId": 1
}
```

**Test Script:**
```javascript
pm.test("Unauthenticated product creation should fail", function() {
    pm.response.to.have.status(401);
});
```

#### Access Resource With Insufficient Permissions

**Endpoint:** `DELETE {{BASE_URL}}/users/1`

**Headers:**
```
Authorization: Bearer {{ACCESS_TOKEN}}
```

**Test Script:**
```javascript
pm.test("Access with insufficient permissions should fail", function() {
    pm.response.to.have.status(403);
});
```

## Test Sequence

For proper testing execution, follow this sequence:

1. Environment setup
2. User registration
3. Email verification
4. User login
5. File upload
6. Category creation
7. Product creation
8. Review creation
9. Get operations for entities
10. Update operations for entities
11. Token refresh
12. Password reset
13. Deletion operations
14. Negative test cases

## Creating A Postman Collection

To use these tests, create a new Postman collection and add requests for each endpoint described above. Add the test scripts to each request, and organize them into folders based on functionality.

For automated testing, you can use Newman, Postman's command-line collection runner, to execute all tests in sequence:

```bash
npm install -g newman
newman run DomiCare_API_Tests.postman_collection.json -e environment.json
```

Remember to create an appropriate environment file with the necessary variables.