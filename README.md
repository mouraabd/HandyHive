# 🐝 HandyHive - Service Marketplace Platform

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green) ![Bootstrap](https://img.shields.io/badge/Frontend-Bootstrap_5-purple) ![Database](https://img.shields.io/badge/Database-MySQL-orange)

**HandyHive** is a comprehensive full-stack marketplace application designed to bridge the gap between local service professionals (Plumbers, Electricians, Handymen) and customers. It features a robust job lifecycle management system, secure booking workflows, and role-based access control.

---

## 🌟 Key Features

### 👤 For Customers
* **🛒 Multi-Service Booking:** Browse and add multiple services (Plumbing, Cleaning, etc.) to a single cart.
* **⚡ Smart Provider Matching:** The system automatically identifies and assigns the best available provider for the specific service type.
* **💳 Secure Checkout:** Integrated payment simulation with credit card validation.
* **📊 Order Tracking:** Monitor job status in real-time (Pending, In Progress, Completed).

### 👷‍♂️ For Providers
* **📋 Job Requests:** distinct dashboard to view incoming job offers.
* **✅ Workflow Management:** Accept jobs to start them and mark them as "Resolved" upon completion.
* **🛠 Profile Customization:** Manage skills, bio, and subscription tiers.
* **📈 History:** Access a complete log of past work and client interactions.

---

## 💻 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3 (Web, Data JPA, Security) |
| **Frontend** | HTML5, JavaScript (ES6), Bootstrap 5 |
| **Database** | MySQL (Production) / H2 (Development) |
| **Build Tool** | Gradle |
| **Security** | BCrypt Encryption, Spring Security RBAC |

---

## ⚙️ Installation & Setup

### 1. Prerequisites
Ensure you have the following installed:
* Java Development Kit (JDK) 17+
* Maven
* MySQL Server

### 2. Database Configuration
Open 'src/main/resources/application.properties` and configure your database connection:

```text
spring.datasource.url=jdbc:mysql://localhost:3306/handyhive_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update


3. Initialize Data (Required)
Execute the following SQL commands in your database to populate the Service Catalog and link them to the default Provider. This prevents "No Provider Found" errors during checkout.

-- 1. Create Service Catalog
INSERT INTO service (name, base_price) VALUES ('Plumbing', 1500);
INSERT INTO service (name, base_price) VALUES ('Electrician', 2000);
INSERT INTO service (name, base_price) VALUES ('Handyman', 800);
INSERT INTO service (name, base_price) VALUES ('Deep Cleaning', 2500);

-- 2. Link Default Provider (Jack) to Services
-- Note: Ensure a provider with ID 1 exists (Jack@Hero.com)
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 1); -- Plumbing
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 2); -- Electrician
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 3); -- Handyman
INSERT INTO provider_services (provider_id, service_id) VALUES (1, 4); -- Cleaning

4. Run the Application
Navigate to the project root and run:

Bash
**Windows:**
```bash
gradlew.bat bootRun

Mac/Linux:
./gradlew bootRun

The application will be accessible at: http://localhost:8082

### 🌐 Step 3: Add Client Running Instructions to `README.md`
You forgot to tell the teacher how to run the frontend.

1.  Open `README.md`.
2.  Scroll to the bottom of the "Installation & Setup" section (after "Run the Application").
3.  **Add this new section**:

```markdown
### 5. Run the Frontend (Client)
1. Navigate to the `HandyHive-Frontend` folder.
2. Open `index.html` in your web browser (Chrome/Edge recommended).
   * *Tip: For the best experience, use "Open with Live Server" in VS Code or the built-in browser preview in IntelliJ to avoid CORS issues.*
   
📖 User Guide
🔐 Authentication
Role,Email,Password
Provider,Jack@Hero.com,password
Customer,Alice@Client.com,password


🛒 Customer Workflow
Navigate to the Services page.

Select desired services (cards will highlight green).

Toggle "Urgent" if priority service is required.

Click Proceed to Checkout to verify the cart.

Complete the payment form to finalize the booking.


👷‍♂️ Provider Workflow
Log in to the Provider Dashboard.

Navigate to My Jobs to see "Pending" requests.

Click Accept to change status to IN_PROGRESS.

Once the work is done, click Mark Resolved to complete the job.



🔌 API Documentation
**API Examples:** Please refer to the `requests.http` file in the project root for executable request examples.

🔐 Auth & Users
Method,Endpoint,Description
POST,/api/auth/login,Authenticate user and retrieve session
POST,/api/users/register,Generic user registration
GET,/api/users/{id},Get basic user details
DELETE,/api/users/{id},Delete a user account permanently

🛠 Jobs & Bookings
Method,Endpoint,Description
POST,/api/jobs/create,Create a new job request (Booking)
PUT,/api/jobs/{jobId}/status,Update status (PENDING -> IN_PROGRESS -> COMPLETED)
GET,/api/jobs/customer/{id},Get booking history for a specific customer
GET,/api/jobs/provider/{id},Get job requests for a specific provider

👷‍♂️ Providers
Method,Endpoint,Description
GET,/api/providers/match/{serviceId},Find the best available provider for a service
GET,/api/providers/{id},Get provider profile details
PUT,/api/providers/{id},"Update provider profile (Bio, Phone, Name)"
PUT,/api/providers/{id}/subscription,Upgrade or Downgrade subscription plan
DELETE,/api/providers/{id},Delete provider account and associated data

👤 Customers
Method,Endpoint,Description
POST,/api/customers/register,Register a new customer account
GET,/api/customers/{id},Get customer profile details
GET,/api/customers/search,Find customer by email
DELETE,/api/customers/{id},Delete customer account

📦 Services
Method,Endpoint,Description
GET,/api/services,List all available services
GET,/api/services/{id},Get service details
POST,/api/services,Add a new service (Admin)
DELETE,/api/services/{id},Delete a service from the catalog (Admin)

⭐ Ratings
Method,Endpoint,Description
POST,/api/jobs/{jobId}/rating,Rate a completed job



👨‍💻 Author
Abdul Rahman Mourad BIE-TJV.21 Project