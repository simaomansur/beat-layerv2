# Beat Layer v2

Beat Layer is a full-stack web application for creating, organizing, and discovering “jams.” It uses a React + Vite + TypeScript frontend and a Spring Boot + PostgreSQL + Flyway backend.

---

## Tech Stack

### Backend
- Java 21 / Spring Boot 3
- PostgreSQL 16
- Flyway for database migrations
- HikariCP connection pooling
- Gradle (Kotlin DSL)
- REST API for `Jam` entities

### Frontend
- React 18 with Vite
- TypeScript
- Axios for API requests

### Infrastructure
- Docker Compose
- Services:
  - `api` (Spring Boot)
  - `db` (PostgreSQL)
  - `frontend` (React dev server)

---

## API Overview

| Method | Endpoint       | Description        |
|-------:|----------------|--------------------|
| GET    | `/jams`        | List all jams      |
| POST   | `/jams`        | Create a new jam   |
| GET    | `/jams/{id}`   | Get a jam by ID    |
| DELETE | `/jams/{id}`   | Delete a jam       |

### Example `POST /jams` Body

```json
{
  "title": "Lo-fi Groove",
  "key": "Am",
  "bpm": 90,
  "genre": "lofi",
  "instrumentHint": "guitar"
}
```

### Roadmap
- Authentication (JWT)
- Filtering and sorting
- File uploads for track previews
- Deployment with Docker or managed hosting

### Author
#### Simao Mansur
- GitHub: https://github.com/simaomansur
- LinkedIn: https://www.linkedin.com/in/simaomansur
