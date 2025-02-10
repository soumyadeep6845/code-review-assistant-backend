# AI-Based Code Review Assistant

## 🚀 Project Overview
The **AI-Based Code Review Assistant** is a full-stack application that helps developers review their code using an AI-powered model. The backend is built with **Java 17, Spring Boot, and MySQL**, while the frontend uses **React**. The AI engine is integrated via **OpenAI API** to analyze submitted code snippets and provide feedback.

## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot, Spring Data JPA, MySQL
- **AI Integration**: OpenAI API
- **Frontend**: React
- **Build Tool**: Gradle 8.6
- **Containerization**: Docker, Kubernetes
- **Testing**: JUnit 5, Mockito
- **Deployment**: AWS Elastic Kubernetes Service (EKS)

## ⚙️ Backend Setup

### Prerequisites
Ensure you have the following installed:
- **Java 17**
- **Gradle 8.6**
- **MySQL** (or use Docker for MySQL)
- **Docker & Kubernetes (optional for deployment)**

### 🔧 Installation & Running the Backend
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/ai-code-review.git
   cd ai-code-review/backend
   ```

2. Configure MySQL Database (Update `application.yml`):
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/codereviewdb
       username: root
       password: password
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
   ```

3. Run the backend service:
   ```sh
   ./gradlew bootRun
   ```

4. API Documentation (Swagger UI):
   - Open: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🐳 Docker & Kubernetes Setup

### 🔹 Docker Build & Run
1. Build Docker Image:
   ```sh
   docker build -t ai-code-review .
   ```
2. Run Container:
   ```sh
   docker run -p 8080:8080 ai-code-review
   ```

### 🔹 Kubernetes Deployment
1. Apply K8s Deployment:
   ```sh
   kubectl apply -f kubernetes/deployment.yaml
   ```

---

## 🔥 API Endpoints

| Method | Endpoint                        | Description               |
|--------|---------------------------------|---------------------------|
| POST   | `/api/review`                   | Submit code for review   |
| GET    | `/api/review/{id}`               | Get review result       |
| GET    | `/api/health`                    | Check server status     |

---

## 🧪 Running Tests
To execute unit tests using JUnit & Mockito:
```sh
./gradlew test
```

---

### 📌 Contributions
If you'd like to contribute, please fork the repository and then raise a **pull request** with necessary changes. Thank you.

### 💚 Found this project interesting?
If you found this project useful, then please consider leaving a ⭐ on Github. Thank you! 😄

---

💡 _Project maintained by [Soumyadeep Das](https://www.linkedin.com/in/soumya0021/)_

