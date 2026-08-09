# Cinema reservation system

## Overview
This is a project made for OOP course at university. It was migrated from dataproctech org.

It focuses on the implementation of a cinema reservation system. Currently it allows users to read movies, screenings and to lock and reserve seats for specific screening in the cinema.

It was developed to get better familiarity with Java and its libraries without introducing Spring Boot yet. However I decided to put into practice some of the architectural concepts I learned during my last internship. These are:
- separation of commands and queries
- rich domain model
- basic domain-driven design
- repository pattern
- ports and adapters architecture
- and others...

## Getting started

1. Clone the repository and open it in your favorite IDE.
2. run `./gradlew run`.
3. Optional step: run `./gradlew seedDb` to seed the database with some dummy data.
4. Open your browser and go to `http://localhost:7070/swagger` to see app's API docs. You can also execute HTTP requests with `http/api.http` file in the project.

## Unit testing
This project uses JUnit and AssertJ for unit testing. To run the tests, execute `./gradlew test` in the terminal.

Only domain logic is tested, it uses given-when-then testing style. I also use test fixtures and separate clock to simplify given part of the tests.

## Main tech stack
- Java 21
- Gradle 8
- Javalin
- SQLite
