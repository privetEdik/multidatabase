package org.example.multidatabase.config;

public record DataSourceProperties(
        String name,
        String url,
        String user,
        String password,
        String table,
        Mapping mapping
) {
    public record Mapping(String id, String username, String name, String surname) {
    }
}



