# 🛠️ HandyHive – Service Marketplace

**CTU FIT – BI-TJV Semestral Project**  
**Author:** Abdul Rahman Mourad  
**Academic Year:** 2025/2026

---

## 📋 Project Overview

**HandyHive** is a full-stack client–server application that connects customers with local service providers (plumbers, electricians, cleaners, etc.). The system supports the full booking flow from browsing services to checkout, job management, and rating completed jobs.

**Key Capabilities**
- 🔍 **Service Discovery:** browse services and prices
- 👷 **Provider Management:** providers register, choose offered services, and manage profiles
- 🛒 **Booking & Checkout:** multi-service cart + urgent prioritization
- 📌 **Smart Provider Matching:** providers are selected based on availability/criteria (not always the same provider)
- ⭐ **Rating System:** customers rate completed jobs
- 📄 **API Documentation:** Swagger / OpenAPI

---

## 🏗️ Architecture

The project implements the required **three-layer architecture**:

### 1) **Persistence Layer (Data Access)**
- **ORM:** Hibernate (JPA)
- **Repositories:** `CustomerRepository`, `ProviderRepository`, `ServiceRepository`, `JobRepository`, `RatingRepository`
- **Database:** PostgreSQL 17 with persistent storage (Docker or external DB server)
- **Many-to-Many:** Provider ↔ Service via join table `provider_services`
- **Complex query:** JPQL aggregation for provider rating / recommendations

### 2) **Business Layer (Application Logic)**
- **Services:** `CustomerService`, `ProviderService`, `JobService`, `RatingService`
- **Transactions:** `@Transactional` used for multi-step operations (checkout, registration updates, etc.)
- **Rules:** duplicate email prevention, password hashing, validation, provider matching

### 3) **Presentation Layer (REST API)**
- **Controllers:** `CustomerController`, `ProviderController`, `ServiceController`, `JobController`, `RatingController`
- **REST:** GET, POST, PUT, DELETE + proper status codes
- **Error handling:** 400 / 404 / 409 / 415 for invalid requests (no HTTP 500 for client mistakes)
- **Docs:** Swagger/OpenAPI 3 endpoints exposed

---

## 🗄️ Data Model

### Entities and Relationships

The application uses **5 main entities** with **6 database tables** (5 entities + 1 join table):

#### Relationships
- **Customer → Job (One-to-Many)**
    - One customer can book multiple jobs
    - FK: `customer_id` in `job`

- **Provider → Job (One-to-Many)**
    - One provider can perform multiple jobs
    - FK: `provider_id` in `job`

- **Provider ↔ Service (Many-to-Many)**
    - Provider offers many services; service can be offered by many providers
    - Join table: `provider_services`

- **Job → Rating (One-to-One)**
    - Completed job can have one rating
    - FK: `job_id` in `rating`

### 1) Customer
Represents a client seeking services.

**Attributes (typical):**
- `id`, `firstName`, `lastName`, `email`, `passwordHash`, `phoneNumber`, `role`

### 2) Provider
Represents a professional offering services.

**Attributes (typical):**
- `providerId`, `firstName`, `lastName`, `email`, `passwordHash`, `bio`, `vettingDocPath`, `isVetted`, `avgRating`

### 3) Service
Represents a service category offered in the catalog.

**Attributes (typical):**
- `serviceId`, `name`, `description`, `basePrice`

### 4) Job
Represents a booking / service contract created by checkout.

**Attributes (typical):**
- `jobId`, `description`, `isUrgent`, `status` (PENDING/COMPLETED/CANCELLED), `dateCreated`

### 5) Rating
Feedback left by the customer for a completed job.

**Attributes (typical):**
- `ratingId`, `score`, `comment`

---

## ⚙️ Technologies

### Backend
- **Java 21**
- **Spring Boot 3.4.1**
- **Hibernate / Spring Data JPA**
- **PostgreSQL 17**
- **Gradle 8.14**
- **OpenAPI / Swagger**

### Frontend
- **HTML5**
- **CSS3 / Bootstrap 5**
- **JavaScript (ES6)**

