🛠️ HandyHive - Service Marketplace
CTU FIT - BI-TJV Semestral Project Author: Abdulrehman Academic Year: 2024/2025

📋 Project Overview
HandyHive is a full-stack client-server application designed to connect customers with local service providers (plumbers, electricians, cleaners, etc.). The system facilitates the entire booking flow, from browsing services to secure checkout and job management. It features a robust Spring Boot REST API backend with a PostgreSQL database and a responsive HTML/JS frontend.

Key Capabilities:

🔍 Service Discovery: Customers can browse and filter services by category.

👷 Provider Management: Providers can register, upload vetting documents, and manage their offerings.

🛒 Booking System: Shopping cart functionality with "Urgent" job prioritization.

💳 Secure Checkout: Integrated transaction simulation creating jobs and assigning providers.

⭐ Rating System: Customers can rate completed jobs to ensure quality control.

🏗️ Architecture
The project implements a strict three-layer architecture as required:

1. Persistence Layer (Data Access)
   JPA Repositories: CustomerRepository, ProviderRepository, JobRepository, RatingRepository, ServiceRepository.

ORM: Hibernate for object-relational mapping.

Database: PostgreSQL 17 (Production) / H2 (Testing).

Complex Queries: JPQL used for aggregating ratings and filtering providers.

2. Business Layer (Application Logic)
   Services: CustomerService, ProviderService, JobService, RatingService.

Transaction Management: @Transactional ensures data integrity during job creation and profile updates.

Business Rules: Duplicate email prevention, file upload handling, password encryption.

Security: BCrypt password encoding for both Customers and Providers.

3. Presentation Layer (REST API)
   Controllers: CustomerController, ProviderController, JobController, RatingController.

HTTP Methods: GET, POST, PUT, DELETE.

Status Codes: 200 OK, 201 Created, 400 Bad Request, 404 Not Found, 409 Conflict.

API Documentation: Swagger/OpenAPI 3.

🗄️ Data Model
Entities and Relationships
The application uses 5 main entities with 6 database tables.

Entity Relationships:

Customer → Job (One-to-Many)

One customer can book multiple jobs.

Foreign key: customer_id in job table.

Provider → Job (One-to-Many)

One provider can perform multiple jobs.

Foreign key: provider_id in job table.

Provider ↔ Service (Many-to-Many via Join Table)

A provider can offer multiple services (e.g., Plumbing and Handyman).

A service is offered by multiple providers.

Join Table: provider_services.

Job → Rating (One-to-One)

A completed job can have one rating associated with it.

Foreign key: job_id in rating table.

1. Customer
   Represents a client seeking services.

Attributes: id, firstName, lastName, email, passwordHash, phoneNumber, role.

2. Provider
   Represents a professional offering services.

Attributes: providerId, firstName, lastName, email, passwordHash, bio, vettingDocPath, isVetted, avgRating.

3. Job
   Represents a service contract.

Attributes: jobId, description, isUrgent, status (PENDING, COMPLETED), dateCreated.

4. Service
   Represents the catalog of available skills.

Attributes: serviceId, name, basePrice, description.

5. Rating
   Feedback left by a customer.

Attributes: ratingId, score (Double), comment.

⚙️ Technologies
Backend
Java 21 - Programming language.

Spring Boot 3.4.1 - Application framework.

Hibernate - ORM implementation.

PostgreSQL 17 - Relational database.

Gradle 8.14 - Build tool.

Frontend
HTML5 - Structure.

CSS3 / Bootstrap 5 - Styling.

JavaScript (ES6) - Client-side logic.

Development
Docker - Containerization.

GitLab CI/CD - Automated testing.

JUnit 5 & Mockito - Testing framework.

🚀 Key Features
✅ Complete CRUD Operations
Provider Management:

Create/Register providers.

Read provider profiles.

Update details (Bio, Phone).

Delete accounts.

Job Management:

Create jobs (Booking).

Read jobs (History).

Update status (Completion).

⭐ Complex Business Operation: Provider Registration with File
Endpoint: POST /api/providers (Multipart Request)

This operation demonstrates handling binary data alongside structured data:

Validation: Checks if email exists.

File Processing: Saves the uploaded PDF/Image to server storage.

Security: Hashes the password.

Persistence: Saves entity with file path reference.


🔍 Complex JPQL Query
Query: calculateAverageRatingByProviderId Location: RatingRepository.java
@Query("SELECT AVG(r.score) FROM Rating r WHERE r.provider.providerId = :providerId")
Double calculateAverageRatingByProviderId(Long providerId);

Value: Efficiently aggregates scores at the database level instead of loading thousands of rating objects into Java memory.


🔗 Many-to-Many Relationship
Implementation: Provider ↔ Service

Providers can have multiple skills, and a skill (like "Plumbing") belongs to many providers.

