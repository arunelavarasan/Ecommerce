# User Service — Layered Refactor

This refactor reorganizes the existing `user-service` into a conventional Spring Boot layered architecture while retaining its current service boundary and endpoint paths.

## Architecture

```text
controller
   |
   v
service (interfaces)
   |
   v
service.impl (business logic)
   |
   v
repository (Spring Data JPA)
   |
   v
MySQL

DTO request / DTO response isolate the API contract from JPA entities.
entity contains database-mapped models.
config retains the existing startup data initializer.
exception contains API exception handling.
```

## Files and existing functionality retained

- `UserServiceApplication`
- `User` and `CartFavorite` JPA entities and their existing table mappings
- `UserRepository` and `CartFavoriteRepository` finder methods
- `UserDataInitializer`, including the original sample users, delivery partners, cart items, and favorites
- `application.properties` (port 8085, service name, Eureka URL, MySQL URL and JPA settings)
- `pom.xml`, Maven wrapper files, `.gitignore`, and existing IDE project metadata
- `TestCartController` and its flat `/test-fetch-items` route
- All existing `UserController` routes and HTTP methods

Generated `target/` build outputs and macOS `__MACOSX` metadata were excluded so the ZIP contains a clean source project rather than stale compiled classes.

## Endpoints retained

| Method | Path | Purpose |
|---|---|---|
| GET | `/users/cart-favorites-list/{userId}/{itemType}` | Get cart/favorite items (path variable form) |
| GET, POST | `/users/get-items?userId=&itemType=` | Legacy fallback item lookup |
| POST | `/users/register` | Register a user |
| GET | `/users/all` | List users |
| PUT | `/users/{id}/block?reason=` | Block a user |
| PUT | `/users/{id}/unblock` | Unblock a user |
| POST | `/users/login?email=&password=` | Login |
| POST | `/users/sync-item?userId=&productId=&quantity=&itemType=` | Add/update a cart or favorite item |
| DELETE | `/users/remove-item?userId=&productId=&itemType=` | Remove a cart/favorite item |
| GET | `/users/{id}` | Get a user profile |
| PUT | `/users/reset-password?email=&newPassword=` | Reset password |
| GET | `/test-fetch-items?userId=&itemType=` | Legacy test item lookup, no `/users` prefix |

## Important compatibility/security note

The original controllers serialized JPA `User` entities directly, including the password field. The refactored controllers return `UserResponse`, which deliberately does **not** expose passwords. This is an intentional security correction; verify that no frontend relies on receiving a password field in API responses. Request formats and route paths are retained, including query/form parameters for login, item sync, and password reset.

The existing seed data and plain-text password behavior are preserved to avoid silently changing the current login implementation. This is not production-safe: add password hashing (e.g. BCrypt), authentication/authorization for administrative endpoints, validation, and secret management as a separate reviewed change. The current database password remains in the original properties file because the configuration was intentionally preserved; move it to an environment variable before deployment.

## Local verification

1. Extract `user-service/`.
2. Ensure JDK 21 and a reachable Maven installation are available.
3. Ensure MySQL database `micro_user_db` exists and the credentials in `application.properties` are valid.
4. Ensure Eureka is available at `http://localhost:8761/eureka/`, or adjust the existing configuration for your local environment.
5. Run `./mvnw test` (or `mvn test`).
6. Start the application and exercise the endpoint table above against a disposable/test database.

**Build verification limitation:** I attempted `./mvnw test`, but the wrapper could not download Maven from Maven Central in this environment. Therefore, the refactor has not been compiled or integration-tested here. Run the build locally before replacing your existing service.
