# Product Service — Layered Refactor

## Refactor goal

This refactor organizes the existing Product Service into Controller → Service interface → Service implementation → Repository, with request/response DTOs. It keeps the existing Spring Boot application identity, port, Eureka settings, database configuration, JPA entity, repository methods, product seed catalog, and API routes.

## Request flow

`ProductController` handles HTTP and delegates to `ProductService`. `ProductServiceImpl` owns application/business logic and calls `ProductRepository` for persistence. `CreateProductRequest` is the API input model; `ProductResponse` and `StockDeductionResponse` are API output models. The `Product` entity remains the database model and is not returned directly by controllers.

## Package layout

```text
com.example.product_service
├── ProductServiceApplication.java
├── controller
│   └── ProductController.java
├── service
│   ├── ProductService.java
│   └── impl
│       └── ProductServiceImpl.java
├── repository
│   └── ProductRepository.java
├── entity
│   └── Product.java
├── dto
│   ├── request
│   │   └── CreateProductRequest.java
│   └── response
│       ├── ProductResponse.java
│       └── StockDeductionResponse.java
├── exception
│   ├── ApiExceptionHandler.java
│   ├── ProductNotFoundException.java
│   ├── InsufficientStockException.java
│   └── InvalidStockQuantityException.java
└── config
    └── ProductDataInitializer.java
```

## Existing API routes retained

| Method | Route | Purpose |
|---|---|---|
| GET | `/products` | List catalog products |
| GET | `/products/{id}` | Get a product by ID |
| POST | `/products` | Create a product |
| PUT | `/products/{id}/deduct-stock?quantity={n}` | Deduct inventory |
| PUT | `/products/{id}/restore-stock?quantity={n}` | Restore inventory |
| GET | `/products/alerts/low-stock` | List products with stock below 5 |
| DELETE | `/products/{id}` | Delete a product |

The `/products` base path, HTTP methods, path variables, query parameter `quantity`, created status (201), stock messages, delete success message, and low-stock threshold are retained. Product response fields remain `id`, `name`, `description`, `price`, `imageUrl`, `stockQuantity`, `category`, `icon`, and `isOutOfStock`.

The stock endpoints now reject zero or negative quantities with HTTP 400. The old code did not validate these inputs, which could result in incorrect stock changes; this is an intentional validation fix.

## Existing code/configuration retained

- `ProductServiceApplication` and Eureka client enablement.
- `Product` entity, field names, constructors, JPA table mapping, and stock-derived out-of-stock behavior.
- `ProductRepository` and its `findByStockQuantityLessThan` / `findByCategoryIgnoreCase` query methods.
- All 50 seeded products across the existing 10 categories, including names, descriptions, prices, image paths, quantities, categories, and icons.
- `application.properties`, `pom.xml`, Maven wrapper files, Eclipse project metadata, `.gitignore`, `.gitattributes`, and the existing test source.
- Generated `target/` output and macOS `__MACOSX` metadata are excluded because they are build artifacts/platform metadata, not source. Rebuild artifacts locally.

## Build and verify

Use JDK 21, consistent with `pom.xml`. From this directory:

```bash
./mvnw test
./mvnw clean package
```

Then start the service and test all seven endpoints above, especially stock deduction/restoration and low-stock results. Use a development/test database first.

## Before production

The existing database credentials are retained for compatibility; move credentials to environment variables or a secret manager before deployment. Inventory deduction should eventually use optimistic/pessimistic locking or an atomic database update to prevent concurrent orders from overselling stock. Consider `BigDecimal` instead of `double` for monetary amounts as a separately tested change.