Database Structure:
CREATE TABLE provider_services (
provider_id BIGINT REFERENCES provider(provider_id),
service_id BIGINT REFERENCES service(service_id),
PRIMARY KEY (provider_id, service_id)
);

Java Implementation:
// In Provider entity
@ManyToMany
@JoinTable(
name = "provider_services",
joinColumns = @JoinColumn(name = "provider_id"),
inverseJoinColumns = @JoinColumn(name = "service_id")
)
private Set<Service> services = new HashSet<>();


📡 API Endpoints
User Management
Method,Endpoint,Description
POST,/api/customers,Register customer
GET,/api/customers/{id},Get profile

Provider Management
Method,Endpoint,Description
POST,/api/providers,Register (Multipart)
GET,/api/providers,List all providers
PUT,/api/providers/{id},Update profile

Job System
Method,Endpoint,Description
POST,/api/jobs,Create Job
GET,/api/jobs/user/{id},Customer History
GET,/api/jobs/provider/{id},Provider History
POST,/api/jobs/{id}/rating,Rate a job


🧪 Testing
The project includes 31 Automated Tests covering all layers.

1️⃣ Unit Tests (Service Layer)
File: ProviderServiceTest.java

Approach: Uses Mockito to verify business logic without database.

@Test
void testRegisterProvider_Success() {
when(providerRepository.save(any())).thenReturn(new Provider());
providerService.registerProvider(new Provider());
verify(providerRepository).save(any());
}

2️⃣ Repository Tests (Data Layer)
File: RatingRepositoryTest.java

Approach: Uses @DataJpaTest with H2 to verify JPQL queries.
@Test
void testCalculateAverageRating() {
Double avg = ratingRepository.calculateAverageRatingByProviderId(1L);
assertThat(avg).isEqualTo(4.0);
}


3️⃣ Integration Tests (Controller Layer)
File: JobControllerTest.java

Approach: Uses MockMvc to test HTTP status codes and JSON.
@Test
void testCreateJob() throws Exception {
mockMvc.perform(post("/api/jobs")
.contentType(MediaType.APPLICATION_JSON)
.content(json))
.andExpect(status().isOk());
}

Running Tests
./gradlew test

🐳 Docker Deployment
### Quick Start
You can spin up the entire environment (Database + Application) using Docker Compose.

```bash
# Start services
docker-compose up --build

# Stop services
docker-compose down

Configuration (docker-compose.yml)
PostgreSQL: Official postgres:15-alpine image.

Application: Builds from local source.

Port: Backend is accessible at http://localhost:8082.
```

📚 API Usage Examples
1. Create a Job
   Request: POST /api/jobs
   {
   "userId": 1,
   "providerId": 5,
   "serviceId": 2,
   "description": "Leaking faucet",
   "isUrgent": true
   }
   Response: 200 OK


2. Register Provider
   Request: POST /api/providers (Multipart)

firstName: "John"

email: "john@work.com"

document: [Binary File]

Response: 200 OK


📖 Project Structure
HandyHive-Project/
├── .idea/                       # IDE Configuration
├── HandyHive-Backend/           # Backend Source Code
│   ├── build.gradle             # Dependencies
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/handyhive/backend/
│   │   │   │   ├── config/      # App Config
│   │   │   │   ├── controller/  # REST Controllers
│   │   │   │   ├── dto/         # Data Transfer Objects
│   │   │   │   ├── exception/   # Global Error Handling
│   │   │   │   ├── model/       # JPA Entities
│   │   │   │   ├── repository/  # Data Access Interfaces
│   │   │   │   ├── service/     # Business Logic
│   │   │   │   └── util/        # Helper classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                # Unit & Integration Tests
│   └── uploads/                 # Storage for vetted documents
│   └── Dockerfile
├── HandyHive-Frontend/          # Frontend Source Code
│   ├── css/
│   ├── js/
│   ├── index.html
│   └── checkout.html
├── .gitlab-ci.yml               # CI/CD Pipeline Config
├── docker-compose.yml           # Docker Orchestration
└── README.md                    # Project Documentation


🎓 Academic Requirements Met
Server Requirements
[x] Three-layer architecture: Persistence, Business, Presentation.

[x] Spring Framework: Spring Boot 3.4.1, Java 21.

[x] Relational Database: PostgreSQL 17.

[x] Entities: 5 Entities + M:N relationship.

[x] Complex Query: JPQL Aggregation in RatingRepository.

[x] Testing: 31 Tests (Unit + Integration + Repository).

[x] Build System: Gradle.

Client Requirements
[x] User Interface: Web-based HTML/JS.

[x] REST Integration: Fetch API used for all operations.

[x] Complex Operation: Multi-step Checkout & File Upload.

👨‍💻 Author
Abdul Rahman Mourad Czech Technical University in Prague Faculty of Information Technology Course: BI-TJV (Java Technologies)

Last Updated: January 4, 2026 Status: ✅ Production Ready - All Tests Passing (31/31)
