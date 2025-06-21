# Advanced REST Assured API Testing Framework

[![Build Status](https://github.com/gourav-007/rest-in-piece/actions/workflows/weekly-tests.yml/badge.svg?branch=main)](https://github.com/gourav-007/rest-in-piece/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=rest-in-piece&metric=alert_status)](https://sonarcloud.io/dashboard?id=rest-in-piece)

## 🚀 Overview

**Advanced REST Assured API Testing Framework** is a comprehensive, production-ready API automation framework built with Java, REST Assured, and TestNG. This framework provides enterprise-level features including environment management, retry mechanisms, detailed reporting, and parallel execution capabilities.

## ✨ Key Features

### 🏗️ Architecture & Design
- **Modular Design**: Clean separation of concerns with dedicated packages for models, services, utilities, and tests
- **SOLID Principles**: Follows object-oriented design principles for maintainability and extensibility
- **Page Object Model**: Service layer pattern for API interactions
- **Builder Pattern**: Fluent API for test data creation

### 🔧 Core Capabilities
- **Multi-Environment Support**: DEV, QA, PROD configurations with easy switching
- **Authentication Management**: Automatic token handling with caching and refresh
- **Retry Mechanisms**: Configurable retry policies for flaky tests
- **Parallel Execution**: TestNG parallel execution with configurable thread pools
- **Schema Validation**: JSON schema validation for response verification
- **Data-Driven Testing**: External test data management with JSON files

### 📊 Reporting & Monitoring
- **Allure Reports**: Rich, interactive test reports with screenshots and logs
- **Detailed Logging**: Structured logging with SLF4J and Logback
- **Request/Response Logging**: Complete API interaction logging
- **Performance Metrics**: Response time validation and monitoring

### 🛡️ Quality Assurance
- **Comprehensive Validations**: Status codes, headers, response times, JSON paths
- **Negative Testing**: Error scenarios and edge cases
- **Schema Validation**: Automated JSON schema verification
- **Exception Handling**: Robust error handling with meaningful messages

## 📁 Project Structure

```
src/
├── main/java/
│   ├── auth/                    # Authentication management
│   ├── config/                  # Configuration management
│   ├── core/                    # Core framework components
│   ├── dataproviders/           # Test data providers
│   ├── models/                  # Data models and POJOs
│   ├── services/                # API service layer
│   └── utils/                   # Utility classes
├── test/java/
│   ├── base/                    # Base test classes
│   └── tests/                   # Test implementations
└── test/resources/
    ├── environments/            # Environment configurations
    ├── schemas/                 # JSON schemas
    ├── testdata/               # Test data files
    └── testng-suites/          # TestNG suite configurations
```

## 🚀 Quick Start

### Prerequisites
- **Java 11+**: Ensure Java 11 or later is installed
- **Maven 3.6+**: Apache Maven for dependency management
- **IDE**: IntelliJ IDEA or Eclipse with TestNG plugin

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/gourav-007/rest-in-piece.git
   cd rest-in-piece
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Run smoke tests**:
   ```bash
   mvn test -Denv=dev -Dsuite=smoke
   ```

## 🔧 Configuration

### Environment Configuration

The framework supports multiple environments through property files:

- `src/test/resources/environments/dev.properties`
- `src/test/resources/environments/qa.properties`
- `src/test/resources/environments/prod.properties`

Example configuration:
```properties
base.url=https://restful-booker.herokuapp.com
auth.username=admin
auth.password=password123
timeout.connection=30000
timeout.read=60000
retry.max.attempts=3
retry.delay.seconds=2
parallel.thread.count=3
```

### Test Execution

#### Run tests with different environments:
```bash
# Development environment
mvn test -Denvironment=dev -Dsuite=smoke

# QA environment with regression suite
mvn test -Denvironment=qa -Dsuite=regression

# Production environment
mvn test -Denvironment=prod -Dsuite=smoke
```

#### Run specific test suites:
```bash
# Smoke tests
mvn test -Dsuite=smoke

# Regression tests
mvn test -Dsuite=regression

# End-to-end tests
mvn test -Dsuite=e2e
```

#### Parallel execution:
```bash
# Run tests in parallel
mvn test -Dsuite=regression -Dparallel=methods -DthreadCount=5
```

## 📝 Writing Tests

### Basic Test Structure

```java
@Epic("Booking Management")
@Feature("CRUD Operations")
public class BookingCrudTests extends BaseTest {
    
    private final BookingService bookingService = new BookingService();
    
    @Test
    @Story("Create Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test creating a new booking with valid data")
    public void testCreateBooking() {
        // Arrange
        BookingRequest bookingRequest = BookingDataProvider.createValidBookingRequest();
        
        // Act
        BookingResponse response = bookingService.createBooking(bookingRequest);
        
        // Assert
        assertThat("Booking ID should not be null", response.getBookingid(), notNullValue());
        assertThat("Booking ID should be positive", response.getBookingid(), greaterThan(0));
    }
}
```

### Service Layer Example

```java
@Slf4j
public class BookingService {
    
    @Step("Create new booking")
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        return RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .body(bookingRequest)
                    .when()
                    .post("/booking")
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            return JsonUtils.fromJson(response.getBody().asString(), BookingResponse.class);
        });
    }
}
```

### Data Providers

```java
public class BookingDataProvider {
    
    public static BookingRequest createValidBookingRequest() {
        return BookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(100, 2000))
                .depositpaid(true)
                .bookingdates(createValidBookingDates())
                .additionalneeds("Breakfast")
                .build();
    }
}
```

## 📊 Reporting

### Allure Reports

Generate and view Allure reports:

```bash
# Generate Allure report
mvn allure:report

# Serve Allure report
mvn allure:serve
```

### Test Results

The framework generates comprehensive reports including:
- Test execution summary
- Request/Response details
- Screenshots for failures
- Performance metrics
- Environment information

## 🔍 Best Practices

### Test Organization
- **Group related tests**: Use TestNG groups and Allure annotations
- **Follow AAA pattern**: Arrange, Act, Assert
- **Use descriptive names**: Clear test and method names
- **Implement proper assertions**: Meaningful assertion messages

### Data Management
- **External test data**: Use JSON files for test data
- **Data builders**: Implement builder pattern for test objects
- **Faker integration**: Generate realistic test data

### Error Handling
- **Retry mechanisms**: Implement retry for flaky tests
- **Proper logging**: Log important test steps and errors
- **Meaningful exceptions**: Throw descriptive exceptions

### Performance
- **Parallel execution**: Use TestNG parallel features
- **Connection pooling**: Reuse HTTP connections
- **Response time validation**: Monitor API performance

## 🛠️ Advanced Features

### Authentication Management

```java
// Automatic token management
String token = AuthenticationManager.getAuthToken();

// Token caching and refresh
AuthenticationManager.clearToken(); // Force refresh
```

### Retry Mechanisms

```java
// Retry configuration in properties
retry.max.attempts=3
retry.delay.seconds=2

// Usage in code
RetryManager.executeWithRetry(() -> {
    // Your API call here
});
```

### Schema Validation

```java
// Validate response against JSON schema
ResponseValidator.validateJsonSchema(response, "schemas/booking-schema.json");
```

### Environment Switching

```java
// Get current environment configuration
EnvironmentConfig config = ConfigManager.getConfig();
String baseUrl = config.baseUrl();
```

## 🚀 CI/CD Integration

### GitHub Actions

The framework includes GitHub Actions workflow for:
- Automated test execution
- Multi-environment testing
- Report generation
- Dependency updates

### Maven Profiles

```bash
# Use Maven profiles for different environments
mvn test -Pdev -Dsuites=smoke
mvn test -Pqa -Dsuites=regression
mvn test -Pprod -Dsuites=smoke
```

## 📚 Dependencies

### Core Dependencies
- **REST Assured 5.3.2**: API testing framework
- **TestNG 7.8.0**: Testing framework
- **Jackson 2.15.2**: JSON processing
- **Allure 2.24.0**: Test reporting
- **SLF4J/Logback**: Logging framework

### Utility Dependencies
- **JavaFaker 1.0.2**: Test data generation
- **Owner 1.0.12**: Configuration management
- **Failsafe 3.3.2**: Retry mechanisms
- **Lombok 1.18.30**: Code generation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues

1. **Connection Timeouts**:
   - Increase timeout values in environment properties
   - Check network connectivity

2. **Authentication Failures**:
   - Verify credentials in environment properties
   - Check token expiry settings

3. **Test Failures**:
   - Review Allure reports for detailed error information
   - Check logs in `target/logs/` directory

4. **Parallel Execution Issues**:
   - Reduce thread count in TestNG configuration
   - Ensure tests are thread-safe

### Support

For support and questions:
- Create an issue on GitHub
- Check existing documentation
- Review test logs and reports

## 🎯 Roadmap

- [ ] Database validation integration
- [ ] API mocking capabilities
- [ ] Performance testing integration
- [ ] Docker containerization
- [ ] Kubernetes deployment support
- [ ] GraphQL API testing support