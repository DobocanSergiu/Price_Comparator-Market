## Overview
The following project is my attempt at the Software Engineer Internship Coding Challenge by Accessa.  The application uses a layered application (Repository, Service and Controller) to help separate concerns and improve readability. 
1. Repository layer uses JPA to interact with the existing PostgreSQL database.
2. Service Layer contains the majority of the business logic and other helper functions (Metric Conversion, Password salting etc.)
3. Controller contains all api endpoints, separated into 2 files: Product (deals with product inventory and price history) and User (deals with user login validation, user shopping cart and user watch list)
4. DTO'S are separated for better readability

Database Schema:
![Price Comparator-Market-db](https://github.com/user-attachments/assets/288ee5f1-1863-46e6-9c91-3bd568acfefe)
## Installation
1. Prerequisite
Before starting make sure java 17 and PostgreSQL are installed. 
2. Create the database
3. Clone repository
- Setup the following environment variables:
``username={database_username};``
``password={database_user_password};``
``db_url={database_jdbc_url};``
4. Setup the following application.properties file:
```
spring.application.name=Price Comparator Market
spring.datasource.url=${db_url}
spring.datasource.username=${username}
spring.datasource.password=${password}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
5. Make sure the following Spring dependency's are present:
- Spring Boot Starter Data JPA
-  Spring Boot Starter Web
- PostgreSQL JDBC Driver
- Spring Boot Starter Test
- Spring Security Crypto

## API Endpoints Examples


- getUserWatchListAtTargetOrLower
```
GET http://localhost:8080/users/getUserWatchListAtTargetOrLower/{{userId}}
/// Returns products from a specified users watch lists who's price is lower or at a target price set by the user
```
- validateLogin
```
POST http://localhost:8080/users/validateLogin  
Content-Type: application/json  
  
{  
"username": "Mike123", 
"password": "M1k3th3b3st777"
}
/// Validates user login information.
```
- getAllDiscounts
```
GET http://localhost:8080/products/getAllDiscounts
/// Returns all discounts present in databse (past, present and future)
```

- getAllPresentOrFutureDiscounts
```
GET http://localhost:8080/products/getAllPresentOrFutureDiscounts
/// Returns all current and upcoming discounts
```
- getAllDiscountsByStore
```
GET http://localhost:8080/products/getAllDiscountsByStore/{{store}}
/// Returns all discounts associated with a specific store (past, present and future)
```
- getAllPresentOrFutureDiscountsByStore
```
GET http://localhost:8080/products/getAllPresentOrFutureDiscountsByStore/{{store}}
/// Returns all present or future discounts associated with a specific store
```
- getLast24hDiscounts
```
GET http://localhost:8080/products/getLast24hDiscounts
/// Returns the most recent discounts (last 24 hours) from all stores
```
- getPricesOfGivenProductAtStores
```
GET http://localhost:8080/products/getPricesOfGivenProductAtStores/{{productName}}
/// Returns the price history of a specific product, at each store that has it in its inventory
```
- getPricesOfGivenProductAtSpecificStore
```
GET http://localhost:8080/products/getPricesOfGivenProductAtSpecificStore/{{productName}}/{{store}}
/// Returns the price history of a specific product at a specific store
```
- getBestBuy
```
GET http://localhost:8080/products/getBestBuy/{{productName}}
/// Returns the best buy (best value per unit) alternative for a specified product
```
- getMostWanted
```
GET http://localhost:8080/products/getMostWanted
/// Goes through every watch list and returns the most common product
```
