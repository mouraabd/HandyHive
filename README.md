# 🛠️ HandyHive – Full-Stack Service Marketplace

**CTU FIT – BI-TJV Semestral Project**  
**Author:** Abdul Rahman Mourad  
**Academic Year:** 2025/2026  
**Status:** ✅ Deployed and working

---

## 🌍 Live Deployment

| Layer | Platform | URL / Notes |
|------|----------|-------------|
| **Frontend** | **Vercel** | `https://handy-hive-mocha.vercel.app` |
| **Backend REST API** | **Render** | `https://handyhive-api.onrender.com` |
| **Database** | **Neon PostgreSQL** | Serverless PostgreSQL database used by the deployed backend |
| **API Documentation** | **Swagger / OpenAPI** | `https://handyhive-api.onrender.com/swagger-ui/index.html` |
| **OpenAPI JSON** | **SpringDoc** | `https://handyhive-api.onrender.com/v3/api-docs` |

> Note: The backend is deployed on a free cloud instance, so the first request may take some time while the Render service and Neon database wake up.

---

## 📋 Project Overview

**HandyHive** is a full-stack client–server web application that connects customers with local service providers such as plumbers, electricians, cleaners, and handymen.

The system supports the full service booking flow: account registration, role-based access, service browsing, provider service selection, multi-service checkout, job creation, job tracking, profile management, password changes, account deletion, and rating completed jobs.

### Key Capabilities

- 🔍 **Service Discovery:** customers can browse available services and prices
- 👷 **Provider Management:** providers can register, select offered services, and manage profiles
- 🛒 **Booking & Checkout:** customers can add multiple services to a cart and confirm checkout
- ⚡ **Urgent Job Prioritization:** urgent jobs can be marked during booking
- 📌 **Provider Matching:** providers are matched based on the service they offer
- 👤 **Role-Based UI:** customers and providers see different navigation/options
- 🔐 **Authentication:** login, registration, password hashing, and protected account features
- ⭐ **Rating System:** customers can rate completed jobs
- 📄 **API Documentation:** backend endpoints are documented through Swagger/OpenAPI
- ☁️ **Cloud Deployment:** frontend, backend, and database are deployed separately using Vercel, Render, and Neon

---

## 🏗️ Architecture

HandyHive follows a **three-tier architecture** with clear separation between the frontend, backend, and database.

```text
┌──────────────────────────────┐
│          Vercel              │
│  Static Frontend             │
│  HTML / CSS / JavaScript     │
└──────────────┬───────────────┘
               │ HTTPS REST API calls
               ▼
┌──────────────────────────────┐
│          Render              │
│  Java Spring Boot REST API   │
│  Controllers / Services / JPA│
└──────────────┬───────────────┘
               │ JDBC over SSL
               ▼
┌──────────────────────────────┐
│          Neon                │
│  Serverless PostgreSQL DB    │
│  Persistent cloud storage    │
└──────────────────────────────┘
```

### 1) Persistence Layer

- **ORM:** Hibernate / JPA
- **Database:** PostgreSQL 17
- **Cloud DB:** Neon serverless PostgreSQL
- **Repositories:**
  - `CustomerRepository`
  - `ProviderRepository`
  - `ServiceRepository`
  - `JobRepository`
  - `RatingRepository`
- **Relationship handling:** One-to-Many, One-to-One, and Many-to-Many mappings
- **Many-to-Many table:** `provider_services`

### 2) Business Layer

- **Services:**
  - `CustomerService`
  - `ProviderService`
  - `JobService`
  - `RatingService`
- **Business rules:**
  - Duplicate email prevention
  - Password hashing
  - Czech phone number validation
  - Customer/provider role handling
  - Provider-service matching
  - Multi-service checkout logic
  - Account deletion cleanup
- **Transactions:** `@Transactional` is used for operations involving multiple database changes

### 3) Presentation Layer

- **Framework:** Spring Boot REST API
- **Controllers:**
  - `CustomerController`
  - `ProviderController`
  - `ServiceController`
  - `JobController`
  - `RatingController`
- **API style:** RESTful endpoints using GET, POST, PUT, DELETE
- **Error handling:** global exception handling for clean 4xx responses instead of unclear 500 errors
- **Documentation:** Swagger/OpenAPI using SpringDoc

