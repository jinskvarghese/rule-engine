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

    spring.datasource.url=jdbc:postgresql://localhost:5432/rule_engine
    spring.datasource.username=rule_engine_user
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

##### Option 2: PostgreSQL in Docker
To use Docker for PostgreSQL:

docker run --name postgres-container -e POSTGRES_USER=rule_engine_user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=rule_engine -p 5432:5432 -d postgres


### Build and Run
1. Clone the repository:

    git clone https://github.com/jinskvarghese/rule-engine.git
    cd rule-engine

2. Build the project using Maven:

    ./mvnw clean install

3. Run the application:

    ./mvnw spring-boot:run