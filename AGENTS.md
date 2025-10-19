# EventMaster AGENTS.md

## Project Overview

EventMaster is a learning platform for **CQRS (Command Query Responsibility Segregation)** and **Event-Driven Architecture (EDA)** patterns. The system uses asynchronous, event-driven communication between write and read models via Kafka/Redpanda.

### Core Architecture Principles

- **CQRS Pattern**: Strict separation between Commands (write path) and Queries (read path)
- **Event-Driven Architecture**: All state changes flow through domain events published to Kafka
- **Asynchronous Processing**: Commands return `202 Accepted` immediately; actual processing happens via event handlers
- **Real Infrastructure Testing**: Zero mocks - use Testcontainers for PostgreSQL, Keycloak, and Redpanda

### Current Implementation Status

✅ **Completed (Milestone: Task 12)**
- Full write path: API → Command → Handler → Write Model → Event Publication
- Full read path: Event → Projector → Read Model (denormalization)
- Authentication & Authorization: OIDC with Keycloak
- Testing infrastructure: Testcontainers with real services

🚧 **Next Steps**
- Connect Query Controller to EventViewRepository
- Implement frontend integration with read model endpoints
- Add more business flows beyond event creation

## Technology Stack

### Backend (Spring Boot 6.2+)
- **Language**: Java 21 (strict)
- **Framework**: Spring Boot 6.2+
- **Security**: OAuth2 Resource Server (JWT validation from Keycloak)
- **Database**: PostgreSQL 16+ (single DataSource, separate tables for write/read models)
- **Event Bus**: Redpanda/Kafka
- **Migrations**: Flyway (all schema changes must go through migrations)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Testcontainers, Awaitility

### Frontend (Nuxt.js)
- **Framework**: Nuxt 3 (TypeScript strict mode)
- **Package Manager**: pnpm (always use `pnpm`, never npm/yarn)
- **UI Library**: PrimeVue
- **State Management**: Pinia
- **Authentication**: @sidebase/nuxt-auth with Keycloak OIDC
- **Validation**: Zod

### Infrastructure (docker-compose)
- **Reverse Proxy**: Caddy (port 80, handles routing and logging)
- **Identity Provider**: Keycloak (port 8180, auto-imports `eventmaster-realm.json`)
- **Event Bus**: Redpanda (port 9092, Kafka-compatible)
- **Database**: PostgreSQL (port 5432)

## Project Structure

### Backend Structure
```

backend/
├── src/main/java/com/eventmaster/
│   ├── command/           \# Command handlers, controllers (WRITE PATH)
│   │   ├── controller/    \# EventCommandController
│   │   ├── handler/       \# EventCommandHandler (@KafkaListener)
│   │   └── model/         \# CreateEventCommand (POJOs)
│   ├── query/             \# Query controllers, projectors (READ PATH)
│   │   ├── controller/    \# EventQueryController
│   │   ├── projector/     \# EventViewProjector (@KafkaListener)
│   │   └── model/         \# EventView (@Entity)
│   ├── domain/            \# Write model entities
│   │   └── Event.java     \# @Entity for write model
│   ├── event/             \# Domain events
│   │   └── EventCreatedEvent.java
│   └── config/            \# Spring configurations
└── src/test/java/         \# Integration tests with Testcontainers
└── BaseIntegrationTest.java

```

### Frontend Structure
```

frontend/
├── pages/
│   ├── index.vue          \# Home page
│   ├── login.vue          \# Login redirect
│   └── events/
│       └── create.vue     \# Event creation form (Task 7)
├── components/            \# PrimeVue components
├── stores/                \# Pinia stores
└── composables/           \# Nuxt composables

```

## Architecture Flow (CRITICAL - Always Follow)

### Write Path (Commands)
```

1. Frontend (Nuxt) → POST /api/v1/events
2. Caddy → Spring Boot EventCommandController
3. Controller creates CreateEventCommand
4. Controller publishes to Kafka topic: "commands.events.create"
5. Controller returns 202 Accepted immediately
6. EventCommandHandler (@KafkaListener) receives command
7. Handler maps to Event entity
8. Handler saves to Write Model (events table)
9. Handler publishes EventCreatedEvent to "events.lifecycle"
```

### Read Path (Queries)
```

1. EventViewProjector (@KafkaListener) listens on "events.lifecycle"
2. Projector receives EventCreatedEvent
3. Projector maps to EventView entity (denormalization)
4. Projector saves to Read Model (event_view table)
5. EventQueryController exposes GET /api/v1/events (to be completed)
6. Frontend queries read model for display
```

## Do's and Don'ts

### DO

