# Code Review Assistant

## 🚀 Overview
This is the **backend** of the Code Review Assistant, a powerful AI-driven system designed to analyze code snippets and provide intelligent feedback. The backend handles API requests, communicates with OpenAI's GPT models, and stores submission data securely.

## 🛠 Tech Stack
- **Language:** Java (v17)
- **Framework:** Spring Boot
- **Database:** MySQL
- **Build Tool:** Gradle
- **Containerization:** Docker
- **Orchestration:** Kubernetes

## 📦 Installation & Setup
### Prerequisites
- Java 17
- MySQL (or Docker for database)
- Gradle

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/your-username/code-review-assistant-backend.git
cd code-review-assistant-backend
```

### 2️⃣ Set Up Environment Variables
Create a `.env` file and configure:
```env
DB_URL=jdbc:mysql://localhost:3306/code_review
DB_USERNAME=root
DB_PASSWORD=yourpassword
OPENAI_API_KEY=your-api-key
```

### 3️⃣ Run Database (Using Docker)
```sh
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=code_review -p 3306:3306 -d mysql:latest
```

### 4️⃣ Build and Run the Backend
```sh
gradle build
java -jar build/libs/code-review-assistant-backend.jar
```
The backend should now be running on `http://localhost:8080`.

## 📌 Features
✅ AI-based code analysis using GPT-4o\
✅ RESTful API for frontend communication\
✅ Secure authentication & authorization\
✅ MySQL database integration\
✅ Scalable with Docker & Kubernetes

## 📂 Folder Structure
```
code-review-assistant-backend/
│-- src/
│   ├── main/
│   │   ├── java/com/example/personal/
│   │   │   ├── config/         # Configurations
│   │   │   ├── controllers/    # API Controllers
│   │   │   ├── models/         # Data Models
│   │   │   ├── repositories/   # Database Repositories
│   │   │   ├── services/       # Business Logic Services
│   │   ├── resources/
│   │       ├── application.properties  # Application Configuration
│   ├── test/         # Unit & Integration Tests
│-- build.gradle      # Gradle Build Configuration
│-- Dockerfile        # Docker Configuration
│-- deployment.yaml   # Kubernetes Configuration
```

## 📜 API Endpoints
| Method | Endpoint               | Description                  |
|--------|------------------------|------------------------------|
| POST   | `/api/review`         | Submits code for review      |
| GET    | `/api/submissions`    | Retrieves user submissions   |
| GET    | `/api/submissions/{id}` | Gets a specific submission  |

## 📌 Deployment
### Build Docker Image
```sh
docker build -t code-review-backend .
docker run -p 8080:8080 code-review-backend
```

### Kubernetes Deployment
Apply the Kubernetes configuration:
```sh
kubectl apply -f deployment.yaml
```

## 🎯 Contribution
If you'd like to contribute, feel free to **fork** the repository and raise a PR with necessary changes.

## 💚 Found this project interesting?
If you found this project useful, then please consider leaving a :star: on Github. Thank you! 😄

## 👨 Project Maintained By
[Soumyadeep Das](https://www.linkedin.com/in/soumya0021/)

---
🚀 Happy Coding! 🎉
