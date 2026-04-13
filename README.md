# 🚀 Event-Driven Microservices Testing Framework

This project provides a professional-grade automation framework for **End-to-End (E2E) integration testing** in event-driven architectures. It focuses on validating data integrity and contract enforcement (Schema Validation) between asynchronous microservices.



## 🏗️ The Testing Architecture

The framework is built with modularity and scalability in mind, following industry-standard patterns:

* **"core/"**: Infrastructure layer handling communication with the Message Broker (Upstash) and Consumer Mock (Webhook.site).
* **"models/"**: Strongly-typed POJOs representing event structures, ensuring type safety and easy maintenance.
* **"utils/"**: Data Factory implementation for generating dynamic, randomized test data to ensure test independence.
* **"resources/schemas/"**: Centralized JSON Schema definitions for Contract Testing.

## 🛠️ Tech Stack

* **Java 21**: Utilizing the latest LTS features.
* **Maven**: Dependency management and build automation.
* **Upstash (QStash)**: Serverless Message Broker used to simulate real-world asynchronous messaging (Kafka/SQS alternative).
* **Jackson**: High-performance JSON serialization and deserialization.
* **Awaitility**: Fluent API for handling asynchronous polling, effectively eliminating flaky tests.
* **Everit JSON Schema**: Robust validation engine for contract testing.
* **JUnit 4**: Reliable test runner and assertion framework.

## 🧪 Test Scenarios

### 1. Happy Path: Asynchronous Integration & Contract Validation
* Generates a unique "ORDER_CREATED" event via "OrderDataFactory".
* Publishes the event to the Broker via REST API.
* Uses **Awaitility** to poll the consumer endpoint until delivery is confirmed.
* Performs a **JSON Schema Validation** to ensure the received message strictly follows the defined contract (data types, required fields and constraints).

### 2. Negative Scenario: Resilience & Data Integrity
* Produces a malformed event (e.g., missing mandatory "order_id").
* Verifies that the consumer does not receive or process the invalid data.
* Ensures the system fails gracefully and maintains data consistency across services.

## 🚀 How to Run

### Prerequisites
Configure the following environment variables on your machine:
* "QSTASH_TOKEN": Your Upstash Authorization Token.
* "WEBHOOK_ID": Your unique Webhook.site token ID.

### Execution
Run the full suite using Maven:
```bash
mvn clean test
```

## 📈 Key Technical Highlights

* **Contract Testing**: The suite performs real-time JSON Schema validation. If a developer changes a data type or removes a mandatory field in the microservice, the tests will fail immediately, preventing breaking changes from reaching production.
* **Anti-Flaky Design**: Zero use of hard-coded "Thread.sleep()". By implementing **Awaitility**, the framework uses smart polling to wait exactly as long as the asynchronous broker needs, optimizing execution time and reliability.
* **Data Independence & Isolation**: Each test run utilizes an "OrderDataFactory" to generate unique datasets. This prevents data collisions and allows the suite to be executed reliably in parallel or across different environments (Staging, Pre-prod).
* **Strongly-Typed Architecture**: Uses POJOs for event representation, ensuring that the test code is maintainable, readable and less prone to errors compared to handling raw JSON strings.
* **Observability**: Integrated logging providing a clear step-by-step trail of the event's lifecycle, from production to delivery and final validation.

---

*Built as a technical showcase for high-level QA Automation and SDET practices.*

---

**Developed by Guilherme Castelo**
*Senior Quality Engineer | SDET | Data Integrity Specialist*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/guilhermecastelo/)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:lguilherme.castelo@gmail.com)	