- **Always use CQRS pattern**: Commands go through command handlers, queries go through projectors
- **Always publish domain events** after write model changes
- **Always use Testcontainers** for integration tests (PostgreSQL, Keycloak, Redpanda)
- **Always use Flyway migrations** for schema changes (never modify DB manually)
- **Always use @KafkaListener with Awaitility** for async testing
- **Always validate with Zod** on frontend forms
- **Always use pnpm** for frontend dependencies
- **Always return 202 Accepted** for async command endpoints
- **Always use @AuthenticationPrincipal** for secured endpoints
- **Always separate concerns**: Entity → Write Model, EventView → Read Model
- **Always use concrete types**: CreateEventCommand, EventCreatedEvent (not generic DTOs)

### DON'T

- **Never mix write and read models** (no direct queries from command handlers)
- **Never use H2 or EmbeddedKafka** in tests (only Testcontainers)
- **Never bypass Flyway** for schema changes
- **Never return write model entities** from query endpoints
- **Never use npm or yarn** (always pnpm for frontend)
- **Never hardcode URLs** (use environment variables and Caddy routing)
- **Never skip JWT validation** in Spring Security config
- **Never use blocking calls** in @KafkaListener methods without Awaitility in tests
- **Never expose sensitive data** in events or logs
- **Never create "god classes"** - keep handlers, projectors, and controllers focused

## Build and Test Commands

### Backend Commands

```


# Build entire backend

mvn clean install

# Run Spring Boot application

mvn spring-boot:run

# Run all tests (includes Testcontainers startup)

mvn test

# Run specific test class

mvn test -Dtest=EventCommandHandlerTest

# Run integration tests only

mvn verify

# Check dependencies for vulnerabilities (OWASP)

mvn dependency-check:check

# Format code

mvn spotless:apply

```

### Frontend Commands

```


# Install dependencies (ALWAYS use pnpm)

pnpm install

# Run dev server

pnpm dev

# Build for production

pnpm build

# Run linting

pnpm lint

# Run type checking

pnpm typecheck

# Run unit tests

pnpm test

# Format code

pnpm format

```

### Infrastructure Commands

```


# Start all services (Caddy, Keycloak, Redpanda, PostgreSQL)

docker-compose up -d

# View logs

docker-compose logs -f [service-name]

# Stop all services

docker-compose down

# Reset all data (nuclear option)

docker-compose down -v

```

## Testing Guidelines

### Backend Testing Philosophy

- **Real Infrastructure Only**: Use Testcontainers for PostgreSQL, Keycloak, Redpanda
- **No Mocking of External Services**: Tests should run against actual containers
- **Async Testing**: Use Awaitility.await() for @KafkaListener assertions
- **Topic Auto-Creation**: Use @TestConfiguration with NewTopic beans

### Critical Test Configuration

```

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)  // Don't use H2!
@TestPropertySource(properties = {
"spring.kafka.test.embedded.enabled=false"  // Don't use EmbeddedKafka!
})
class EventCommandHandlerTest extends BaseIntegrationTest {
// Use Awaitility for async assertions
Awaitility.await()
.atMost(10, TimeUnit.SECONDS)
.until(() -> eventRepository.findById(id).isPresent());
}

```

### Frontend Testing

- Use Vitest for unit tests
- Use Playwright for E2E tests (when implemented)
- Mock Keycloak auth in tests using test tokens
- Always validate forms with Zod before submission

## Code Style Guidelines

### Backend (Java/Spring Boot)

- **Java Version**: Java 21 (use modern features: records, pattern matching, switch expressions)
- **Naming Conventions**:
  - Commands: `CreateEventCommand`, `UpdateEventCommand`
  - Events: `EventCreatedEvent`, `EventUpdatedEvent`
  - Handlers: `EventCommandHandler`, `EventQueryHandler`
  - Projectors: `EventViewProjector`
  - Controllers: `EventCommandController`, `EventQueryController`
- **Package Structure**: Organize by feature, not layer (`command/`, `query/`, not `controllers/`, `services/`)
- **Entity Annotations**: Use `@Entity`, `@Table`, `@Id`, `@GeneratedValue` explicitly
- **Kafka Topics**: Use descriptive names: `commands.events.create`, `events.lifecycle`

### Frontend (TypeScript/Nuxt)

- **TypeScript Strict Mode**: Always enabled, no `any` types
- **Component Naming**: PascalCase for components, kebab-case for files
- **Composables**: Use `use` prefix (e.g., `useAuth`, `useEvents`)
- **API Calls**: Always use try-catch with proper error handling
- **Form Validation**: Define Zod schemas separately from components

## Security Considerations

### Authentication & Authorization

- **Backend**: Configured as OAuth2 Resource Server (validates JWT from Keycloak)
- **Frontend**: Uses @sidebase/nuxt-auth with OIDC flow
- **Token Flow**: Authorization Code Flow with PKCE
- **Token Storage**: HttpOnly cookies (handled by nuxt-auth)
- **API Protection**: All `/api/v1/*` endpoints require valid JWT

### Security Rules

