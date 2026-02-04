# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would definitely perform a refactor to improve maintainability. Currently, there is no clear separation of concerns between the infrastructure layer and the domain layer within the "products" package. I would reorganize the codebase to ensure that distinct classes are used for data persistence (entities) and domain logic (models), adhering to a more modular architecture.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
The API-First approach used in the "warehouses" package requires more initial configuration, but it ensures that the exported documentation is directly synchronized with the actual API implementation. This mitigates errors and significantly improves documentation quality.

Conversely, the Code-First approach—where tools generate documentation via annotations (like Swagger/OpenAPI annotations)—can feel more agile and straightforward during initial development. However, one cannot overlook the overhead of maintaining those annotations and the risk of omitting critical information. From my perspective, an API-First strategy is preferable as it facilitates long-term maintainability and consistency.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
A balanced testing strategy is crucial for ensuring both immediate and long-term project health. While Integration Tests provide excellent value by validating complete use cases and interactions between components, they should not replace Unit Tests. A pragmatic approach involves using Integration Tests to cover the primary success paths and critical error scenarios of the API. Concurrently, Unit Tests should be focused on complex business logic, algorithms, and edge cases within services and domain models. This combination ensures broad coverage and allows for rapid, isolated testing of intricate logic, which significantly speeds up debugging and improves code quality and maintainability over the project's lifecycle.
```