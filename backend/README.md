# Exchange rate backend application

This is a Spring Boot-based integrates with the [ECB data api](https://data.ecb.europa.eu/help/api/overview) 
to provide Euro exchange rates

## Requirements

- **Java 21**
- **Gradle** for building the project

## Installation

### Prerequisites

Before you begin, ensure that you have the following installed:
- **Java** (JDK 21) 
- **Gradle**

### Running the Application

To run the application locally, use Gradle to run the Spring Boot application:

```bash
gradle bootRun
```

## TODOs

* Implement error handling and retrying mechanism if data population fails
* Improve request logging
