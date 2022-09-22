
# Spring Security Demo

Demo project for Spring Security with JWT, and OAuth included.





## Run Locally

Clone the project

```bash
  git clone https://github.com/kentisthebest/spring-security.git
```

Use this SQL to create tables for user and authority

- [User Schema](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/appendix-schema.html)


Using Command-line Arguments to run Spring boot project
```bash
mvn spring-boot:run -D"spring-boot.run.arguments=--spring.datasource.url=<jdbc-url> --spring.datasource.username=<username> --spring.datasource.password=<password> --spring.jpa.properties.hibernate.dialect=<dialect> --spring.security.key=<key>"
```

Alternatively, you can edit the application.properties and just run it.

## Running Tests

To run tests, run the following command

```bash
  mvn test
```


## Tech Stack
Java SQL


## API Documentation (swagger ui)

http://localhost:8080/api/swagger-ui/index.html


## Authors

- [@kentisthebest](https://github.com/kentisthebest)
- [@kentembutido](https://github.com/kentembutido)
