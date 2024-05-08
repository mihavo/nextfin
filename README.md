# RESTful API for Money Transfer

This project implements a simple RESTful API using Spring Boot (Java) for handling financial transactions. The main functionality is to transfer money between two bank accounts, adhering to the specified acceptance criteria.

## Table of Contents

- [Features](#features)
- [Endpoints](#endpoints)
- [Data Models](#data-models)
- [General Notes](#general-notes)

## Features

- Happy path for money transfer between two accounts.
- Handling insufficient balance to process money transfer.
- Preventing transfer between the same account.
- Validating the existence of accounts during the transaction.
- Simple and extensible data models for accounts and transactions.

## Endpoints

### 1. Money Transfer

- **Endpoint**: `POST /api/transfer`
- **Request Body**:
  ```json
  {
    "sourceAccountId": "string",
    "targetAccountId": "string",
    "amount": 10.5,
    "currency": "GBP"
  }
  ```
- **Response**:
    - Success:
      ```json
      {
        "feelFree": "to decide the response type"
      }
      ```
    - Failure:
      ```json
      {
        "feelFree": "to decide the response type"
      }
      ```

## Data Models

### 1. Account

```json
{
  "id": "string",
  "balance": 100.0,
  "currency": "GBP",
  "createdAt": "2024-01-15T12:00:00Z",
  "addOther": "optionalFieldsIfNecessary"
}
```

### 2. Transaction

```json
{
  "id": "string",
  "sourceAccountId": "string",
  "targetAccountId": "string",
  "amount": 10.5,
  "currency": "GBP",
  "addOther": "optionalFieldsIfNecessary"
}
```

## General Notes

- Mock external services and databases if needed.
- Choose between Maven or Gradle for dependency management.
- The API is assumed to be public; no security measures are required for this assignment.
