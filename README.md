# Mensa Web App

This project is designed for the Software Testing course.
It provides a web application built with JSF
and shall be used as a running example for assignments throughout the course.

[![pipeline status](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/badges/main/pipeline.svg)](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/-/commits/main)
[![coverage report](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/badges/main/coverage.svg)](https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp/-/commits/main)

## Mensa Web Application

A comprehensive web application designed for managing and displaying university cafeteria (Mensa) menus with advanced filtering capabilities. This project was developed as part of the Software Testing course at the University of Passau.

## Features

- **Menu Display**: View daily menus with detailed dish information
- **Advanced Filtering**: Filter dishes based on various criteria
  - Exclude dishes containing specific allergens
  - Filter by food additives
  - Categorize dishes using tags
- **User-Friendly Interface**: Clean, responsive design built with JavaServer Faces (JSF)
- **Data Management**: Automatic data cleanup and scheduling
- **Error Handling**: Comprehensive error handling and logging

## Technologies Used

- **Backend**:
  - Java 17+
  - Jakarta EE 9+ (formerly Java EE)
  - Contexts and Dependency Injection (CDI) for dependency injection
  - Java Persistence API (JPA) for data management
  - Maven for build automation and dependency management

- **Frontend**:
  - JavaServer Faces (JSF) with Facelets
  - PrimeFaces component library
  - JavaScript (minimal, for basic interactivity)

- **Testing**:
  - JUnit 5 for unit testing
  - Mockito for mocking dependencies
  - Cucumber for Behavior-Driven Development (BDD) testing
  - Selenium for end-to-end testing

- **Tools**:
  - Apache Tomcat 10+ as the application server
  - Git for version control
  - IntelliJ IDEA as the primary IDE

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── de/uni_passau/fim/se2/st/mensawebapp/
│   │       ├── business/       # Business logic and services
│   │       ├── global/         # Global configurations and utilities
│   │       └── view/           # Web interface and backing beans
│   ├── resources/             # Configuration files
│   └── webapp/                # Web resources (JSF pages, CSS, etc.)
└── test/
    └── java/                  # Test source files
        └── de/uni_passau/fim/se2/st/mensawebapp/
            └── cucumber/      # BDD test definitions
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.6.0 or later
- Apache Tomcat 10.0 or later
- Git (for version control)

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://gitlab.infosun.fim.uni-passau.de/se2/teaching/software-testing/mensawebapp.git
   cd mensawebapp
   ```

2. **Build the application**:
   ```bash
   mvn clean package
   ```

3. **Deploy to Tomcat**:
   - Copy the generated `target/mensawebapp.war` to your Tomcat's `webapps` directory
   - Start Tomcat server

### Running in IntelliJ IDEA

1. **Import the project** as a Maven project
2. **Configure Tomcat**:
   - Go to `Run` > `Edit Configurations`
   - Add a new `Tomcat Server` > `Local` configuration
   - In the `Deployment` tab, add the `mensawebapp:war exploded` artifact
   - Apply the changes and click `OK`
3. **Run the application** using the configured Tomcat run configuration

## Testing

The project includes comprehensive test suites:

- **Unit Tests**: `mvn test`
- **Integration Tests**: `mvn verify`
- **BDD Tests**: Run the Cucumber feature files in `src/test/resources/features/`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Contact

For any inquiries, please contact the project maintainers.
