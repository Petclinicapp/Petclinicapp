# Pet Clinic Appoitment App

### Overview

A veterinary clinic management system built with Spring Boot and React that streamlines pet healthcare management. The application serves both veterinary professionals and pet owners, providing a platform for managing pet records, scheduling appointments, and tracking treatment histories. Pet owners can register pets and manage visits, while veterinarians can oversee appointments and maintain medical records. Built with modern technologies and featuring secure authentication, the system ensures efficient management of pet healthcare services.

## Features

- User Authentication & Authorization

  - JWT-based authentication
  - Role-based access control (Admin, Doctor, User)

- Pet Managementc

  - Add and update pet information
  - View pet's visit History
  - Track pet medical history

- Visit Management

  - Schedule veterinary appointments
  - Manage visits (Edit/Cancel)
  - Track visit history

## Technology Stack

### Backend

- Java 21
- Spring Boot 3
- Spring Security
- AWS DynamoDB
- JWT Authentication
- RESTful API

### Frontend

- React
- JavaScript
- Tailwind CSS
- React Router
- Axios
- Context API for State Management

## API Endpoints

### Authentication

- POST `/api/v1/auth/register` - Register new user
- POST `/api/v1/auth/login` - User login
- POST `/api/v1/auth/logout` - User logout

### Pets

- POST `/api/v1/pets/add` - Create new pet
- GET `/api/v1/pets/{petId}` - Get pet details
- GET `/api/v1/pets/user/{userId}` - Get user pets
- PATCH `/api/v1/pets/{petId}` - Update pet information
- DELETE `/api/v1/pets/{petId}` - Delete pet

### Visits

- POST `/api/v1/visits/add` - Schedule new visit
- GET `/api/v1/visits/{visitId}` - Get visit details
- GET `/api/v1/visits/user/{visitId}` - Get user visit details
- PATCH `/api/v1/visits/{visitId}` - Update visit information
- DELETE `/api/v1/visits/{visitId}` - Cancel visit

## Getting Started

- Clone the repository
- Configure AWS credentials

  - Create `.aws` folder at your home directory
  - Inside `.aws` folder create `credentials` (without .txt) file and put _your access key id_ and _secret access key_

  ```bash
  [default]
  aws_access_key_id = your-access-key-id
  aws_secret_access_key = your-secret-access-key
  ```

- Set up DynamoDB tables
- Open and start back-end application
  - `mvn clean install`
  - `mvn spring-boot:run`
- Navigate to front-end `cd frontend`
- Install all dependencies by typing `npm i` in the terminal
- To start the frontend type `npm run dev`

## Team

| DEV
|-----------------------------------------------------|
|[Eglė Juknevičiūtė](https://github.com/eglereact) |
|[Žydrūnas Daukšas](https://github.com/ZeyDy) |
|[Ovidijus Eitminavičius](https://github.com/Ovii2) |
|[Renata V.](https://github.com/Renseva) |
