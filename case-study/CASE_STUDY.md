# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
A key challenge is balancing the need for real-time operational insights with the strict requirements of financial accounting. For live operational dashboards, low-latency, aggregated data can be effective for monitoring trends and making quick decisions. However, for financial tracking, cost allocation, and auditing, this approach is insufficient. Financial teams require exact, traceable transaction logs for every expense to ensure compliance and accuracy. Therefore, the system must support a dual-path data strategy: one path for high-frequency, near real-time aggregated data for operational views, and a separate, robust path that captures and persists every single transaction with full fidelity for financial systems and auditing purposes. This ensures both operational agility and financial integrity.

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
To identify cost optimization opportunities, we must analyze the granular, transactional cost data we are collecting. Strategies could include optimizing inventory levels based on demand forecasts or reducing transportation costs by consolidating shipments. For data processing, we can apply principles from I/O optimization, such as batch processing for loading data into analytics systems. This improves efficiency without sacrificing the integrity of individual transaction records. Each cost-saving initiative should be prioritized based on its potential impact and implementation complexity, using the detailed cost data to model expected outcomes and track actual savings post-implementation.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Integrating the Cost Control Tool with financial systems is critical for maintaining a single source of truth for all cost-related data. This integration ensures that financial reporting is accurate, timely, and fully auditable. While aggregated data is useful for high-level trend analysis, financial systems require a complete and granular record of every transaction. For accounting and auditing, there is no substitute for exact, traceable data for every penny spent. A seamless integration would involve transactional data pipelines that guarantee data consistency and integrity between the operational tool and the company's core financial systems, preventing data loss and ensuring full compliance.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Accurate budgeting and forecasting are direct results of having high-quality, granular historical data. The detailed cost transaction logs we collect are the foundation for building reliable predictive models. By analyzing this rich dataset, we can forecast future costs with greater precision, from labor and inventory to transportation expenses. A robust system would use this historical data to model different scenarios, allowing the company to allocate resources more effectively and set realistic budgets that are based on a deep understanding of past performance.

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Preserving the detailed cost history of the old warehouse is crucial for effective cost control during the transition. This history should be a granular record of all operational expenses, not just high-level summaries. This detailed data serves as a vital benchmark for setting the budget of the new warehouse and for conducting a precise gap analysis between the old and new facilities' operational efficiency. By comparing the detailed cost structures, we can identify specific areas where the new warehouse is over or under-performing, enabling targeted interventions to keep the operation within budget and ensure it meets performance expectations.

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
