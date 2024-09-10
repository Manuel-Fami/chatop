# Chatop

## Description

The chatop application manages the backend of the chatop site created in Angular.

You'll find the front end here:
https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring

This site puts vehicle owners in touch with potential users, for rental purposes.

## Installation

To run the application, you need to have the following software installed:

- Java
- Maeven
- MariaDB and have a working Mysal database.
- An IDE that supports Spring Boot

Download or clone the directory and install it locally.

To launch the application

Generate a JAR in the project root folder by running :
mvn clean install
Launch the application by running
spring-boot:run

## Tests

### Postman

You can use Postman to test the ChaTop API.
Download the Postman collection and environment script: https://www.postman.com/

ChaTop API Postman Collection
Note that only the /api/auth/register and /api/auth/login entry points do not require authentication. It is advisable to create a first user by sending a POST request to /api/auth/register and use the returned JWT token to authenticate other requests.

You'll find the collections script in: main/java/ressources/postman/rental.postman_collection.json

## Documentation

The project uses Swagger to automatically generate comprehensive, interactive REST API documentation. Swagger, via Springdoc OpenAPI integration, enables developers and users to view and test the various API endpoints directly from the web interface.

Here is the link :
http://localhost:3001/api/swagger-ui/index.html#/

You must have launched the project to access the documentation.