### Development & Deployment
- **Docker & Docker Compose**
- **GitLab versioning**
- **JUnit 5 + Mockito** for automated tests

---

## 🚀 Key Features

### ✅ Complete CRUD Operations
Full CRUD is implemented for:
- **Customers**
- **Providers**
- **Services**
- **Jobs**
- **Ratings**
- ✅ plus CRUD operations over the **Provider ↔ Service** many-to-many association

---

## ⭐ Complex Business Operation (Client)

### Multi-Service Checkout + Provider Assignment
**Business operation:** a customer selects **multiple services** (cart), optionally marks the order as **urgent**, and then performs checkout.

This single action triggers **multiple CRUD operations** through multiple API calls:
1. Fetch selected services (READ)
2. Find/choose matching providers for each service (READ + complex logic)
3. Create one or more jobs (CREATE)
4. Assign provider(s) to each job (UPDATE / association)
5. Return job confirmations (READ)

This is the required complex client operation composed of multiple server calls.

---

## 🔍 Complex JPQL Query (ORM)

The backend contains at least one **JPQL query** that goes beyond basic CRUD.

Example query goal:
- **Aggregate ratings** for a provider (average score), used in provider recommendations.

**Location:** `RatingRepository.java`  
**Example JPQL:**
```java
@Query("SELECT AVG(r.score) FROM Rating r WHERE r.provider.providerId = :providerId")
Double calculateAverageRatingByProviderId(Long providerId);
```

This performs aggregation directly in the database (efficient) and supports recommendation logic.

---

## 🔗 Many-to-Many Relationship

### Provider ↔ Service via `provider_services`

**Database table**
```sql
CREATE TABLE provider_services (
  provider_id BIGINT REFERENCES provider(provider_id),
  service_id  BIGINT REFERENCES service(service_id),
  PRIMARY KEY (provider_id, service_id)
);
```

**Java (Provider entity)**
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

## 📡 API Endpoints

### Swagger / OpenAPI
- **Swagger UI:** `http://localhost:8082/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8082/v3/api-docs`

### Services (`/api/services`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/services` | Create service (**201 Created**) |
| GET | `/api/services` | List services |
| GET | `/api/services/{id}` | Get service by ID |
| PUT | `/api/services/{id}` | Update service |
| DELETE | `/api/services/{id}` | Delete service |

