package org.example.multidatabase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.generated.model.UserResponse;
import org.example.multidatabase.config.AppDataSourcesConfig;
import org.example.multidatabase.config.DataSourceProperties;
import org.example.multidatabase.dto.UserDto;
import org.example.multidatabase.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAggregatorService {

    private final AppDataSourcesConfig config;
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers(String firstName, String lastName, String login) {
        return config.getDataSources().stream()
                .map(ds -> CompletableFuture.supplyAsync(() ->
                        safeFetch(ds, firstName, lastName, login)))
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .map(this::mapToResponse)
                .toList();
    }

    private List<UserDto> safeFetch(DataSourceProperties ds, String firstName, String lastName, String login) {
        try {
            return userRepository.fetchUsers(ds, firstName, lastName, login);
        } catch (Exception e) {
            log.error("Error fetching users from data source {}: {}", ds.name(), e.getMessage(), e);
            return List.of();
        }
    }

    private UserResponse mapToResponse(UserDto dto) {
        return new UserResponse()
                .id(dto.id())
                .username(dto.username())
                .name(dto.name())
                .surname(dto.surname());
    }
}



