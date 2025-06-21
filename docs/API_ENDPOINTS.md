# API Endpoints Documentation

## Overview

This document provides comprehensive information about all API endpoints tested by the framework.

## Base URL

- **Development**: `https://restful-booker.herokuapp.com`
- **QA**: `https://qa-restful-booker.herokuapp.com`
- **Production**: `https://prod-restful-booker.herokuapp.com`

## Authentication

### POST /auth

**Description**: Authenticate and receive an access token

**Request Body**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "abc123def456"
}
```

**Status Codes**:
- `200`: Success
- `401`: Invalid credentials

## Booking Management

### POST /booking

**Description**: Create a new booking

**Request Body**:
```json
{
  "firstname": "John",
  "lastname": "Doe",
  "totalprice": 150,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2024-01-01",
    "checkout": "2024-01-05"
  },
  "additionalneeds": "Breakfast"
}
```

**Response**:
```json
{
  "bookingid": 123,
  "booking": {
    "firstname": "John",
    "lastname": "Doe",
    "totalprice": 150,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "2024-01-01",
      "checkout": "2024-01-05"
    },
    "additionalneeds": "Breakfast"
  }
}
```

**Status Codes**:
- `200`: Success
- `400`: Bad Request
- `500`: Internal Server Error

### GET /booking/{id}

**Description**: Retrieve a specific booking

**Path Parameters**:
- `id` (integer): Booking ID

**Response**:
```json
{
  "firstname": "John",
  "lastname": "Doe",
  "totalprice": 150,
  "depositpaid": true,
  "bookingdates": {
    "checkin": "2024-01-01",
    "checkout": "2024-01-05"
  },
  "additionalneeds": "Breakfast"
}
```

**Status Codes**:
- `200`: Success
- `404`: Booking not found

### PUT /booking/{id}

**Description**: Update a complete booking

**Headers**:
- `Cookie: token={auth_token}`

**Path Parameters**:
- `id` (integer): Booking ID

**Request Body**: Same as POST /booking

**Response**: Same as GET /booking/{id}

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `404`: Booking not found

### PATCH /booking/{id}

**Description**: Partially update a booking

**Headers**:
- `Cookie: token={auth_token}`

**Path Parameters**:
- `id` (integer): Booking ID

**Request Body** (partial):
```json
{
  "firstname": "Jane",
  "lastname": "Smith"
}
```

**Response**: Complete booking object with updates

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `404`: Booking not found

### DELETE /booking/{id}

**Description**: Delete a booking

**Headers**:
- `Cookie: token={auth_token}`

**Path Parameters**:
- `id` (integer): Booking ID

**Response**: Empty body

**Status Codes**:
- `201`: Success (Note: API returns 201 instead of 204)
- `401`: Unauthorized
- `404`: Booking not found

## Error Responses

### Standard Error Format

```json
{
  "error": "Error message",
  "details": "Additional error details"
}
```

### Common Error Codes

- `400`: Bad Request - Invalid request format or data
- `401`: Unauthorized - Missing or invalid authentication
- `404`: Not Found - Resource does not exist
- `500`: Internal Server Error - Server-side error

## Rate Limiting

- No explicit rate limiting documented
- Recommended: Max 100 requests per minute per IP

## Testing Considerations

### Positive Test Cases
- Valid data creation and retrieval
- Successful authentication
- Complete CRUD operations
- Schema validation

### Negative Test Cases
- Invalid authentication credentials
- Non-existent resource access
- Invalid data formats
- Missing required fields
- Unauthorized operations

### Edge Cases
- Empty string values
- Null values
- Very long strings
- Special characters
- Date format variations