---

## ☁️ Cloud Deployment Details

This project is not only a local prototype. It is deployed as a real multi-tier cloud application.

### 1) Database Deployment – Neon PostgreSQL

The production database is hosted on **Neon**, a serverless PostgreSQL platform.

#### Neon responsibilities in the project

- Stores all persistent application data
- Hosts tables for customers, providers, services, jobs, ratings, and provider-service associations
- Provides the PostgreSQL connection used by the Render backend
- Keeps data persistent even when the backend service restarts

#### Neon configuration notes

The backend connects to Neon using environment variables instead of hardcoded credentials.

Recommended Render environment variables:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<neon-username>
SPRING_DATASOURCE_PASSWORD=<neon-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Important deployment note:

- For Hibernate schema creation/update, the direct Neon PostgreSQL connection is safer than the transaction pooler connection.
- Credentials must never be committed to Git.
- Database tables are generated/updated by Hibernate using `spring.jpa.hibernate.ddl-auto=update`.
- Default services can be seeded automatically by the backend so provider registration has services to load.

---

### 2) Backend Deployment – Render

The Java Spring Boot backend is deployed on **Render** as a web service.

#### Render responsibilities in the project

- Runs the Spring Boot REST API
- Exposes endpoints under `https://handyhive-api.onrender.com`
- Connects to Neon PostgreSQL through environment variables
- Serves Swagger/OpenAPI documentation
- Handles business logic, validation, persistence, and role-based behavior

#### Render service settings

Typical Render configuration:

```text
Root Directory: HandyHive-Backend
Build Command: ./gradlew clean build
Start Command: java -jar build/libs/*.jar
```

If Gradle permission issues occur on Render, run locally and commit the permission change:

```bash
chmod +x HandyHive-Backend/gradlew
git add HandyHive-Backend/gradlew
git commit -m "Make Gradle wrapper executable"
git push
```

#### Render environment variables

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<neon-username>
SPRING_DATASOURCE_PASSWORD=<neon-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Optional environment variables:

```env
SERVER_PORT=8082
SPRING_PROFILES_ACTIVE=prod
```

#### Backend public URLs

```text
Backend API:   https://handyhive-api.onrender.com
Swagger UI:    https://handyhive-api.onrender.com/swagger-ui/index.html
OpenAPI JSON:  https://handyhive-api.onrender.com/v3/api-docs
```

#### Real deployment issues solved

During deployment, several real-world problems were solved:

- Fixed Gradle wrapper execution permission on Render
- Configured environment variables for cloud database access
- Connected Render backend to Neon PostgreSQL
- Adjusted the Neon connection so Hibernate could build/update schemas
- Fixed CORS issues between the Vercel frontend and Render backend
- Added clearer backend validation/error handling
- Added loading behavior for backend cold starts
- Seeded default services so provider registration works on a fresh cloud database

---

### 3) Frontend Deployment – Vercel

The frontend is deployed on **Vercel** as a static web application.

#### Vercel responsibilities in the project

- Hosts the public user interface
- Serves HTML, CSS, JavaScript, Bootstrap assets, and images
- Calls the Render backend REST API using `fetch`
- Provides the live website link for users to test the application

#### Vercel deployment settings

Typical Vercel configuration:

```text
Root Directory: HandyHive-Frontend
Framework Preset: Other
Build Command: none
Output Directory: .
Install Command: none
```

Because the frontend is static, no Node.js build step is required.

#### Frontend API connection

The frontend calls the deployed backend API:

```javascript
const API_BASE = "https://handyhive-api.onrender.com";
```

The backend must allow the Vercel origin in CORS configuration:

```text
https://handy-hive-mocha.vercel.app
```

#### Frontend public URL

```text
https://handy-hive-mocha.vercel.app
```

---

## 🗄️ Data Model

The application uses **5 main entities** with **6 database tables** including the many-to-many join table.

### Relationships

- **Customer → Job**: One customer can book many jobs
- **Provider → Job**: One provider can perform many jobs
- **Provider ↔ Service**: Many providers can offer many services
- **Job → Rating**: One completed job can have one rating

### 1) Customer

Represents a client seeking services.

Typical fields:

- `id`
- `firstName`
- `lastName`
- `email`
- `passwordHash`
- `phoneNumber`
- `role`
- `bio`

