package org.hang.live.common.datasource.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Configuration that settles database connection
 *
 * @Author hang
 * @Date: Created in 18:06 2024/8/19
 * @Description
 */
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info("dataSource: what");
            LOGGER.info("dataSource: {}", dataSource);
            Connection connection = dataSource.getConnection();
        };
    }
}