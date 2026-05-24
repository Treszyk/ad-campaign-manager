# Ad Campaign Manager

A full-stack application for managing local product advertising campaigns. The system provides campaign CRUD operations, transactional wallet balance deductions and refunds, whitelisted keyword typeahead search, seller-level data isolation boundaries, and a dynamic visual tool to export campaign ad previews directly as images.

## Setup & Launch Instructions

### Prerequisites

- Java Development Kit (JDK) 25
- Node.js (v18+) and npm
_(Note: Maven is not required to be installed globally; the project is fully bundled with the Maven Wrapper.)_

### 1. Launch the Backend

First, copy the environment template in the project root directory:

*   **Linux / macOS (Bash / Zsh)**:
    ```bash
    cp .env.example .env
    ```
*   **Windows (Command Prompt / PowerShell)**:
    ```cmd
    copy .env.example .env
    ```

Then, navigate to the `/backend` directory and launch the server:

*   **Linux / macOS (Bash / Zsh)**:
    ```bash
    cd backend
    ./mvnw clean spring-boot:run
    ```
*   **Windows (Command Prompt / PowerShell)**:
    ```cmd
    cd backend
    mvnw.cmd clean spring-boot:run
    ```

_(Note: The application dynamically loads local environment variables from the `.env` file at startup on all platforms.)_

- The Spring REST API starts on `http://localhost:8080`
- Initial database entities for Alice and Bob are automatically seeded on startup.

### 2. Launch the Frontend

Navigate to the `/frontend` directory and execute:

```bash
npm install
npm start
```

- The application launches on `http://localhost:4200` with hot-reloading active.

---

## Technical Stack

### Backend

- **Language**: Java 25
- **Framework**: Spring Boot 4.0.6 (Web, JPA, validation layers)
- **Database**: H2 In-Memory Database (automatic schema generation and profile data seeding on startup)
- **Libraries**: Lombok, MapStruct 1.6.3, Jackson

### Frontend

- **Framework**: Angular 21 (Standalone Component Architecture)
- **State Management**: Angular Signals
- **Styling**: Vanilla CSS (CSS variables, flex and grid layouts)
- **Client Libraries**: html2canvas 1.4.1 (for ad preview image captures)
- **Client Testing**: Vitest 4.0.8

---

## Architectural Decisions

### 1. Domain-Driven Design (DDD)

The backend decouples business rules from persistence logic:

- **Domain Model (`com.treszyk.campaigns.domain.model`)**: Contains pure Java domain classes (`Campaign`, `EmeraldAccount`, `Product`) verifying internal validations (such as minimum bids, positive balances, and valid radiuses). These classes are entirely clean of database or persistence annotations.
- **Domain Service (`com.treszyk.campaigns.domain.service.CampaignDomainService`)**: Encapsulates domain logic including town/keyword whitelisting, account-seller verification, and campaign isolation constraints.
- **Persistence Isolation**: Separate JPA entities (`CampaignJpaEntity` etc. under `com.treszyk.campaigns.infrastructure.entity`) map database structures, with bidirectional mapping to domain models handled via MapStruct, and bridged to domain repositories using Spring Data adapters.

### 2. Wallet Fund Management

Campaign fund changes are directly linked to the seller's wallet balance:

- **Create**: Deducts the campaign fund from the selected wallet.
- **Edit**: Deducts or refunds the difference between the old and new budget.
- **Delete**: Refunds the full campaign fund back to the wallet.
- **Frontend Sync**: The wallet balance updates immediately on screen upon saving changes.

### 3. Reactive State Isolation

- Sellers switch profiles inside the frontend header context.
- An Angular `effect()` inside `CampaignService` monitors profile changes and updates the active products and campaigns collection to maintain seller-level data boundaries.

### 4. Global Interceptors & Error Propagation

- An Angular `httpErrorInterceptor` intercepts REST anomalies (such as connection failures, validation exceptions, or server offline states), parses Spring's structured `GlobalExceptionHandler` bodies, and routes them to `ToastService`.
- Notifications display in a bottom-centered overlay that stacks using `flex-direction: column-reverse`.

### 5. Scope-Control Omissions

- User login sessions, credential management, seller configuration management, and product catalog inventory CRUD workflows are consciously omitted to focus strictly on core campaign management without inflating the project scope.

---

## Key Assumptions

- **Internal ERP/CRM System**: It is assumed that the application functions as an internal business tool (such as an ERP or CRM panel) rather than a public-facing multi-tenant SaaS platform. Consequently, the API is configured to return descriptive, exact entity-level validation and lookup errors to optimize debugging and administrative workflow speed, bypassing generic 404 or 403 responses that would otherwise be required to prevent resource and user enumeration vulnerabilities in public-facing multi-tenant architectures.
- **Development-Only Database Configuration**: The datasource credentials inside `application.properties` are hardcoded to standard H2 dev defaults (such as a blank password for user `sa`) and run completely in-memory. This is a conscious choice to facilitate immediate local execution and setup. In a production environment, external secrets are managed through environment variables (e.g., `${DB_PASSWORD}`) and activated via isolated Spring Profiles.

---

## REST API Endpoints

### Campaign Administration

- `POST /api/campaigns` — Creates a new campaign (deducts funds from wallet).
- `GET /api/campaigns` — Lists all campaigns in the system.
- `GET /api/campaigns/seller/{sellerId}` — Lists isolated campaigns for a specific seller.
- `GET /api/campaigns/{id}` — Fetches details for a single campaign.
- `PUT /api/campaigns/{id}` — Modifies an existing campaign (handles budget delta recalculations).
- `DELETE /api/campaigns/{id}` — Deletes a campaign (refunds budget to wallet).

### Seller & Inventory Metadata

- `GET /api/sellers` — Lists all active sellers (e.g. Alice, Bob).
- `GET /api/sellers/{id}` — Fetches details for a single seller.
- `GET /api/sellers/{id}/accounts` — Returns emerald wallets for a seller.
- `GET /api/sellers/{id}/products` — Returns catalog items for a seller.

### Whitelist Metadata

- `GET /api/metadata/themes` — Whitelisted pastel color preview options.
- `GET /api/metadata/keywords` — Approved tags for typeahead filtering.
- `GET /api/metadata/towns` — Whitelisted target locations.
