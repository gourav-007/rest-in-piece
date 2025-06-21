# Setup Guide

## Prerequisites

### System Requirements
- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
- **Git**: For version control
- **Operating System**: Windows, macOS, or Linux

### Verification Commands
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Git version
git --version
```

## Installation Steps

### 1. Clone the Repository
```bash
git clone https://github.com/gourav-007/rest-in-piece.git
cd rest-in-piece
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Installation
```bash
mvn test -Dsuite=smoke -Denvironment=dev
```

## IDE Setup

### IntelliJ IDEA

1. **Import Project**:
   - File → Open → Select project directory
   - Choose "Import as Maven project"

2. **Configure TestNG**:
   - Install TestNG plugin if not already installed
   - File → Settings → Plugins → Search "TestNG"

3. **Configure Allure**:
   - Install Allure plugin
   - File → Settings → Plugins → Search "Allure"

4. **Set JDK**:
   - File → Project Structure → Project → Project SDK → Select JDK 11+

### Eclipse

1. **Import Project**:
   - File → Import → Existing Maven Projects
   - Select project directory

2. **Install TestNG**:
   - Help → Eclipse Marketplace → Search "TestNG"
   - Install TestNG for Eclipse

3. **Configure Build Path**:
   - Right-click project → Properties → Java Build Path
   - Ensure JDK 11+ is selected

### VS Code

1. **Install Extensions**:
   - Java Extension Pack
   - TestNG Runner
   - Maven for Java

2. **Configure Java**:
   - Ctrl+Shift+P → "Java: Configure Runtime"
   - Set JDK 11+ as default

## Environment Configuration

### 1. Environment Files
Create or modify environment-specific property files:

```bash
src/test/resources/environments/
├── dev.properties
├── qa.properties
├── prod.properties
└── default.properties
```

### 2. Sample Configuration
```properties
# dev.properties
base.url=https://restful-booker.herokuapp.com
auth.username=admin
auth.password=password123
timeout.connection=30000
timeout.read=60000
retry.max.attempts=3
retry.delay.seconds=2
parallel.thread.count=3
```

### 3. Environment Variables (Optional)
```bash
export ENVIRONMENT=dev
export BASE_URL=https://restful-booker.herokuapp.com
export AUTH_USERNAME=admin
export AUTH_PASSWORD=password123
```

## Running Tests

### Command Line Execution

#### Basic Test Execution
```bash
# Run all tests
mvn test

# Run specific suite
mvn test -Dsuite=smoke

# Run with specific environment
mvn test -Denvironment=qa -Dsuite=regression
```

#### Parallel Execution
```bash
# Run tests in parallel
mvn test -Dsuite=regression -Dparallel=methods -DthreadCount=5
```

#### Profile-based Execution
```bash
# Using Maven profiles
mvn test -Pdev -Dsuite=smoke
mvn test -Pqa -Dsuite=regression
mvn test -Pprod -Dsuite=smoke
```

### IDE Execution

#### IntelliJ IDEA
1. Right-click on test class/method
2. Select "Run" or "Debug"
3. Configure run configuration if needed

#### Eclipse
1. Right-click on test class
2. Run As → TestNG Test

## Report Generation

### Allure Reports

#### Install Allure Command Line
```bash
# macOS
brew install allure

# Windows (using Scoop)
scoop install allure

# Linux
sudo apt-get install allure
```

#### Generate Reports
```bash
# Generate report
mvn allure:report

# Serve report (opens in browser)
mvn allure:serve

# Generate and open report
mvn test allure:serve
```

### Viewing Reports
- **Allure Report**: `target/site/allure-maven-plugin/index.html`
- **Surefire Report**: `target/site/surefire-report.html`
- **Logs**: `target/logs/test-execution.log`

## Troubleshooting

### Common Issues

#### 1. Java Version Issues
```bash
# Error: Unsupported Java version
# Solution: Ensure JDK 11+ is installed and JAVA_HOME is set
export JAVA_HOME=/path/to/jdk11
```

#### 2. Maven Dependencies
```bash
# Error: Dependencies not resolved
# Solution: Clean and reinstall
mvn clean install -U
```

#### 3. TestNG Plugin Issues
```bash
# Error: TestNG not found
# Solution: Verify TestNG plugin installation in IDE
```

#### 4. Port Conflicts
```bash
# Error: Port already in use
# Solution: Kill process using the port
lsof -ti:8080 | xargs kill -9
```

#### 5. SSL Certificate Issues
```bash
# Error: SSL handshake failed
# Solution: Add JVM argument
-Dcom.sun.net.ssl.checkRevocation=false
```

### Debug Mode

#### Enable Debug Logging
```bash
# Run with debug logging
mvn test -Dlogback.configurationFile=src/test/resources/logback-debug.xml
```

#### Debug in IDE
1. Set breakpoints in test code
2. Run in debug mode
3. Step through execution

### Performance Optimization

#### JVM Tuning
```bash
# Increase heap size
export MAVEN_OPTS="-Xmx2g -Xms1g"

# Garbage collection tuning
export MAVEN_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

#### Parallel Execution Tuning
```properties
# Adjust thread count based on system capabilities
parallel.thread.count=5
```

## CI/CD Integration

### GitHub Actions
The project includes `.github/workflows/weekly-tests.yml` for automated testing.

### Jenkins Integration
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn test -Denvironment=qa -Dsuite=regression'
            }
        }
        stage('Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }
}
```

### Docker Integration
```dockerfile
FROM openjdk:11-jdk-slim

WORKDIR /app
COPY . .

RUN ./mvnw clean install -DskipTests

CMD ["./mvnw", "test", "-Denvironment=docker"]
```

## Next Steps

1. **Explore Test Structure**: Review existing test classes
2. **Create New Tests**: Follow the patterns in existing tests
3. **Customize Configuration**: Modify environment properties
4. **Set Up CI/CD**: Configure automated test execution
5. **Generate Reports**: Set up regular report generation

## Support

For additional help:
- Check the main README.md
- Review existing test examples
- Create GitHub issues for bugs
- Consult the troubleshooting section