### 2) Provider

Represents a professional offering services.

Typical fields:

- `providerId`
- `firstName`
- `lastName`
- `email`
- `passwordHash`
- `phoneNumber`
- `bio`
- `vettingDocPath`
- `isVetted`
- `avgRating`
- `subscriptionId`

### 3) Service

Represents a service category offered in the marketplace.

Typical fields:

- `serviceId`
- `name`
- `description`
- `basePrice`

### 4) Job

Represents a booking created through checkout.

Typical fields:

- `jobId`
- `description`
- `isUrgent`
- `status`
- `dateCreated`
- `customer`
- `provider`
- `service`

### 5) Rating

Represents customer feedback for a completed job.

Typical fields:

- `ratingId`
- `score`
- `comment`
- `job`

---

## 🔗 Many-to-Many Relationship

### Provider ↔ Service via `provider_services`

A provider can offer multiple services, and each service can be offered by multiple providers.

```sql
CREATE TABLE provider_services (
  provider_id BIGINT REFERENCES provider(provider_id),
  service_id  BIGINT REFERENCES service(service_id),
  PRIMARY KEY (provider_id, service_id)
);
```

Example JPA mapping:

```java
@ManyToMany
@JoinTable(
  name = "provider_services",
  joinColumns = @JoinColumn(name = "provider_id"),
  inverseJoinColumns = @JoinColumn(name = "service_id")
)
private Set<Service> services = new HashSet<>();
```

---

## ⚙️ Technologies Used

### Backend

- Java 21
- Spring Boot 3.4.1
- Spring Web
- Spring Security
- Hibernate / Spring Data JPA
- PostgreSQL 17
- Gradle 8.14
- Swagger / OpenAPI
- JUnit 5
- Mockito

### Frontend

- HTML5
- CSS3
- Bootstrap 5
- JavaScript ES6
- Browser LocalStorage
- Fetch API

### Database & Deployment

- Neon PostgreSQL
- Render Web Service
- Vercel Static Hosting
- Docker / Docker Compose for local development
- Git / GitLab versioning

---

## 🚀 Main Features

### Customer Features

- Register as customer
- Log in and log out
- Browse services
- Add services to cart
- Mark jobs as urgent
- Complete checkout
- Create job requests
- View job history
- Edit profile
- Change password
- Delete account
- Rate completed jobs

### Provider Features

- Register as provider
- Select offered services during registration
- Upload optional vetting document/certificate
- Log in and log out
- View assigned jobs
- Update job status
- Edit profile
- Change password
- Delete account
- Manage subscription/profile information

### System Features

- Role-based navigation
- Provider access restriction from customer-only service purchase flow
- Multi-service checkout
- Provider matching by service
- PostgreSQL persistence
- Global exception handling
- Swagger/OpenAPI documentation
- Cloud deployment

---

## ⭐ Complex Business Operation

### Multi-Service Checkout + Provider Assignment

A customer can select multiple services, optionally mark some as urgent, and confirm all selected services through checkout.

This action triggers multiple backend operations:

1. Read selected services
2. Find matching providers for each service
3. Create one job per selected service
4. Assign the selected/matched provider
5. Store the jobs in PostgreSQL
6. Return job confirmations to the frontend
7. Allow customer/provider job tracking after checkout

This satisfies the requirement for a complex client operation composed of multiple server calls.

---

## 🔍 Complex JPQL Query

The backend includes JPQL queries beyond basic CRUD, such as provider recommendation/rating aggregation.

Example goal:

- Calculate provider average rating
- Use rating and service matching to recommend providers

Example JPQL:

```java
@Query("SELECT AVG(r.score) FROM Rating r WHERE r.provider.providerId = :providerId")
Double calculateAverageRatingByProviderId(Long providerId);
```

This allows aggregation to be performed directly in the database.

---

## 📡 API Endpoints

### Swagger / OpenAPI

```text
Swagger UI:   https://handyhive-api.onrender.com/swagger-ui/index.html
OpenAPI JSON: https://handyhive-api.onrender.com/v3/api-docs
```

### Services – `/api/services`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/services` | Create service |
| GET | `/api/services` | List services |
| GET | `/api/services/{id}` | Get service by ID |
| PUT | `/api/services/{id}` | Update service |
| DELETE | `/api/services/{id}` | Delete service |

