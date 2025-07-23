package org.example.multidatabase.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DynamicDataSourceFactory {

    private final Map<String, JdbcTemplate> jdbcTemplates;

    public DynamicDataSourceFactory(AppDataSourcesConfig config) {
        this.jdbcTemplates = config.getDataSources().stream()
                .collect(Collectors.toUnmodifiableMap(
                        DataSourceProperties::name,
                        ds -> {
                            try {
                                Driver driver = DriverManager.getDriver(ds.url());
                                SimpleDriverDataSource dataSource = new SimpleDriverDataSource(
                                        driver,
                                        ds.url(),
                                        ds.user(),
                                        ds.password()
                                );
                                return new JdbcTemplate(dataSource);
                            } catch (SQLException e) {
                                log.error("JDBC driver not found or not suitable for URL: {}", ds.url());
                                throw new IllegalStateException("Cannot create DataSource for: " + ds.name(), e);
                            }
                        }
                ));
    }

    public JdbcTemplate getTemplate(String name) {
        return jdbcTemplates.get(name);
    }
}


