# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. 
If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
Inconsistent Database Access Strategy
Current Situation
    Warehouses → Repository + Domain Mapping
    WarehouseRepository implements WarehouseStore
    Uses DbWarehouse as persistence model
    Maps to/from Warehouse (domain model)

Stores → Active Record (Panache Entity)
    Store.listAll()
    Store.findById()
    store.persistAndFlush()

So the codebase uses two different architectural styles.

Refactor Recommendation: Clean Architecture style
   Use Repository + Domain Model everywhere. Why?
   Better separation of concerns
   Easier testing (mock repository)
   Business logic lives in services, not entities
   Cleaner hexagonal architecture

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. 
What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
1️⃣ OpenAPI-First (Contract-First) Approach (Your warehouse-openapi.yaml → generated code → WarehouseResource implementation)

✅ Pros
    1. Strong API Contract Governance

    The API is defined independently of implementation.

    This is huge when:

        Multiple teams consume the API
        Frontend depends on strict contracts
        External clients exist
        You version APIs
        The YAML becomes a single source of truth.

    2. Consumer-Driven Development Friendly
        Frontend and backend can work in parallel.
        Mocks can be generated.
        SDKs can be generated automatically.
        This scales extremely well in enterprise environments.

    3. Prevents Accidental API Drift
        With code-first, someone may:
        Rename a field
        Change a response
        Modify a status code
        Without realizing they broke clients.
        Contract-first prevents this.
    4. Built-in Validation & Documentation
        You get:OpenAPI docs, Swagger UI, Typed models, Validation annotations, All consistent.
    5. Better for Public APIs
        If this were:
            A B2B API
            A platform API
            Externally exposed service

❌ Cons
    1. Boilerplate & Friction
        You modify YAML
        Regenerate code
        Adapt implementation
        Handle mapping between generated models and domain
        Slower for quick changes.
    2. Generated Model Leakage
    3. Less Flexible During Early Development


2️⃣ Code-First Approach (Product & Store) (JAX-RS directly coded in StoreResource)

✅ Pros
    1. Faster Iteration
        Just write the endpoint.
        Very quick during prototyping.
    2. Less Indirection
        No generation step.
        No model duplication.
`   3. Simpler Mental Model
        What you see is what you get.

❌ Cons
    1. Higher Risk of Breaking Clients
        No strict contract enforcement. Changes may go unnoticed.
    2. Documentation May Drift
        Even with annotations, documentation can lag.
    3. Harder Client SDK Generation


Recommendation for This Codebase
    Based on what I see (simple CRUD-style services, no visible complex domain boundaries):
    Unless this is a public API…

👉 I would standardize on code-first + automatic OpenAPI generation
    Then: Remove manual YAML, Use annotations, Let Quarkus generate OpenAPI, Keep API close to implementation.That reduces friction and complexity.


```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on,
and how would you ensure test coverage remains effective over time?

**Answer:**
```txt

What I Would Prioritize
1.API Integration Tests (Top Priority)
2️.PATCH / Update Edge Cases, Full stack tests.
3️.Repository Query Tests (Selective)
4. In CI: Run integration tests on every PR
       Fail build on broken tests
       Optionally enforce minimum coverage threshold
5. Refactor Tests When Refactoring Code

### Focus on tests

1. Integration tests for all endpoints
2. Strong tests around PATCH/update logic
3. Filtering/archiving behavior tests
4. Basic contract validation for Warehouse
5. Add regression test for every bug found

That gives strong, sustainable coverage without overengineering.


### Strategy for This Codebase
1. Standardize on integration tests for all resources.
2. Add targeted tests for: Partial updates, Filtering logic, Archive behavior.
3. Avoid over-engineered mocking.
4. Add regression test for every bug.

Keep tests fast and DB-backed.

```