### Customers – `/api/customers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customers` | Register customer |
| GET | `/api/customers` | List customers |
| GET | `/api/customers/{id}` | Get customer |
| PUT | `/api/customers/{id}` | Update customer |
| PUT | `/api/customers/{id}/password` | Change customer password |
| DELETE | `/api/customers/{id}` | Delete customer |

### Providers – `/api/providers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/providers` | Register provider using multipart form data |
| GET | `/api/providers` | List providers |
| GET | `/api/providers/{id}` | Get provider |
| PUT | `/api/providers/{id}` | Update provider |
| PUT | `/api/providers/{id}/password` | Change provider password |
| DELETE | `/api/providers/{id}` | Delete provider |
| GET | `/api/providers/recommendations?serviceId={id}` | Recommended providers |
| GET | `/api/providers/match/{serviceId}` | Match provider for checkout |

### Provider ↔ Service Association

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/providers/{providerId}/services` | List provider services |
| PUT | `/api/providers/{providerId}/services` | Replace provider services |
| PUT | `/api/providers/{providerId}/services/{serviceId}` | Add service to provider |
| DELETE | `/api/providers/{providerId}/services/{serviceId}` | Remove service from provider |

### Jobs – `/api/jobs`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/jobs` | Create job during checkout |
| GET | `/api/jobs/customer/{customerId}` | Customer job history |
| GET | `/api/jobs/provider/{providerId}` | Provider job history |
| PUT | `/api/jobs/{jobId}` | Update job |
| PUT | `/api/jobs/{jobId}/status` | Update job status |
| DELETE | `/api/jobs/{jobId}` | Delete job |

### Ratings

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/jobs/{jobId}/rating` | Create rating for job |
| GET | `/api/ratings` | List ratings |
| GET | `/api/ratings/{id}` | Get rating |
| DELETE | `/api/ratings/{id}` | Delete rating |

### Status Codes

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`
- `415 Unsupported Media Type`

---

## 🧪 Testing

The project includes automated tests across different layers.

### Unit Tests

- Test service-layer business logic
- Use Mockito to mock repositories
- Example: `ProviderServiceTest.java`

### Repository Tests

- Test JPA repositories and JPQL queries
- Use `@DataJpaTest`
- Example: `RatingRepositoryTest.java`

### Controller / Integration Tests

- Test REST endpoints
- Verify response codes and request handling
- Use MockMvc / Spring Boot test utilities
- Example: `JobControllerTest.java`

### Run Tests

```bash
cd HandyHive-Backend
./gradlew test
```

### Build Backend

```bash
cd HandyHive-Backend
./gradlew clean build
```

---

## 🐳 Local Docker Setup

From the project root:

```bash
docker compose up -d --build
```

Stop containers:

```bash
docker compose down
```

Docker is useful for local development with a local PostgreSQL container. The deployed production database is hosted separately on Neon.

---

## 🔧 Manual Local Setup

### Prerequisites

- Java 21
- PostgreSQL 17
- Gradle Wrapper included in project

### 1) Create local PostgreSQL database

```sql
CREATE DATABASE handyhive;
```

### 2) Configure database credentials

Use environment variables or edit local configuration:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/handyhive
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### 3) Run backend locally

```bash
cd HandyHive-Backend
./gradlew bootRun
```

Local backend:

```text
http://localhost:8082
```

Swagger locally:

```text
http://localhost:8082/swagger-ui/index.html
```

### 4) Run frontend locally

```bash
cd HandyHive-Frontend
python -m http.server 63342
```

Then open:

```text
http://localhost:63342
```

---

## 🔐 Environment Variables

### Backend production variables on Render

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<neon-username>
SPRING_DATASOURCE_PASSWORD=<neon-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Optional:

```env
SERVER_PORT=8082
SPRING_PROFILES_ACTIVE=prod
```

### Security Notes

- Do not commit `.env` files containing database credentials.
- Use Render environment variables for production secrets.
- Use Neon direct database credentials only in the deployment environment.
- Keep frontend public URLs separate from backend secrets.

---

## 📚 API Usage Examples

### Create Service

