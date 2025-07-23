package org.example.multidatabase.controller;

import org.example.generated.api.UsersApi;
import org.example.generated.model.UserResponse;
import org.example.multidatabase.service.UserAggregatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersApiImpl implements UsersApi {

    private final UserAggregatorService userAggregatorService;

    public UsersApiImpl(UserAggregatorService userAggregatorService) {
        this.userAggregatorService = userAggregatorService;
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers(String firstName, String lastName, String login) {
        return ResponseEntity.ok(userAggregatorService.getAllUsers(firstName, lastName, login));
    }
}

