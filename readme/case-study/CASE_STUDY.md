# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
Core Challenges in a Fulfillment Environment
A. Direct vs Indirect Costs
Some costs are straightforward:
Labor assigned to a specific warehouse
Rent for a specific facility
Inventory purchase cost

    B. Allocation Methodology
    Transportation cost could be allocated by:Number of shipments, Weight, Volume, Revenue

    C. Timing & Accounting Periods
    Fulfillment operations and accounting timelines don’t always align.

    D. Warehouse Lifecycle (Replace Scenario)
    E. Data Accuracy & Source of Truth
    F. Granularity & Performance

Important Design Considerations
1. Immutability: Financial data should ideally be append-only.
2. Deterministic Allocation 
   Allocation logic must be:Predictable, Reproducible, Traceable
3. Precision & Rounding
    Never use floating point for money. BigDecimal, Defined rounding rules, Consistent scale



## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
A. Network Optimization (Warehouse & Store Footprint)
    Strategies:
        Consolidate underutilized warehouses
        Replace high-cost facilities
        Reposition inventory geographically
        Reduce inter-warehouse transfers
    Expected Outcomes
        Lower fixed overhead
        Reduced duplicate coverage
        Improved transportation efficiency

Better inventory pooling
B. Labor Optimization
   Strategies
       Improve picking route design
       Batch order processing
       Slot high-frequency SKUs closer to dispatch 
       Better workforce scheduling via demand forecasting
       Cross-training employees

   Expected Outcomes
        Lower labor cost per order
        Higher throughput
        Reduced overtime
        Improved productivity
C. Inventory Optimization
   Strategies
        Demand forecasting improvements
        Reduce excess safety stock
        ABC classification (focus on high-value SKUs)
        Reduce slow-moving stock 
        Improve replenishment policies

   Expected Outcomes
        Lower holding costs
        Reduced capital tied in inventory
        Less obsolescence
   Improved cash flow
D. Transportation Optimization
   Strategies
        Shipment consolidation
        Route optimization
        Carrier negotiation
        Ship-from-closest warehouse logic
        Reduce expedited shipping
   Expected Outcomes
        Lower cost per shipment
        Reduced fuel expenses
        Fewer emergency transfers
        Improved delivery predictability
E. Process & Automation Optimization
   Strategies
        Warehouse automation (conveyors, robotics)
        Barcode/RFID tracking
        Process standardization
        Performance dashboards
   Expected Outcomes
        Higher operational consistency
        Reduced error rate
        Long-term labor savings
        Improved scalability

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Why Integration Is Important
A. Single Source of Truth
    If these systems are not integrated:
        Costs are manually reconciled
        Delays occur
        Data inconsistencies arise
        Decision-making becomes reactive instead of proactive
B. Real-Time Cost Visibility

Benefits of Integration
1. Financial Accuracy
2. Better Decision-Making
3. Faster Financial Close
4. Improved Budget Control
5. Stronger Governance & Compliance

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Why Budgeting & Forecasting Are Critical in Fulfillment
    A. Proactive Cost Control
    B. Workforce Planning & Capacity Management
    C. Inventory Optimization
    D. Transportation Planning
    E. Capital Allocation
Key Considerations When Designing a Budgeting & Forecasting System
    A. Identify Cost Drivers (Driver-Based Forecasting)
    B. Integrate Historical Data
    C. Separate Fixed vs Variable Costs
    D. Real-Time Data Feeds
    E. Scenario Planning & Simulation

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Why Preserving Cost History Is Critical
A. Ensures Financial Continuity
   The Business Unit Code represents:
        A geographical area
        A cost center
        A profit & loss entity

B. Enables Meaningful Variance Analysis
    Budgeting relies heavily on historical data.
    For example:
        Labor cost per unit shipped
        Transportation cost per route
        Overhead per square meter
        Cost per order
C. Supports Accurate Budget Setting
    When creating the new Warehouse budget, you need:
        Historical cost trends
        Seasonality patterns
        Volume-cost relationships
        Fixed vs variable cost breakdown
D. Audit & Compliance Requirements
    Financial records must:
        Preserve transaction history
        Maintain traceability
        Support audit review



Cost Control Aspects During Warehouse Replacement
   1. Transition Costs
   2. Fixed Cost Reset
   3️. Overhead Allocation Adjustments
   4️. Operational Ramp-Up Curve

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