```powershell
$payload = @{ name = "Plumbing"; description="Fix pipes"; basePrice=500 } | ConvertTo-Json -Compress
$payload | Set-Content -NoNewline -Encoding utf8 payload.json
curl.exe -i -X POST "https://handyhive-api.onrender.com/api/services" -H "Content-Type: application/json" --data-binary "@payload.json"
```

### Register Customer

```powershell
$payload = @{
  firstName = "Test"
  lastName = "Customer"
  email = "customer@example.com"
  phoneNumber = "+420 777 123 456"
  password = "123456"
} | ConvertTo-Json -Compress

$payload | Set-Content -NoNewline -Encoding utf8 customer.json
curl.exe -i -X POST "https://handyhive-api.onrender.com/api/customers" -H "Content-Type: application/json" --data-binary "@customer.json"
```

### Register Provider

```powershell
curl.exe -i -X POST "https://handyhive-api.onrender.com/api/providers" `
  -F "firstName=John" `
  -F "lastName=Doe" `
  -F "email=john@example.com" `
  -F "password=123456" `
  -F "phone=+420 777 123 456" `
  -F "bio=Experienced provider" `
  -F "serviceIds=1" `
  -F "serviceIds=2"
```

### Create Job

```json
{
  "userId": 1,
  "providerId": 2,
  "serviceId": 1,
  "isUrgent": true,
  "description": "Booked via HandyHive checkout"
}
```

---

## 🧭 User Flow

### Customer Flow

```text
Register/Login → Browse Services → Add to Cart → Checkout → Job Created → Track Job → Rate Completed Job
```

### Provider Flow

```text
Register/Login → Select Services → View Assigned Jobs → Update Job Status → Manage Profile
```

---

## 🧩 Project Structure

```text
HandyHive-Project/
├── HandyHive-Backend/
│   ├── src/main/java/com/handyhive/backend/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── util/
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── src/test/
│   ├── build.gradle
│   └── Dockerfile
├── HandyHive-Frontend/
│   ├── assets/
│   ├── js/
│   ├── styles.css
│   └── *.html
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## 🛠️ Troubleshooting

### Backend takes a long time to respond

When using free cloud services, the backend may sleep after inactivity. The first request can take some time while Render starts the service and reconnects to Neon.

### Provider services do not load during registration

Check:

1. Render backend is awake
2. `/api/services` returns service data
3. Neon database has seeded default services
4. CORS allows the Vercel frontend origin

### CORS error from frontend

Make sure the backend allows:

```text
https://handy-hive-mocha.vercel.app
```

### Gradle build fails on Render

Make sure `gradlew` is executable:

```bash
chmod +x HandyHive-Backend/gradlew
```

### Hibernate does not create/update tables on Neon

Use the direct Neon database connection string with SSL:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
```

Avoid relying on a pooler connection for schema generation if Hibernate has trouble creating/updating tables.

---

## 🎓 Academic Requirements Met

### Server Requirements

- [x] Three-layer architecture
- [x] Java Spring backend
- [x] ORM with Hibernate/JPA
- [x] PostgreSQL relational database
- [x] At least 3 entities
- [x] Many-to-many association with CRUD behavior
- [x] Complex JPQL query
- [x] REST API with proper HTTP methods and status codes
- [x] Global exception handling
- [x] OpenAPI documentation
- [x] Automated tests
- [x] Gradle build
- [x] Git versioning

### Client Requirements

- [x] UI application
- [x] Uses REST backend
- [x] Complex multi-call checkout operation
- [x] Role-based customer/provider behavior

### Deployment Requirements / Extra Work

- [x] Frontend deployed on Vercel
- [x] Backend deployed on Render
- [x] Database deployed on Neon PostgreSQL
- [x] Cloud environment variables configured
- [x] CORS configured for deployed frontend/backend communication
- [x] Production-style debugging and deployment fixes completed

---

## 👨‍💻 Author

**Abdul Rahman Mourad**  
Czech Technical University in Prague  
Faculty of Information Technology  
Course: BI-TJV Java Technologies

---

## 📌 Current Status

✅ Backend deployed  
✅ Frontend deployed  
✅ Neon database connected  
✅ Customer and provider registration working  
✅ Checkout/job creation working  
✅ Profile editing/password/account deletion working  
✅ Ready for demo, feedback, and further improvements
