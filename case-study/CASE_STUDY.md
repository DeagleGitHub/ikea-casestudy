# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
The optimal solution depends on the current data ingestion strategy for expense logging. If we are processing the absolute total of transactions, the sheer volume of high-frequency data could create a bottleneck, making real-time synchronization and accessibility unsustainable; in this scenario, implementing data sampling or approximate aggregation would be necessary. Conversely, if the data throughput is manageable and ingestion is not a bottleneck, we could provide full-fidelity visibility of actual expenditures. However, for real-time monitoring and operational decision-making, high precision is often secondary to low latency, making the first approach (approximate metrics) more efficient for live dashboards.

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
While I am not specifically familiar with dedicated financial management platforms, I would apply the same principles used in optimizing I/O operations: implementing a batch processing strategy for similar expenses. By aggregating individual transactions into larger, logical volumes rather than processing each expense atomically, we can streamline management workflows and achieve significant economies of scale. This approach reduces system overhead and minimizes the operational costs associated with high-frequency data handling.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
As discussed in the first question, financial decision-making relies more heavily on trend analysis than on discrete, granular data points. Providing exact, real-time data for every transaction can be computationally expensive and often unnecessary for high-level oversight; for instance, tracking the specific sale of a single product every second offers diminishing returns. It is more efficient to utilize aggregated data, which simplifies system ingestion and provides a clearer visualization of market trends and operational performance.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
The first priority would be to implement a dashboard featuring comprehensive sales and expense metrics per SKU to track their evolution over time. Through data visualization, we can identify which products are trending upward, maintaining stability, or facing a decline in demand. Building on this foundation, we could then develop predictive models that utilize this historical data to automatically generate performance forecasts for each product's lifecycle

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Maintaining a comprehensive historical dataset of product performance is critical for generating accurate predictive analytics and anticipating future trends. Even if a specific warehouse is decommissioned, its historical operational data serves as a benchmark (or baseline), allowing us to perform a gap analysis to determine if the new facility is meeting expected performance standards and KPIs.

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
