package org.example.multidatabase.integration;

import org.example.generated.model.UserResponse;
import org.example.multidatabase.config.AppDataSourcesConfig;
import org.example.multidatabase.config.DynamicDataSourceFactory;
import org.example.multidatabase.dto.UserDto;
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
public class MultiDatabaseApplicationTests extends BaseIntegrationTest {


    @Autowired
    private AppDataSourcesConfig config;

    @Autowired
    private DynamicDataSourceFactory factory;

    @Autowired
    UserAggregatorService userAggregatorService;

    @BeforeAll
    void initTables() {
        initTablesFromConfig(config, factory);
    }

    @Test
    void shouldReturnAllUsersWhenNoFilters() {
        List<UserResponse> users = userAggregatorService.getAllUsers(null, null, null);
        assertThat(users)
                .hasSize(3)
                .extracting(UserResponse::getUsername)
                .containsExactlyInAnyOrder("users-db_login", "mysql-db_login", "oracle-db_login");
    }

    @Test
    void shouldFilterByLoginContainingL() {
        List<UserResponse> users = userAggregatorService.getAllUsers(null, null, "r");
        assertThat(users)
                .hasSize(2)
                .extracting(UserResponse::getUsername)
                .containsExactlyInAnyOrder("users-db_login", "oracle-db_login");
    }

    @Test
    void shouldFilterByExactLogin() {
        List<UserResponse> users = userAggregatorService.getAllUsers(null, null, "oracle-db_login");
        assertThat(users).hasSize(1);
        assertThat(users.getFirst().getUsername()).isEqualTo("oracle-db_login");
    }
}
