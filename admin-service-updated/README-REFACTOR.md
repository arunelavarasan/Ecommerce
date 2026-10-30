# Admin Service — layered refactor

This refactor separates HTTP handling, business logic, persistence, and API DTOs while retaining the existing `/admin/...` paths and JSON response field names.

## Structure

- `controller/`: REST endpoints only; delegates to service interfaces.
- `service/`: business service interfaces.
- `service/impl/`: business logic and entity-to-response mapping.
- `repository/`: Spring Data JPA persistence.
- `entity/`: JPA database models.
- `dto/request/`: incoming API request models.
- `dto/response/`: API response models.
- `config/`: application configuration and demo data initializer.

## Endpoints

- `POST /admin/logs` — parameters: `username`, optional `module`, `action`, optional `ipAddress`
- `GET /admin/logs`
- `POST /admin/refunds/request` — parameters: `orderId`, `userId`, `amount`, `paymentMethod`, `reason`
- `GET /admin/refunds`
- `PUT /admin/refunds/{id}/approve`
- `GET /admin/metrics`

The create endpoints use `@ModelAttribute` so existing query/form-parameter clients continue to work. If the frontend is sending JSON, switch those methods to `@RequestBody` and update the client payloads accordingly.

## Build

Requires Java 21 and Maven (or the included Maven wrapper):

```bash
./mvnw clean test
```

Configure database credentials through environment variables rather than committing secrets. The current `application.properties` was retained to minimize changes, but its hard-coded password should be moved to environment variables before sharing or deploying.

## Microservices architecture note

This change makes the code modular and layered *inside one deployable service*. It does not split it into multiple independently deployable microservices. Keep Admin Service as its own bounded service; only extract audit logging or refunds into separate services if they need independent ownership, deployment, scaling, and data boundaries. In a distributed design, each service should own its data and communicate through APIs/events rather than directly accessing another service's tables.