- **Never commit secrets** to git (use `.env` files)
- **Never expose Keycloak admin credentials** in code
- **Always validate input** on both frontend (Zod) and backend (Bean Validation)
- **Always use parameterized queries** (JPA handles this)
- **Always enable CSRF protection** in Spring Security
- **Always use HTTPS in production** (Caddy handles this)

## Database Schema Management

### Flyway Migrations (Critical)

- **All schema changes MUST go through Flyway**
- **Never modify database schema manually**
- **Migration files**: `src/main/resources/db/migration/V{version}__{description}.sql`
- **Naming**: `V1__create_events_table.sql`, `V2__create_event_view_table.sql`
- **Testing**: Flyway runs automatically in tests via Testcontainers

### Current Schema

- **Write Model**: `events` table (stores Event entities)
- **Read Model**: `event_view` table (stores EventView projections)
- **Separate tables ensure CQRS isolation**

## Kafka Topics (Event Bus)

### Topic Naming Convention

- **Commands**: `commands.{aggregate}.{action}` (e.g., `commands.events.create`)
- **Events**: `events.{category}` (e.g., `events.lifecycle`)
- **Error Topics**: `errors.{source}` (future implementation)

### Topic Management

- **Production**: Topics auto-created by Redpanda
- **Tests**: Use `@TestConfiguration` with `NewTopic` beans to ensure topics exist

## Git Workflow

### Commit Message Format

```

[MODULE] Brief description

Longer explanation if needed.

Fixes: \#issue-number

```

Examples:
- `[BACKEND] Add EventViewProjector for read model denormalization`
- `[FRONTEND] Implement event creation form with Zod validation`
- `[INFRA] Configure Keycloak realm auto-import`

### PR Checklist

- ✅ All tests pass (`mvn test` and `pnpm test`)
- ✅ Code follows CQRS patterns (commands separate from queries)
- ✅ Flyway migration created for schema changes
- ✅ Integration tests added with Testcontainers
- ✅ JWT authentication tested
- ✅ No hardcoded values (use properties/env vars)
- ✅ Code formatted (spotless for Java, prettier for TS)
- ✅ Small, focused diff (avoid large refactors without discussion)

## When Stuck

- **Ask clarifying questions** about CQRS flow if unsure
- **Propose a plan** before large architectural changes
- **Reference existing code**: EventCommandHandler for write path, EventViewProjector for read path
- **Check Testcontainers logs** if integration tests fail
- **Verify Kafka topics** exist before running handlers
- **Check Keycloak realm import** if auth fails

## API Documentation

### Backend Endpoints

#### Command Endpoints (Write)
- `POST /api/v1/events` - Create event (returns 202 Accepted)
  - Handler: EventCommandController
  - Auth: Required (JWT)
  - Body: CreateEventCommand
  - Async: Publishes to Kafka, returns immediately

#### Query Endpoints (Read)
- `GET /api/v1/events` - List events (TO BE COMPLETED)
  - Handler: EventQueryController
  - Auth: Required (JWT)
  - Returns: List<EventView>
  - Source: Read model (event_view table)

### Frontend Routes

- `/` - Home page
- `/login` - Login redirect to Keycloak
- `/events/create` - Event creation form (protected route)

## Good Examples to Follow

### Backend Examples
- ✅ **Command Handler**: `EventCommandHandler` - Shows @KafkaListener, entity mapping, event publishing
- ✅ **Projector**: `EventViewProjector` - Shows denormalization pattern
- ✅ **Test**: `EventCommandHandlerTest` - Shows Testcontainers, Awaitility usage
- ❌ **Avoid**: Direct database access from controllers

### Frontend Examples
- ✅ **Form**: `/events/create.vue` - Shows Zod validation, PrimeVue components
- ✅ **Auth**: Login flow with `signIn('keycloak')`
- ❌ **Avoid**: Direct API calls without error handling

## Nested AGENTS.md (Future)

For larger projects, consider:
- `backend/AGENTS.md` - Backend-specific rules
- `frontend/AGENTS.md` - Frontend-specific rules
- `infrastructure/AGENTS.md` - Docker/deployment rules

## Learning Resources (Reference Only)

This project is for learning CQRS and EDA. Key concepts:
- **CQRS**: Command Query Responsibility Segregation
- **Event Sourcing**: Not implemented yet (events are intermediate, not the source of truth)
- **Eventual Consistency**: Read model updated asynchronously after write model
- **Saga Pattern**: Not implemented yet (future for distributed transactions)

## Notes for AI Agents

- **Think CQRS First**: Before writing code, identify if it's command (write) or query (read)
- **Respect Async Boundaries**: Commands return 202, actual processing happens in handlers
- **Test with Real Services**: Never suggest H2 or EmbeddedKafka
- **Follow Existing Patterns**: Mirror EventCommandHandler for new commands, EventViewProjector for new projections
- **Incremental Changes**: Small PRs focused on one business flow at a time
```