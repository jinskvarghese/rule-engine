# Rule Engine Application with AST

## Overview
This project implements a 3-tier rule engine application where rules can be dynamically created and evaluated based on user attributes. The rules are represented using an Abstract Syntax Tree (AST).

### Key Features:
- Dynamic creation, combination, and evaluation of rules using AST.
- Rule evaluation against user data based on conditions such as age, department, salary, etc.
- Error handling and validation for rule creation and data formats.

---

## Build Instructions

### Prerequisites:
- **Java 17**
- **Maven 3.8+**
- **PostgreSQL** 
- (Optional) **Docker** if using containers

### Database Setup

#### Option 1: PostgreSQL Setup Locally
1. Install PostgreSQL and create the database:
   ```sql
   CREATE DATABASE rule_engine;
   CREATE USER rule_engine_user WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE rule_engine TO rule_engine_user;

2. Update the src/main/resources/application.properties file with the following:
    ````shell
    spring.datasource.url=jdbc:postgresql://localhost:5432/rule_engine
    spring.datasource.username=rule_engine_user
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ````

##### Option 2: PostgreSQL in Docker
To use Docker for PostgreSQL:
````shell
docker run --name postgres-container -e POSTGRES_USER=rule_engine_user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=rule_engine -p 5432:5432 -d postgres
````


### Build and Run
1. Clone the repository:
    ````shell
    git clone https://github.com/jinskvarghese/rule-engine.git
    cd rule-engine
    ````

2. Build the project using Maven:
    ````shell
    ./mvnw clean install
    ````

3. Run the application:
    ````shell
    ./mvnw spring-boot:run
    ````

### API Endpoints
1. Create Rule
URL: /api/rules/create
Method: POST
Request Body:
json
Copy code
{
    "rule": "age > 30 AND department = 'Sales'"
}
Response: Returns the Abstract Syntax Tree (AST) representation of the rule.
2. Combine Rules
URL: /api/rules/combine
Method: POST
Request Body:
json
Copy code
{
    "rules": [
        "age > 30 AND department = 'Sales'",
        "age < 25 AND department = 'Marketing'"
    ]
}
Response: Returns the combined AST of the provided rules.
3. Evaluate Rule
URL: /api/rules/evaluate
Method: POST
Request Body:
json
Copy code
{
    "rule": "age > 30 AND department = 'Sales'",
    "userData": {
        "age": 35,
        "department": "Sales",
        "salary": 60000,
        "experience": 3
    }
}
Response: Returns true if the rule matches the user data, otherwise false.
Design Choices
Abstract Syntax Tree (AST) for Rule Representation:
Why AST?: AST allows for a hierarchical and flexible representation of rules. This structure enables dynamic creation and modification of rules, making it easier to combine and evaluate them based on different conditions.
Spring Boot:
Why Spring Boot?: Spring Boot provides fast setup for web applications with REST APIs, making it ideal for rapid development. It simplifies dependency management and offers excellent integration with databases like PostgreSQL.
PostgreSQL:
Why PostgreSQL?: PostgreSQL is chosen for its reliability, scalability, and strong SQL support. It efficiently stores rule metadata and user data required for evaluation.
Non-Functional Items
Error Handling: The application includes custom error handling for invalid rule strings and malformed data. Validation is performed during rule creation to ensure correctness.
Performance Considerations: AST ensures that rule evaluation is efficient, even for complex conditions. The structure is built to handle dynamic modification without affecting performance.
Testing Instructions
To test the API, you can use Postman or cURL. Here is an example using cURL:

Create a Rule:

bash
Copy code
curl -X POST http://localhost:8080/api/rules/create \
-H "Content-Type: application/json" \
-d '{"rule": "age > 30 AND department = '\''Sales'\''"}'
Evaluate a Rule:

bash
Copy code
curl -X POST http://localhost:8080/api/rules/evaluate \
-H "Content-Type: application/json" \
-d '{
      "rule": "age > 30 AND department = '\''Sales'\''",
      "userData": {
         "age": 35,
         "department": "Sales",
         "salary": 60000,
         "experience": 3
      }
    }'