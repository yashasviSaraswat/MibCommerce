# 🛒 MigCommerce

MigCommerce is an e-commerce application built with **Spring Boot (backend)** and **React (frontend)**.  
It also uses **Redis** for caching and session management, and **MySQL** as the main database.

---

## 🚀 Project Setup

### 1. Clone the Repository
```
git clone https://github.com/your-username/migcommerce.git
cd migcommerce
```
2. Start Redis

Redis must be running before you start the backend.

Option A: Run Redis as a Windows Service (always on)
```
cd C:\Redis
redis-server.exe --service-install redis.windows.conf --loglevel verbose
redis-server.exe --service-start
```
Option B: Run Redis Manually (start only when using project)
```
cd C:\Redis
redis-server.exe
```

👉 Keep this window open while running MigCommerce.

3. Setup MySQL Database

Install MySQL (if not already installed).

Open MySQL terminal and run:
```
CREATE DATABASE migcommerce;


Update application.properties in the backend with your MySQL username & password:

spring.datasource.username=your_username
spring.datasource.password=your_password
```
4. Backend (Spring Boot)
```
cd backend
./mvnw spring-boot:run
```

Backend runs at 👉 http://localhost:8080

5. Frontend (React)
```
cd frontendClient
npm install
npm start

```
Frontend runs at 👉 http://localhost:3000

✅ Tech Stack

Backend: Spring Boot, Java

Frontend: React, JavaScript

Database: MySQL

Cache/Session Store: Redis

⚡ Common Issues

Redis Error (Unable to connect to Redis)
→ Make sure Redis is running (redis-cli ping should return PONG).

MySQL Authentication Error
→ Check username/password in application.properties.

Port Conflicts
→ If port 8080 or 3000 is already in use, change it in configs.