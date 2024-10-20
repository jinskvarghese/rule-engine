# Rule Engine Application with AST (Frontend + Backend)

## Overview
This application is a 3-tier rule engine that allows users to define eligibility rules based on attributes like age, department, income, and spend. The system utilizes an Abstract Syntax Tree (AST) to represent, evaluate, and modify rules dynamically. The primary user interaction happens via the frontend UI, which provides an intuitive way to create, combine, and evaluate rules.


### Key Features:
- **Frontend UI**: A simple, user-friendly interface for creating, combining, and evaluating rules.
- **Rule Creation**: Users can dynamically define rules based on attributes such as `age`, `department`, `salary`, etc.
- **Rule Combination**: Multiple rules can be combined and stored for future evaluations.
- **Rule Evaluation**: The frontend allows users to submit a JSON object for rule evaluation.
- **Backend API**: The frontend communicates with the backend to handle all operations related to rule creation, combination, and evaluation.
- **WebSocket Integration**: Real-time updates for rule creation and evaluation.
- **Caching**: Optimized with caching mechanisms to improve performance when evaluating frequently used rules.
- **Security**: The system has basic security (HTTP basic authentication) to restrict unauthorized access.

---

## Tech Stack
- **Frontend**: HTML, CSS, JavaScript (for making API calls).
- **Backend**: Spring Boot, Java, REST API.
- **Database**: PostgreSQL (for rule storage).
- **Caching**: In-memory caching using Spring Cache.
- **WebSocket**: Real-time communication using SockJS and STOMP
- **Security**: HTTP Basic Authentication for protected endpoints.

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

#### Option 2: PostgreSQL in Docker
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
**URL**: `/api/rules/create`
**Method**: POST
**Request Body**:
````shell
{
    "rule": "age > 30 AND department = 'Sales'"
}
````
**Response**: Returns the Abstract Syntax Tree (AST) representation of the rule.

2. Combine Rules
**URL**: `/api/rules/combine`
**Method**: POST
**Request Body**:
````shell
{
    "rules": [
        "age > 30 AND department = 'Sales'",
        "age < 25 AND department = 'Marketing'"
    ]
}
````
**Response**: Returns the combined AST of the provided rules.

3. Modify Rule
- **URL** `/api/rules/modify`
- **Method**:POST
- **Request Body**: 
    ```shell
    {
      "oldCondition": "age > 30",
      "newCondition": "age < 25"
    }
    ```
- **Response**: Returns the modified Abstract Syntax Tree (AST).

4. Evaluate Rule
**URL**: `/api/rules/evaluate`
**Method**: POST
**Request Body**:
````shell
{
    "rule": "age > 30 AND department = 'Sales'",
    "userData": {
        "age": 35,
        "department": "Sales",
        "salary": 60000,
        "experience": 3
    }
}
````
**Response**: Returns true if the rule matches the user data, otherwise false.

## WebSocket Integration (Real-time Updates)
The application provides real-time rule creation and evaluation updates using WebSockets. These updates are displayed directly on the frontend.

## Frontend UI
The project includes a basic HTML-based frontend to interact with the API.

### Accessing the UI
1. Start the Spring Boot application.
2. Open a browser and go to http://localhost:8080.

### Security Layer
- A basic HTTP authentication has been implemented using Spring Security.
- To access any API endpoint, the user needs to authenticate with the following credentials:
  - Username: `assignment1`
  - Password: `zeotap`

## Caching

The application uses Spring Boot's caching mechanism to optimize the performance of rule evaluations. The `@Cacheable` annotation is used in the `RuleEvaluationService` to cache rule evaluation results for repeated inputs. The caching mechanism stores the evaluated results of rules to avoid recomputation.

- **Cache Type**: Caffeine
- **Cache Name**: `ruleEvaluations`
- **Cache Expiry**: Entries expire after 10 minutes
- **Maximum Cache Size**: 500 (if using Caffeine)

To modify the caching behavior, adjust the cache settings in `application.properties`.


### Features:
- Create Rule: Allows users to input a rule and submit it to the /create API.
- Combine Rules: Accepts multiple rules (comma-separated) and submits them to the /combine API.
- Evaluate Rule: Inputs both a rule and user data in JSON format to evaluate the rule via the /evaluate API.

## Example Flow:
1. Create Rule:

Enter a rule like 
````shell
age > 30 AND department = 'Sales'
````
Click on Create Rule.
The rule's AST will be displayed below.

2. Combine Rules:

Enter multiple rules separated by commas, e.g., 
````shell
age > 30 AND department = 'Sales', age < 25 AND department = 'Marketing'
````
Click Combine Rules.
The combined rule AST will be displayed.

3. Evaluate Rule:

Enter a rule and JSON data in the form:
````shell
{"age": 35, "department": "Sales", "salary": 60000, "experience": 3}
````
Click Evaluate Rule.
The result (True/False) will be displayed.

## Design Choices
### Abstract Syntax Tree (AST) for Rule Representation:
- AST allows for a hierarchical and flexible representation of rules. This structure enables dynamic creation and modification of rules, making it easier to combine and evaluate them based on different conditions.
### Spring Boot:
- Spring Boot provides fast setup for web applications with REST APIs, making it ideal for rapid development. It simplifies dependency management and offers excellent integration with databases like PostgreSQL.
### PostgreSQL:
- PostgreSQL is chosen for its reliability, scalability, and strong SQL support. It efficiently stores rule metadata and user data required for evaluation.
### Frontend:
- A basic HTML interface allows users to create, combine, and evaluate rules using forms.
JavaScript (with fetch) is used for making API calls.

## Non-Functional Items
- Error Handling: The application includes custom error handling for invalid rule strings and malformed data. Validation is performed during rule creation to ensure correctness.
- Performance Considerations: AST ensures that rule evaluation is efficient, even for complex conditions. The structure is built to handle dynamic modification without affecting performance.

## Additional Notes
- **Performance**: Caching has been implemented to optimize evaluation speed for frequently used rules.
- **Security**: Basic HTTP authentication is implemented to restrict access to API endpoints.
- **Extensibility**: The system is designed to support dynamic rule modifications and extensions in the future.
- **WebSocket Integration**: This provides real-time updates using WebSockets.

### Future Enhancements: User-Defined Functions

The system can be extended to support user-defined functions within the rule language. This would allow for advanced conditions and computations, such as:
- `calculateBonus(salary, experience)`
- `isEligibleForPromotion(age, department)`

This feature is not implemented in the current version but is a potential future enhancement.
