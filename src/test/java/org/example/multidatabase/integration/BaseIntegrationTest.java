package org.example.multidatabase.integration;

import org.example.multidatabase.config.AppDataSourcesConfig;
import org.example.multidatabase.config.DataSourceProperties;
import org.example.multidatabase.config.DynamicDataSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
public abstract class BaseIntegrationTest {

    static final PostgreSQLContainer<?> postgresUsers = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("users")
            .withUsername("user1")
            .withPassword("pass1");

    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("employees")
            .withUsername("user2")
            .withPassword("pass2");

    static final OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("test");

    static {
        postgresUsers.start();
        mysql.start();
        oracle.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("app.data-sources[0].url", postgresUsers::getJdbcUrl);
        registry.add("app.data-sources[0].user", postgresUsers::getUsername);
        registry.add("app.data-sources[0].password", postgresUsers::getPassword);
        registry.add("app.data-sources[0].name", () -> "users-db");
        registry.add("app.data-sources[0].table", () -> "users");
        registry.add("app.data-sources[0].mapping.id", () -> "id");
        registry.add("app.data-sources[0].mapping.username", () -> "username");
        registry.add("app.data-sources[0].mapping.name", () -> "first_name");
        registry.add("app.data-sources[0].mapping.surname", () -> "last_name");

        registry.add("app.data-sources[1].url", mysql::getJdbcUrl);
        registry.add("app.data-sources[1].user", mysql::getUsername);
        registry.add("app.data-sources[1].password", mysql::getPassword);
        registry.add("app.data-sources[1].name", () -> "mysql-db");
        registry.add("app.data-sources[1].table", () -> "employees");
        registry.add("app.data-sources[1].mapping.id", () -> "emp_id");
        registry.add("app.data-sources[1].mapping.username", () -> "login");
        registry.add("app.data-sources[1].mapping.name", () -> "name");
        registry.add("app.data-sources[1].mapping.surname", () -> "surname");

        registry.add("app.data-sources[2].url", oracle::getJdbcUrl);
        registry.add("app.data-sources[2].user", oracle::getUsername);
        registry.add("app.data-sources[2].password", oracle::getPassword);
        registry.add("app.data-sources[2].name", () -> "oracle-db");
        registry.add("app.data-sources[2].table", () -> "workers");
        registry.add("app.data-sources[2].mapping.id", () -> "worker_id");
        registry.add("app.data-sources[2].mapping.username", () -> "login");
        registry.add("app.data-sources[2].mapping.name", () -> "first_name");
        registry.add("app.data-sources[2].mapping.surname", () -> "last_name");
    }

    public static void initTablesFromConfig(AppDataSourcesConfig config, DynamicDataSourceFactory factory) {
        for (DataSourceProperties ds : config.getDataSources()) {
            String table = ds.table();
            String id = ds.mapping().id();
            String login = ds.mapping().username();
            String name = ds.mapping().name();
            String surname = ds.mapping().surname();

            String createSql = String.format("""
                    CREATE TABLE %s (
                        %s VARCHAR(255) PRIMARY KEY,
                        %s VARCHAR(255),
                        %s VARCHAR(255),
                        %s VARCHAR(255)
                    )
                    """, table, id, login, name, surname);

            String insertSql = String.format("""
                    INSERT INTO %s (%s, %s, %s, %s)
                    VALUES (?, ?, ?, ?)
                    """, table, id, login, name, surname);

            JdbcTemplate jdbc = factory.getTemplate(ds.name());
            try {
                jdbc.execute("DROP TABLE " + table);
            } catch (Exception e) {
                // ignore: table might not exist
            }

            jdbc.execute(createSql);
            jdbc.update(insertSql,
                    UUID.randomUUID().toString(),
                    ds.name() + "_login",
                    ds.name() + "_name",
                    ds.name() + "_surname"
            );
        }
    }

}

