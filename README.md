# FluxCompute

**An AI-powered distributed computing and resource-sharing platform.**

FluxCompute is a platform designed to connect users who need computational resources with providers who are willing to share their available computing power.

Users can submit computational workloads, while provider machines execute those workloads in isolated Docker containers through the FluxCompute Resource Agent.

The long-term vision is to build a distributed computing ecosystem that makes CPU, memory, storage, and GPU resources accessible through a unified platform.

---

## Overview

Computationally intensive workloads, particularly machine learning and data processing tasks, often require significant hardware resources. However, many machines have unused computational capacity.

FluxCompute aims to bridge this gap by allowing resource providers to contribute their available computing resources and enabling users to submit workloads for distributed execution.

### How It Works

1. A user registers and submits a computational workload.
2. The backend stores the workload and maintains its metadata.
3. The scheduler identifies a suitable resource provider.
4. The backend sends an execution request to the provider's Resource Agent.
5. The agent downloads and executes the workload inside a Docker container.
6. Execution results, logs, and generated artifacts are sent back to the backend.
7. The platform stores the results for the user.

---

## Architecture

```text
                  FLUXCOMPUTE PLATFORM
                  ====================

                      React Frontend
                            |
                            | REST API
                            |
                    Spring Boot Backend
                            |
              +-------------+-------------+
              |             |             |
          Auth/User     Workloads      Scheduler
              |             |             |
              +-------------+-------------+
                            |
                       WebSocket
                            |
                  Resource Agent
                            |
                     Docker Sandbox
                            |
                  Workload Execution
                            |
                 Results & Artifacts
                            |
                      REST Upload
                            |
                    Spring Boot Backend
                            |
                       MySQL DB
```

### Main Components

| Component | Responsibility |
|---|---|
| React Frontend | User interface for submitting and managing workloads |
| Spring Boot Backend | Authentication, workload management, scheduling, and job coordination |
| MySQL | Stores application data and workload metadata |
| Resource Agent | Connects provider machines to the platform and executes assigned jobs |
| Docker | Provides containerized workload execution |

---

## Technology Stack

**Backend**
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring WebSocket
- Spring Data JPA
- REST APIs

**Database**
- MySQL

**Frontend**
- React
- JavaScript
- HTML
- CSS

**Infrastructure**
- Docker
- Docker Engine
- Containerized workload execution

---

## Core Features

### Authentication and Authorization
- User authentication using JWT.
- Protected backend endpoints.
- User-specific workload management.

### Workload Management
- Upload computational workloads.
- Store workload files on the backend.
- Maintain workload metadata and execution status.
- Download workloads for execution by resource agents.

### Resource Coordination
- Communication between the backend and provider agents.
- WebSocket-based job notifications.
- Workload assignment and execution coordination.

### Result Management
- Receive execution status and logs.
- Accept generated artifact ZIP files.
- Store job artifacts on the backend.

---

## Project Structure

The backend project follows a layered Spring Boot architecture.

```text
fluxcompute/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/fluxcompute/backend/
│   │   │       ├── controllers/
│   │   │       ├── services/
│   │   │       ├── repositories/
│   │   │       ├── models/
│   │   │       ├── config/
│   │   │       └── ...
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── storage/
├── pom.xml
└── README.md
```

*The package structure above illustrates the intended organization; adjust it to match the actual repository.*

---

## Getting Started

### Prerequisites

- Java version compatible with the project's Maven configuration.
- Maven (or the included Maven Wrapper).
- MySQL Server.
- Docker Engine, if running the complete local execution workflow.
- Git.

### 1. Clone the Repository

```bash
git clone <FLUXCOMPUTE_REPOSITORY_URL>
cd fluxcompute
```

### 2. Configure MySQL

Create a database:

```sql
CREATE DATABASE fluxcompute;
```

Configure the database connection in `src/main/resources/application.properties`.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fluxcompute
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Configure the remaining JPA and application properties according to your project configuration.

### 3. Run the Backend

On Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The backend should start on the configured server port.

---

## Agent Communication

FluxCompute uses WebSocket communication to coordinate execution with connected Resource Agents.

The agent receives job execution instructions and downloads workload files through backend REST endpoints.

After execution, the agent sends the result back to the backend using a multipart HTTP request.

### Result Upload

The agent uploads execution information to:

```http
POST /api/jobs/{jobId}/result
Content-Type: multipart/form-data
```

Expected multipart fields:

| Field | Description |
|---|---|
| status | COMPLETED or FAILED |
| exitCode | Container execution exit code |
| logs | Workload execution logs |
| artifacts | ZIP file containing generated artifacts |

The backend stores the received artifact ZIP under the job-specific result directory.

---

## Security Considerations

- Protect backend APIs using authentication and authorization.
- Authenticate Resource Agents before accepting execution commands or results.
- Verify that an agent is authorized to execute or report on a particular job.
- Validate uploaded workload files.
- Avoid exposing Docker socket access to untrusted workloads.
- Use stronger isolation mechanisms when executing untrusted user code.
