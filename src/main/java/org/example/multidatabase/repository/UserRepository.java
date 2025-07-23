package org.example.multidatabase.repository;

import org.example.multidatabase.config.DataSourceProperties;
import org.example.multidatabase.dto.UserDto;

import java.util.List;

public interface UserRepository {
    List<UserDto> fetchUsers(DataSourceProperties ds, String firstName, String lastName, String login);
}
