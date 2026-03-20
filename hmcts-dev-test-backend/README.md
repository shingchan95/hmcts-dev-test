# HMCTS Developer Test Backend

This backend is based on the HMCTS starter project and has been extended with a PostgreSQL-backed task API.

## Existing starter endpoints

- `GET /`
- `GET /get-example-case`

## Added task endpoints

### Create task
- **Method:** `POST`
- **Path:** `/tasks`

**Request body**
```json
{
  "title": "Finish HMCTS challenge",
  "description": "Create Java backend task API",
  "status": "TODO",
  "dueDateTime": "2026-03-05T18:00:00"
}
```
### Get all tasks
- **Method:** `GET`
- **Path:** `/tasks`

### Get task by ID
- **Method:** `GET`
- **Path:** `/tasks/{id}`

### Update task status
- **Method:** `PATCH`
- **Path:** `/tasks/{id}/status`

**Request body**
```json
{
  "status": "DONE"
}
```
### Delete task
- **Method:** `DELETE`
- **Path:** `/tasks/{id}`

## Tech stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Gradle

## Environment variables

The application uses the following environment variables:
- SERVER_PORT
- DB_HOST
- DB_PORT
- DB_NAME
- DB_OPTIONS
- DB_USER_NAME
- DB_PASSWORD

## Build the project
```bash
./gradlew clean build
```

## Run the application
```bash
./gradlew bootRun
```
The application runs on: http://localhost:4000

