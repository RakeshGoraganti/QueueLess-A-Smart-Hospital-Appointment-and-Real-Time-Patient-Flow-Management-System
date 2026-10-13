# Smart Hospital Appointment and Queue Management System

Full-stack project:
- Frontend: React + Vite + React Router + Axios + Lucide React
- Backend: Java 21+, Spring Boot 3, Maven, Spring Data JPA, Spring Security, JWT
- Database: MySQL 8+

## Features
- Patient registration/login and role-aware navigation
- Admin hospital, department, and doctor management endpoints
- Appointment booking with sequential daily doctor tokens
- Patient appointment list and queue status
- Doctor queue view and next-patient workflow
- Consultation notes and appointment completion
- Dashboard statistics endpoint
- Password hashing with BCrypt, JWT authentication, role checks
- MySQL persistence and seed data

## Prerequisites
Install Java 21+, Maven 3.9+, Node.js 20+, and MySQL 8+.

## 1. Configure MySQL
Create a database:
```sql
CREATE DATABASE smart_hospital;
```
The backend reads these environment variables (defaults are for local development):
- `DB_URL` (default `jdbc:mysql://localhost:3306/smart_hospital?createDatabaseIfNotExist=true`)
- `DB_USERNAME` (default `root`)
- `DB_PASSWORD` (default `root`)
- `JWT_SECRET` (set a long random secret in real deployments)

Update the defaults in `backend/src/main/resources/application.properties` if needed.

## 2. Start backend
From `backend/`:
```bash
mvn spring-boot:run
```
Backend URL: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui/index.html

On first start, sample hospitals/departments/doctors and demo users are inserted if absent:
- Admin: `admin@hospital.com` / `Admin@123`
- Doctor: `doctor@hospital.com` / `Doctor@123`
- Patient: `patient@hospital.com` / `Patient@123`

Change demo passwords before any real deployment. Demo accounts are for local development only.

## 3. Start frontend
From `frontend/`:
```bash
npm install
npm run dev
```
Open the URL printed by Vite (normally http://localhost:5173).

## API overview
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/hospitals`
- `GET /api/hospitals/{id}/departments`
- `GET /api/departments/{id}/doctors`
- `GET /api/doctors`
- `POST /api/appointments`
- `GET /api/appointments/mine`
- `GET /api/queue/doctor/{doctorId}?date=YYYY-MM-DD`
- `POST /api/queue/doctor/{doctorId}/call-next`
- `PATCH /api/appointments/{id}/complete`
- `GET /api/dashboard/stats` (admin)

## Notes
This is a functional academic/demo foundation, not a certified clinical system. Before real hospital use, add professional security review, privacy/compliance review, backups, monitoring, deployment secrets management, and formal testing. Estimated waiting times are indicative only.
