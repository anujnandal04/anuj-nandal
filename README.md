# Bajaj Finserv Health Java Challenge

## Project Overview

This Spring Boot application was developed for the Bajaj Finserv Health Qualifier 1 challenge. The application automates a webhook-based workflow that generates a SQL query solution and submits it back to Bajaj Finserv's API.

**Date Submitted:** 2025-08-29

## Challenge Requirements

The application fulfills the following requirements:

1. Sends a POST request on startup to generate a webhook
2. Receives a webhook URL and access token in response
3. Solves a SQL problem (determined by the registration number)
4. Submits the SQL solution to the provided webhook URL with JWT authentication
5. No controllers/endpoints are used to trigger the flow

## Technical Implementation

### Project Structure

```
src/
  main/
    java/
      com/
        bajaj/
          finserv/
            BajajFinservApplication.java
            service/
              WebhookService.java
            model/
              WebhookRequest.java
              WebhookResponse.java
              SolutionRequest.java
            config/
              AppConfig.java
    resources/
      application.properties
pom.xml
```

### Technologies Used

- Java 11
- Spring Boot 2.7.3
- Maven
- RestTemplate for API calls
- Lombok for reducing boilerplate code

## SQL Solutions

The application determines which SQL problem to solve based on the registration number:

### For Odd Registration Numbers (Question 1):

```sql
SELECT d.department_name, COUNT(e.employee_id) AS employee_count 
FROM departments d 
LEFT JOIN employees e ON d.department_id = e.department_id 
GROUP BY d.department_name 
ORDER BY employee_count DESC, d.department_name ASC
```

### For Even Registration Numbers (Question 2):

```sql
SELECT p.product_name, SUM(o.quantity * o.unit_price) AS total_revenue 
FROM products p 
JOIN order_details o ON p.product_id = o.product_id 
GROUP BY p.product_name 
ORDER BY total_revenue DESC 
LIMIT 5
```

## Setup and Execution

### Prerequisites

- JDK 11+
- Maven 3.6+

### Building the Project

```bash
mvn clean package
```

### Running the Application

```bash
java -jar target/finserv-0.0.1-SNAPSHOT.jar
```

The application will automatically:
1. Send the webhook generation request
2. Determine the correct SQL problem to solve
3. Submit the solution to the provided webhook URL

## Implementation Details

- The application uses `CommandLineRunner` to execute the workflow on startup
- RestTemplate is used for API calls
- The workflow is fully automated with proper logging
- JWT token authentication is implemented as required

## Author

Developed by: Anuj Nandal  
GitHub: anujnandal04