### Customers (`/api/customers`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customers` | Register customer (**201 Created**) |
| GET | `/api/customers` | List customers |
| GET | `/api/customers/{id}` | Get customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Providers (`/api/providers`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/providers` | Register provider (multipart, **201 Created**) |
| GET | `/api/providers` | List providers |
| GET | `/api/providers/{id}` | Get provider |
| PUT | `/api/providers/{id}` | Update provider |
| DELETE | `/api/providers/{id}` | Delete provider |
| GET | `/api/providers/recommendations?serviceId={id}` | Recommended providers (complex query) |

### Provider ↔ Service association
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/providers/{providerId}/services` | List provider services |
| PUT | `/api/providers/{providerId}/services` | Replace provider services (list of IDs) |
| PUT | `/api/providers/{providerId}/services/{serviceId}` | Add service to provider (**204**) |
| DELETE | `/api/providers/{providerId}/services/{serviceId}` | Remove service from provider (**204**) |

### Jobs (`/api/jobs`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/jobs` | Create job (used by checkout) |
| GET | `/api/jobs/customer/{customerId}` | Customer job history |
| GET | `/api/jobs/provider/{providerId}` | Provider job history |
| PUT | `/api/jobs/{jobId}` | Update job (status/fields) |
| DELETE | `/api/jobs/{jobId}` | Delete job |

### Ratings
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/jobs/{jobId}/rating` | Create rating for job |
| GET | `/api/ratings` | List ratings |
| GET | `/api/ratings/{id}` | Get rating |
| DELETE | `/api/ratings/{id}` | Delete rating |

**Status codes used**
- `200 OK`, `201 Created`, `204 No Content`
- `400 Bad Request`, `404 Not Found`, `409 Conflict`, `415 Unsupported Media Type`

---

## 🧪 Testing

The project includes automated tests covering different layers:

### 1️⃣ Unit Tests (Service Layer)
- Example: `ProviderServiceTest.java`
- **Mockito** used to mock repositories and test business logic in isolation

### 2️⃣ Repository Tests (Data Layer)
- Example: `RatingRepositoryTest.java`
- Uses `@DataJpaTest` to validate JPQL queries

### 3️⃣ Controller / Integration Tests (API Layer)
- Example: `JobControllerTest.java`
- Uses MockMvc / Spring Boot test utilities to verify REST behavior and status codes

### Running tests
```bash
cd HandyHive-Backend
./gradlew test
```

---

## 🐳 Docker Deployment

### Quick Start (Recommended)
From project root:
```bash
docker compose up -d --build
```

**Backend API:** `http://localhost:8082`  
**Swagger UI:** `http://localhost:8082/swagger-ui/index.html`

Stop:
```bash
docker compose down
```

**Notes**
- PostgreSQL runs as a DB server container with persistent volume
- The backend uses environment variables from `.env` (if present)

---

## 🔧 Manual Setup (Without Docker)

### Prerequisites
- **Java 21**
- **PostgreSQL 17** (DB server)
- **Gradle Wrapper** included in project

### Database
Create a database/user in PostgreSQL and set the credentials in `application.properties` (or via env vars).

### Run Backend
```bash
cd HandyHive-Backend
./gradlew bootRun
```

Backend will run at: `http://localhost:8082`

---

## 🌐 Client Application (Frontend)

### Run the Client
The frontend is a static web app (HTML/CSS/JS). You can open it directly or serve it:

**Option 1: open HTML**
- Open `HandyHive-Frontend/index.html` in the browser

**Option 2: local HTTP server**
```bash
cd HandyHive-Frontend
python -m http.server 63342
```
Then open:
- `http://localhost:63342`

The client uses the backend REST API at `http://localhost:8082`.

---

## 📚 API Usage Examples

### Example: Create Service (Windows PowerShell)
```powershell
$payload = @{ name = "Plumbing"; description="Fix pipes"; basePrice=500 } | ConvertTo-Json -Compress
$payload | Set-Content -NoNewline -Encoding utf8 payload.json
curl.exe -i -X POST "http://localhost:8082/api/services" -H "Content-Type: application/json" --data-binary "@payload.json"
```

### Example: Provider registration (multipart)
```powershell
curl.exe -i -X POST "http://localhost:8082/api/providers" `
  -F "firstName=John" `
  -F "lastName=Doe" `
  -F "email=john@example.com" `
  -F "password=pass" `
  -F "phone=+420700000000" `
  -F "bio=Experienced provider" `
  -F "serviceIds=1" `
  -F "serviceIds=2"
```

---

## 📖 Project Structure

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
│   └── Dockerfile
├── HandyHive-Frontend/
│   ├── css/
│   ├── js/
│   └── *.html
├── docker-compose.yml
├── .env
└── README.md
```

---

## 🎓 Academic Requirements Met

### ✅ Server Requirements
- [x] Three-layer architecture (Persistence / Business / REST API)
- [x] Spring Framework in Java
- [x] ORM (Hibernate/JPA)
- [x] Relational DB server (PostgreSQL) with persistent storage
- [x] At least 3 entities (5 entities implemented)
- [x] Many-to-many association (Provider ↔ Service) + CRUD over association
- [x] Complex JPQL query (aggregation / recommendation support)
- [x] Proper REST (methods + correct HTTP status codes)
- [x] No HTTP 500 for invalid client requests (4xx instead)
- [x] OpenAPI documentation
- [x] Automated tests (3 types)
- [x] Gradle build with tests
- [x] Git versioned project (GitLab)

### ✅ Client Requirements
- [x] UI application (HTML/JS frontend)
- [x] Uses REST API as backend
- [x] Complex operation: checkout performs multiple calls and creates jobs

---

## 👨‍💻 Author

**Abdul Rahman Mourad**  
Czech Technical University in Prague  
Faculty of Information Technology  
Course: BI-TJV (Java Technologies)

**Last Updated:** January 2026  
**Status:** ✅ Ready for submission
