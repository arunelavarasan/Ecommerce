# Distribution Service — Layered Refactor

This project refactors the original `distribution-service` into controller → service interface → service implementation → repository, with request and response DTOs.

## Architecture

```text
controller/DistributionController
          |
          v
service/DistributionService
          |
          v
service/impl/DistributionServiceImpl
          |
          v
repository/DistributionRepository
          |
          v
entity/Distribution -> MySQL
```

- `dto/request`: request models for dispatch and status updates.
- `dto/response`: response models that retain the original distribution field names.
- `exception`: centralized 400/404/500 response handling.
- `config/DistributionDataInitializer`: retains all 15 original sample shipment records.
- `repository/DistributionRepository`: retains the existing Spring Data query methods.
- Eureka discovery remains enabled.

## Existing API routes retained

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/distribution/dispatch?orderId=ORD-1001&userId=2` | Create shipment |
| GET | `/distribution/track/{orderId}` | Find tracking details |
| PUT | `/distribution/update/{orderId}?status=DISPATCHED&courierPartner=BlueDart` | Update stage and optionally courier |
| GET | `/distribution/filter?city=Chennai` | Filter by city |
| GET | `/distribution/filter?pincode=600002` | Filter by pincode |
| GET | `/distribution` | List shipments |
| GET | `/distribution/partner/{partnerName}` | Find shipments for a partner (case-insensitive substring) |
| PUT | `/distribution/{orderId}/status?status=DELIVERED` | Update stage from delivery portal |

The original request-parameter style, route paths, HTTP methods, shipment fields, tracking number format, default dispatch values, timestamp behavior, and CORS configuration are retained. Response payloads use DTOs with the same shipment field names; status-update endpoints retain their message/order/status/courier fields.

## Configuration

Set environment variables before running:

```bash
export DB_URL='jdbc:mysql://localhost:3306/micro_distribution_db'
export DB_USERNAME='root'
export DB_PASSWORD='your-local-database-password'
export EUREKA_SERVER_URL='http://localhost:8761/eureka/'
export SERVER_PORT=8085
```

`DB_PASSWORD` is intentionally not hard-coded in the refactored project. `spring.jpa.hibernate.ddl-auto` defaults to `update` to preserve the original local development behavior; use a migration tool and a safer schema strategy in production.

## Build and run

Use JDK 21 (the original `pom.xml` target) and run from this project directory:

```bash
./mvnw test
./mvnw clean package
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead.

## Scope and verification

The refactor preserves all original application source and test source files while moving controller business/database work into the service layer. The original entity, repository query methods, initializer with 15 sample shipments, Maven dependencies, Maven wrapper, project metadata, and Eureka support are retained. Generated `target/` output and macOS `__MACOSX`/AppleDouble metadata are excluded because they are build artifacts or OS metadata, not source code.

The code was not compiled or integration-tested in this environment. Run the commands above and test against a non-production database before deploying.

## Existing behavior and production considerations

- The original status model is a string and remains a string to avoid a schema/API change.
- The service retains the original timestamp behavior: `DISPATCHED` sets `dispatchedAt`; `DELIVERED` sets `deliveredAt`.
- Duplicate order IDs are rejected on dispatch to avoid ambiguous tracking lookup (`findByOrderId` returns an `Optional`).
- Partner matching still uses a case-insensitive substring match.
- Status transition rules, authentication/authorization, audit history, and transactional coordination with order/payment services were not part of the original service and are not invented in this refactor.
