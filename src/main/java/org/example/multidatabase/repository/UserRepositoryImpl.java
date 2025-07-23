package org.example.multidatabase.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multidatabase.config.DataSourceProperties;
import org.example.multidatabase.config.DynamicDataSourceFactory;
import org.example.multidatabase.dto.UserDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String LIKE_TEMPLATE = "%%%s%%";
    private static final String AND_LOWER = " AND LOWER(";
    private static final String LIKE_LOWER = ") LIKE LOWER(?)";

    private final DynamicDataSourceFactory dataSourceFactory;

    @Override
    public List<UserDto> fetchUsers(DataSourceProperties ds, String firstName, String lastName, String login) {
        try {
            JdbcTemplate jdbc = dataSourceFactory.getTemplate(ds.name());

            StringBuilder sql = new StringBuilder(String.format(
                    "SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s WHERE 1=1",
                    ds.mapping().id(),
                    ds.mapping().username(),
                    ds.mapping().name(),
                    ds.mapping().surname(),
                    ds.table()
            ));

            List<Object> params = new ArrayList<>();

            if (firstName != null && !firstName.isBlank()) {
                sql.append(AND_LOWER).append(ds.mapping().name()).append(LIKE_LOWER);
                params.add(String.format(LIKE_TEMPLATE, firstName));
            }

            if (lastName != null && !lastName.isBlank()) {
                sql.append(AND_LOWER).append(ds.mapping().surname()).append(LIKE_LOWER);
                params.add(String.format(LIKE_TEMPLATE, lastName));
            }

            if (login != null && !login.isBlank()) {
                sql.append(AND_LOWER).append(ds.mapping().username()).append(LIKE_LOWER);
                params.add(String.format(LIKE_TEMPLATE, login));
            }

            return jdbc.query(sql.toString(), (rs, rowNum) -> new UserDto(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("surname")
            ), params.toArray(new Object[0]));

        } catch (Exception ex) {
            log.warn("[{}] DB unavailable ({}): {}", ds.name(), ds.url(), ex.getMessage());
            return List.of();
        }
    }
}

