# Caffee Kiosk

## 1. Overview

Caffee Kiosk is a simple Java desktop application for a coffee shop point-of-sale (POS) system, built to demonstrate key Object-Oriented Programming (OOP) principles and modern Java development practices. The application features a complete user flow, from employee login to order placement, payment processing, and receipt generation.

A key architectural feature of this project is its **pluggable data source layer**. The application can be configured to run against a live PostgreSQL database or use a local, file-based mock data source by changing a single configuration setting, demonstrating the power of dependency injection and service-oriented architecture.

**Note**: This project was created as a final project for a Object-Oriented Programming course.

## 2. Tech Stack & Versions

- **Language**: `Java 21`
- **UI Framework**: `JavaFX 21`
- **Build Tool**: `Maven`
- **Database**: `PostgreSQL` (Optional)
- **JSON Parsing**: `Jackson Databind`
- **Logging**: `SLF4J 2.x` & `Log4j 2.x`
- **Testing**: `JUnit 5` & `Mockito 5.x`
- **Password Hashing**: `jBCrypt`

## 3. Project Setup and Configuration

To run this project successfully, you need to configure your environment correctly. Follow these steps carefully.

### 3.1. Prerequisites

- **Java Development Kit (JDK)**: Version **21** is required.
- **IDE**: An IDE that supports Maven projects, such as IntelliJ IDEA.
- **PostgreSQL**: (Optional) Required only if you want to use the database connection mode.

### 3.2. Data Source Configuration (app.properties)

The application uses a properties file to determine which data source to use. This file is located at `src/main/resources/app.properties`.

#### **Default Mode: InternalFile**

By default, the application is configured to run in `InternalFile` mode, which requires no database setup. It reads data directly from the JSON files located in the `src/main/resources/data/` directory.

To use this mode, ensure the following line is in your `app.properties` file:
```properties
data.source=InternalFile
```

#### **Optional Mode: DBConnection**

If you want to run the application against a live PostgreSQL database, you need to:

1.  **Set the Data Source**: Change the `app.properties` file to:
    ```properties
    data.source=DBConnection
    ```

2.  **Configure Credentials**: Update the `db.url`, `db.user`, and `db.password` properties in the same file with your database credentials.
    ```properties
    db.url=jdbc:postgresql://localhost:5432/OOP_Caffee
    db.user=postgres
    db.password=your_secret_password
    ```

3.  **Set up the Database**: Make sure you have created the `OOP_Caffee` database and run the necessary SQL scripts found in the `database_schema` directory.

### 3.3. IDE Configuration (IntelliJ IDEA Example)

It is **crucial** that your IDE is configured to use **JDK 21** to compile and run the project.

1.  **Set the Project SDK**: Go to **File > Project Structure...**, select **Project**, and set the **SDK** to your installed **JDK 21**.
2.  **Set the Maven Runner JRE**: Go to **File > Settings... > Build, Execution, Deployment > Build Tools > Maven > Runner**, and set the **JRE** to your **JDK 21**.
3.  **Reload Maven**: Allow the IDE to reload the Maven project if prompted.

## 4. How to Run the Application

1.  **Configure `app.properties`**: Ensure the `data.source` is set to your desired mode (`InternalFile` or `DBConnection`).
2.  **Run via IDE**: Navigate to the `app.Launcher` class and run its `main()` method.
3.  **Run via Maven**: Open a terminal in the project root and run `mvn clean javafx:run`.

## 5. How to Run Tests

Open the **Maven** tool window, expand **Lifecycle**, and double-click the **`test`** goal. Alternatively, run `mvn clean test` from your terminal.

## 6. Project Structure

- **`src/main/java`**: Contains the main application source code.
- **`src/main/resources`**: Contains non-code resources, including FXML files, properties, and the `data` directory for InternalFile mode.
- **`src/test/java`**: Contains all unit tests.
- **`pom.xml`**: The Maven Project Object Model file.
- **`logs/`**: The directory where log files are generated.