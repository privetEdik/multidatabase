# 📦 MultiDatabase

**MultiDatabase** is a REST API service that aggregates users from multiple relational databases (PostgreSQL, MySQL, Oracle), supports filtering by parameters, and performs asynchronous aggregation.

---

## 🐳 How to Run the Project

1. Clone the repository:
2. Open project in Intallij IDE
3. Open terminal in IDE. you must be in the root folder of the project
4. Run the following command.It will launch four containers: two PostgreSQL, one MySQL, one Oracle. Wait for the images to load and the containers to start
   ```bash
   docker compose up -d

5. After the containers are launched, run the scripts for loading data into the databases. With their help you will be able to work with the dev version of the api. The scripts will wait for the containers to be ready. This script will download postgres and mysql
   ```bash
   ./scripts/init-all.sh
   
6. This script will download oracle
   ```bash
   ./scripts/init-oracle-wrapper.sh

7. Run the application from the main class MultiDatabaseApplication.java
8. Generate code from Open API
   ```bash
   Gradle → multidatabase → Tasks → openapi tools → openApiGenerate
9. Now you can check the API from the browser or adapt it to Postman
   ```bash
   http://localhost:8080/users
   ```
   response - 8 objects;
      ```bash
   http://localhost:8080/users?firstName=Alex&lastName=Fedorov
   ```
   response - 1 object;
      ```bash
   http://localhost:8080/users?login=o
   ```
   response - 2 objects;


## 🐳 How to Run the Tests

1. I recommend stopping the dev version and deleting containers to save resources before running tests (you can skip this). Run in the terminal:
      ```bash
   docker compose down -v
   ```
2. To run the ordered version of the tests, navigate through the tabs in Intellij IDE.
   ```bash
   Gradle → Tasks → verification → test
   ```

3. I recommend deleting images after use 