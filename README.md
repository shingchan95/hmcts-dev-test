# HMCTS Developer Challenge – Tasks Tracker

This repository contains my solution to the HMCTS developer challenge.

## Projects

- **Backend:** `hmcts-dev-test-backend/`  
  Spring Boot (Java) REST API with PostgreSQL persistence.

- **Frontend:** `hmcts-dev-test-frontend/`  
  Node/Express server-rendered UI (Nunjucks + GOV.UK Frontend).

> Each project contains its own README with full setup details.

## Quick start (local)

### 1) Start PostgreSQL
Ensure PostgreSQL is running locally and a database exists (example: `hmcts_tasks`).

### 2) Run the backend (port 4000)
```bash
cd hmcts-dev-test-backend
./gradlew clean build
./gradlew bootRun
```

Backend will be available at:
- http://localhost:4000

### 3) Run the frontend (port 3100)
```bash
cd hmcts-dev-test-frontend
yarn install
yarn webpack
yarn start:dev
```

Frontend will be available at:
- https://localhost:3100

## Environment variables

The backend reads configuration from environment variables. An example file is provided:
- hmcts-dev-test-backend/.env.example