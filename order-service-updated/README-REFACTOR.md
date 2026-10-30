# Order Service — Layered Refactor

## Refactoring goals
The original order-service source has been organized into the requested layered architecture:

`Controller -> Service interface -> Service implementation -> Repository -> Entity`

Request and response DTOs isolate HTTP contracts from JPA entities. Existing client interfaces, DTOs, entity fields, seed data, application properties, Maven dependencies, and tests are retained.

## Package structure

- `controller/OrderController.java` — HTTP routing and request binding only.
- `service/OrderService.java` — use-case contract.
- `service/impl/OrderServiceImpl.java` — checkout, cancellation, status, delivery-attempt and lookup logic.
- `repository/OrderRepository.java` — database access.
- `dto/request/CheckoutRequest.java` — checkout input model.
- `dto/request/CancelOrderRequest.java` — cancellation input model.
- `dto/response/OrderResponse.java` — API representation of an order.
- `entity/Order.java` — existing JPA entity; its database columns and fields are retained.
- `client/` — existing OpenFeign clients for Admin, Distribution, Product and User services.
- `config/OrderDataInitializer.java` — original sample-order initialization is retained.
- `dto/ProductDTO.java`, `dto/UserDTO.java` — existing inter-service DTOs retained.

## Existing endpoints retained

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/orders/checkout` | Place order |
| POST | `/orders/{orderId}/cancel` | Cancel order with reason |
| PUT | `/orders/{orderId}/status?status=...` | Update status and optionally courier partner |
| GET | `/orders/user/{userId}` | Find orders for a user |
| GET | `/orders` | List orders |
| PUT | `/orders/{orderId}/attempt-failed?reason=...` | Record failed delivery attempt and apply RTO rule |
| GET | `/orders/partner/{partnerName}` | Find orders assigned to a courier partner |

Checkout still accepts the existing query/form parameters. Cancellation still accepts a JSON body such as `{"reason":"Customer requested cancellation"}`. The order response DTO keeps the same field names as the entity's JSON representation, including `deliveryOtp`, for compatibility.

## Important implementation details

- Corrected `OrderRepository` to use `Long` as the JPA ID type because `Order.id` is a `Long`; lookup by business order number remains `findByOrderId(String)`.
- Existing Feign client interfaces and service names were not renamed.
- Existing order initialization data was retained.
- Optional Feign client injection and the original best-effort remote calls remain for compatibility.
- `OrderDataInitializer`, `application.properties`, `pom.xml`, wrapper files, and the original test source are retained.
- Generated `target/` output and macOS `__MACOSX` metadata are excluded from the deliverable; they are build artifacts rather than source code.

## Verification status

The Maven wrapper could not download Maven 3.9.16 in this environment, so the project has **not** been successfully compiled or integration-tested here. Run the following locally from this directory:

```bash
./mvnw test
./mvnw clean package
```

Then run the service against a test database and verify all seven endpoints, Eureka registration, and the Product/Distribution/Admin/User Feign calls.

## Existing behaviors not silently redesigned

This is a structural refactor, not a redesign of order/payment business rules. Product price fallback, best-effort stock deduction and remote calls, existing order statuses, delivery attempt/RTO behavior, and the sample data are retained. Review these behaviors before production: swallowed remote-call failures can result in inconsistent stock/shipment state, stock should be compensated if checkout fails, order cancellation currently does not restore stock or invoke the existing refund client, and delivery OTP exposure should be reviewed as a security concern.
