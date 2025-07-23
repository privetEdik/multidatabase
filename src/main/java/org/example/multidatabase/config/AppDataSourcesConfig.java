package org.example.multidatabase.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppDataSourcesConfig {
    private final List<DataSourceProperties> dataSources;

    public AppDataSourcesConfig(List<DataSourceProperties> dataSources) {
        this.dataSources = List.copyOf(dataSources);
    }
}

