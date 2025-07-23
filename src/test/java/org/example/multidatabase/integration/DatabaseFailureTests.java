package org.example.multidatabase.integration;

import org.example.generated.model.UserResponse;
import org.example.multidatabase.config.AppDataSourcesConfig;
import org.example.multidatabase.config.DynamicDataSourceFactory;
import org.example.multidatabase.service.UserAggregatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DatabaseFailureTests extends BaseIntegrationTest {

    @Autowired
    private AppDataSourcesConfig config;

    @Autowired
    private DynamicDataSourceFactory factory;

    @Autowired
    private UserAggregatorService userAggregatorService;

    @BeforeAll
    void initTables() {
        initTablesFromConfig(config, factory);
    }

    @Test
    void shouldReturnUsersEvenIfMySQLIsDown() {
        mysql.stop();

        List<UserResponse> users = userAggregatorService.getAllUsers(null, null, null);

        assertThat(users)
                .extracting(UserResponse::getUsername)
                .doesNotContain("mysql-db_login")
                .contains("users-db_login", "oracle-db_login");

        postgresUsers.stop();

        List<UserResponse> users2 = userAggregatorService.getAllUsers(null, null, null);

        assertThat(users2)
                .extracting(UserResponse::getUsername)
                .doesNotContain("mysql-db_login", "users-db_login")
                .contains("oracle-db_login");

        oracle.stop();

        List<UserResponse> users3 = userAggregatorService.getAllUsers(null, null, null);

        assertThat(users3).isEmpty();
    }

}
